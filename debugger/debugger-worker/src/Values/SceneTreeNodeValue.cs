using System;
using System.Collections.Generic;
using System.Threading;
using JetBrains.Annotations;
using JetBrains.Util;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.Render.ChildrenRenderers;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using Mono.Debugging.MetadataLite.API;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// A node of the Godot remote scene tree, presented as:
    /// - child scene tree nodes INLINE
    /// - a single "Properties" group at the end
    ///
    /// Scene-tree rendering depends on how the node was reached, not on the value's type,
    /// so this is a value entity rather than an IChildrenRenderer (which dispatches on (role, type) only).
    /// Marking the IValueReference instead does not work: IObjectValueRole.Base reuses the derived role's reference,
    /// so the marker leaks onto base-class slices and causes infinite recursive re-rendering.
    /// </summary>
    internal sealed class SceneTreeNodeValue<TValue> : IValue<TValue>
        where TValue : class
    {
        private readonly IValue<TValue> myInner;
        private readonly NodeChildrenEnumerator<TValue> myChildrenEnumerator;
        private readonly IValueServicesFacade<TValue> myValueServices;
        private readonly ILogger myLogger;

        private SceneTreeNodeValue(
            [NotNull] IValue<TValue> inner,
            [NotNull] NodeChildrenEnumerator<TValue> childrenEnumerator,
            [NotNull] IValueServicesFacade<TValue> valueServices,
            [NotNull] ILogger logger)
        {
            myInner = inner;
            myChildrenEnumerator = childrenEnumerator;
            myValueServices = valueServices;
            myLogger = logger;
        }

        [NotNull]
        public static IValue<TValue> Create(
            [NotNull] IValueReference<TValue> reference,
            [NotNull] NodeChildrenEnumerator<TValue> childrenEnumerator,
            [NotNull] IValueServicesFacade<TValue> valueServices,
            [NotNull] ILogger logger)
        {
            return new SceneTreeNodeValue<TValue>(reference.ToValue(valueServices), childrenEnumerator, valueServices,
                logger);
        }

        public IEnumerable<IValueEntity> GetChildren(
            IPresentationOptions options,
            CancellationToken token = new CancellationToken())
        {
            var nodeRole = GetNodeRoleSafe(options);

            // Not an object (a null node, a value we failed to fetch) - let the platform present it
            if (nodeRole == null)
                return myInner.GetChildren(options, token);

            var children = myChildrenEnumerator.EnumerateChildrenSafe(nodeRole, options, token,
                myChildrenEnumerator.SceneTreeRows);
            return children == null
                ? myInner.GetChildren(options, token)
                : WithProperties(children, nodeRole, options, token);
        }

        /// <returns>Null if the value is not an object, or if fetching it failed.</returns>
        /// <remarks>
        /// An aborted or cancelled evaluation propagates rather than being reported as null, because null sends
        /// <see cref="GetChildren"/> down the fallback path and into a second evaluation that cannot succeed either.
        /// </remarks>
        [CanBeNull]
        private IObjectValueRole<TValue> GetNodeRoleSafe(IPresentationOptions options)
        {
            try
            {
                return myInner.GetPrimaryRole(options) as IObjectValueRole<TValue>;
            }
            catch (EvaluatorExceptionThrownException<TValue> e)
            {
                myLogger.LogThrown(e, myInner.ValueReference.OriginatingFrame, myValueServices, options);
            }
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
            {
                // We're not expecting this exception, log it as an error so we can fix it
                myLogger.LogException(e);
            }

            return null;
        }

        private IEnumerable<IValueEntity> WithProperties(
            IEnumerable<IValueEntity> children,
            IObjectValueRole<TValue> nodeRole,
            IPresentationOptions options,
            CancellationToken token)
        {
            foreach (var child in children)
                yield return child;

            // Lazy on purpose - the platform's member enumeration of a Godot type is expensive and most nodes are
            // never expanded this far.
            yield return new SimpleEntityGroup(GroupNames.Properties,
                EnumerateProperties(nodeRole, options, token), isTop: false);
        }

        private IEnumerable<IValueEntity> EnumerateProperties(
            IObjectValueRole<TValue> nodeRole,
            IPresentationOptions options, CancellationToken token)
        {
            return options.FlattenHierarchy
                ? ChildrenRenderingUtil.EnumerateMembersFlat(nodeRole, options, token, myValueServices)
                : ChildrenRenderingUtil.EnumerateMembersWithBaseNode(nodeRole, options, token, myValueServices);
        }

        public IValueKeyPresentation GetKeyPresentation(
            IPresentationOptions options,
            CancellationToken token = new CancellationToken())
        {
            return myInner.GetKeyPresentation(options, token);
        }

        public IValuePresentation GetValuePresentation(
            IPresentationOptions options,
            CancellationToken token = new CancellationToken())
        {
            return myInner.GetValuePresentation(options, token);
        }

        public string SimpleName => myInner.SimpleName;

        public IMetadataTypeLite DeclaredType => myInner.DeclaredType;

        public IValueRole GetPrimaryRole(IValueFetchOptions options) => myInner.GetPrimaryRole(options);

        public IValueReference<TValue> ValueReference => myInner.ValueReference;

        public IEnumerable<IValueReference<TValue>> GetChildValues(IValueFetchOptions options)
        {
            return myInner.GetChildValues(options);
        }

        public bool IsWriteable => myInner.IsWriteable;

        public void SetValueFrom(IValue newValue, IValueFetchOptions options) =>
            myInner.SetValueFrom(newValue, options);

        public void ResetCache() => myInner.ResetCache();
    }
}
