using System;
using System.Collections.Generic;
using System.Threading;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values.ValueReferences;
using JetBrains.Util;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values.Render.ChildrenRenderers;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using Mono.Debugging.MetadataLite.API;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    // Adds "Children" and "Parent" rows to any Godot.Node value reached through code (a local, a field, "this").
    // Nodes reached through the remote scene tree are SceneTreeNodeValue instead, and present their children inline.
    [DebuggerSessionComponent]
    internal class NodeObjectChildrenRenderer<TValue> : ChildrenRendererBase<TValue, IObjectValueRole<TValue>>
        where TValue : class
    {
        private readonly IOptions myOptions;
        private readonly ILogger myLogger;
        private readonly NodeChildrenEnumerator<TValue> myChildrenEnumerator;

        public NodeObjectChildrenRenderer(IOptions options, ILogger logger,
            NodeChildrenEnumerator<TValue> childrenEnumerator)
        {
            myOptions = options;
            myLogger = logger;
            myChildrenEnumerator = childrenEnumerator;
        }

        // Higher than ObjectChildrenRenderer, so we're called first and our rows end up at the top. We're not
        // exclusive - the other renderers still contribute the ordinary object members below them.
        public override int Priority => 50;

        public override bool IsExclusive => false;

        protected override bool IsApplicable(IObjectValueRole<TValue> role, IMetadataTypeLite type,
            IPresentationOptions options, IUserDataHolder dataHolder)
        {
            return myOptions.ExtensionsEnabled && type.FindTypeThroughHierarchy("Godot.Node") != null;
        }

        protected override IEnumerable<IValueEntity> GetChildren(IObjectValueRole<TValue> valueRole,
            IMetadataTypeLite instanceType,
            IPresentationOptions options,
            IUserDataHolder dataHolder,
            CancellationToken token)
        {
            // Children are plain node values, not scene tree nodes. The group evaluates nothing until expanded, so
            // only the Parent row can fail here - and it must not take the Children row down with it.
            var entities = new List<IValueEntity> { new NodeChildrenGroup<TValue>(valueRole, myChildrenEnumerator) };

            var parentEntity = GetParentEntitySafe(valueRole, options);
            if (parentEntity != null)
                entities.Add(parentEntity);

            return entities;
        }

        // A failure here must cost us the Parent row only, so everything is absorbed - including an abort, which
        // would otherwise take the Children row down with it on its way out of GetChildren.
        private IValueEntity GetParentEntitySafe(IObjectValueRole<TValue> valueRole, IPresentationOptions options)
        {
            try
            {
                return GetParentEntity(valueRole);
            }
            catch (EvaluatorAbortedException e)
            {
                // Evaluation has been aborted, e.g. the user has continued before evaluation has completed
                myLogger.LogExceptionSilently(e);
            }
            catch (EvaluatorExceptionThrownException<TValue> e)
            {
                myLogger.LogThrown(e, valueRole.ValueReference.OriginatingFrame, ValueServices, options);
            }
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
            {
                // We're not expecting this exception, log it as an error so we can fix it
                myLogger.LogException(e);
            }

            return null;
        }

        // Lazily evaluated - the parent is only fetched when the row is expanded/presented. A scene root's null parent
        // is rendered as "null" by the standard null reference presenter
        private IValueEntity GetParentEntity(IObjectValueRole<TValue> valueRole)
        {
            var reference = valueRole.GetInstanceMethodInvocationReferenceSafeIfNotFound(
                MethodSelectors.NodeObject_GetParent) as IValueReference<TValue>;
            if (reference == null)
            {
                myLogger.Warn("Cannot find GetParent method on NodeObject");
                return null;
            }

            return new CalculatedValueReferenceDecorator<TValue>(reference, ValueServices.RoleFactory, "Parent")
                .ToValue(ValueServices);
        }
    }
}
