using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using JetBrains.Annotations;
using JetBrains.Application.I18n;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Resources;
using JetBrains.Util;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.CallStacks;
using Mono.Debugging.Client.DebuggerOptions;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using Mono.Debugging.MetadataLite.API;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// Fetches the children of a Godot.Node as value entities. Shared by the grouped "Children" category and the
    /// inline scene-tree presentation, which differ only in the <see cref="INodeRowFactory{TValue}"/> they pass.
    /// </summary>
    /// <remarks>
    /// A session component, so "can this debuggee run our helper lambdas?" is learned at most once per debug session
    /// no matter which presentation path asks - see <see cref="myBulkFetchUnavailable"/>.
    /// </remarks>
    [DebuggerSessionComponent]
    internal class NodeChildrenEnumerator<TValue>
        where TValue : class
    {
        private readonly IValueServicesFacade<TValue> myValueServices;
        private readonly ILogger myLogger;

        // Set once the debuggee has proved it cannot run our helper lambdas.
        // Remember it and fall back to slow path
        private bool myBulkFetchUnavailable;

        public NodeChildrenEnumerator([NotNull] IValueServicesFacade<TValue> valueServices, [NotNull] ILogger logger)
        {
            myValueServices = valueServices;
            myLogger = logger;
            SceneTreeRows = new SceneTreeRowFactory<TValue>(valueServices, this);
            PlainRows = new PlainNodeRowFactory<TValue>(valueServices);
        }

        /// <summary>Rows for the inline remote scene tree.</summary>
        [NotNull]
        public INodeRowFactory<TValue> SceneTreeRows { get; }

        /// <summary>Rows for the "Children" category of a node reached through code.</summary>
        [NotNull]
        public INodeRowFactory<TValue> PlainRows { get; }

        /// <summary>
        /// Presents <paramref name="reference"/> as a node of the remote scene tree.
        /// </summary>
        [NotNull]
        public IValue<TValue> CreateSceneTreeNode([NotNull] IValueReference<TValue> reference) =>
            SceneTreeNodeValue<TValue>.Create(reference, this, myValueServices, myLogger);

        /// <summary>
        /// <see cref="EnumerateChildren"/>, materialised so failures surface here rather than mid-render.
        /// </summary>
        /// <returns>
        /// Null if the enumeration failed - callers choose between showing nothing and falling back to another
        /// presentation. An empty list means the node really has no children.
        /// </returns>
        /// <remarks>
        /// An aborted or cancelled evaluation is not a failure and is left to propagate. Reporting it as null would
        /// tell the caller to try a different presentation, and that second attempt is just as doomed as this one -
        /// see <see cref="EvaluationFailures.ShouldPropagate"/>.
        /// </remarks>
        [CanBeNull]
        public IReadOnlyList<IValueEntity> EnumerateChildrenSafe(
            IObjectValueRole<TValue> nodeRole,
            IPresentationOptions options,
            CancellationToken token,
            INodeRowFactory<TValue> rows)
        {
            try
            {
                return EnumerateChildren(nodeRole, options, token, rows).ToList();
            }
            catch (EvaluatorExceptionThrownException<TValue> e)
            {
                myLogger.LogThrown(e, nodeRole.ValueReference.OriginatingFrame, myValueServices, options);
            }
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
            {
                // We're not expecting this exception, log it as an error so we can fix it
                myLogger.LogException(e);
            }

            return null;
        }

        /// <summary>
        /// Enumerates the children of a Godot.Node as value entities.
        /// </summary>
        public IEnumerable<IValueEntity> EnumerateChildren(
            IObjectValueRole<TValue> nodeRole,
            IPresentationOptions options,
            CancellationToken token,
            INodeRowFactory<TValue> rows)
        {
            token.ThrowIfCancellationRequested();

            var nodeType = nodeRole.ReifiedType.MetadataType.FindTypeThroughHierarchy("Godot.Node");
            if (nodeType == null)
            {
                // Expected - the scene tree path can be handed any value. Not an error, and nothing to show.
                myLogger.Trace("Not a Godot.Node, no children to enumerate");
                yield break;
            }

            var getChildrenMethod = MetadataTypeLiteEx.LookupInstanceMethodSafe(nodeType,
                MethodSelectors.NodeObject_GetChildren, includeBases: false);
            if (getChildrenMethod == null)
            {
                myLogger.Warn("Unable to find Node.GetChildren method");
                yield return new ErrorValue(ErrorValue.RowName,
                    Strings.NodeChildrenEnumerator_GetChildrenMethodNotFound);
                yield break;
            }

            var frame = nodeRole.ValueReference.OriginatingFrame;

            token.ThrowIfCancellationRequested();

            // Fetch all children in one call
            IObjectValueRole<TValue> childrenRole = null;
            IValueEntity errorEntity = null;
            try
            {
                var childrenValue = getChildrenMethod.Parameters.Length == 0
                    ? nodeRole.CallInstanceMethod(options, getChildrenMethod)
                    : nodeRole.CallInstanceMethod(options, getChildrenMethod,
                        myValueServices.ValueFactory.CreatePrimitive(frame, options, false));
                childrenRole = new SimpleValueReference<TValue>(childrenValue, frame, myValueServices.RoleFactory)
                    .AsObjectSafe(options);
            }
            // EvaluatorAbortedException is an EvaluatorException, so the ShouldPropagate guard has to come first:
            // an evaluation the user aborted by stepping or resuming must not be reported as a failed node.
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e) &&
                                      e is EvaluatorException or ValueException)
            {
                myLogger.LogExceptionSilently(e);
                var description = ThrownDebuggeeException.Describe(e, frame, myValueServices, options, myLogger);
                errorEntity = new ErrorValue(ErrorValue.RowName,
                    Strings.NodeChildrenEnumerator_FailedToGetChildren.Format(description.NON_LOCALIZABLE()));
            }

            if (errorEntity != null)
            {
                yield return errorEntity;
                yield break;
            }

            if (childrenRole == null)
            {
                myLogger.Trace("GetChildren returned null, or unable to fetch children");
                yield break;
            }

            token.ThrowIfCancellationRequested();

            var countPrimitive = childrenRole.GetInstancePropertyReference("Count", searchInBases: true)
                ?.AsPrimitiveSafe(options);
            var childCount = countPrimitive?.GetPrimitiveSafe<int>() ?? 0;
            if (childCount == 0)
            {
                myLogger.Trace("No children, or unable to fetch child count");
                yield break;
            }

            token.ThrowIfCancellationRequested();

            // Fast path: target invocation and name-transfer costs are constant; child values are fetched lazily.
            if (ChildFetchStrategy.ShouldBulkFetch(childCount) &&
                TryBulkFetchChildren(childrenRole, options, token, out var childrenArray, out var names))
            {
                foreach (var child in ChildChunking.SplitIntoChunks<IValueEntity>(0, names.Length,
                             index => rows.CreateChild(childrenArray.GetElementReference(index), names[index], index),
                             rows.CreateChunkGroup, token))
                    yield return child;

                yield break;
            }

            // Slow path: walk the array through its indexer, resolving each name individually (one invocation each).
            var getItemMethod = MetadataTypeLiteEx.LookupInstanceMethodSafe(childrenRole.ReifiedType.MetadataType,
                MethodSelectors.ArrayLike_GetItem, includeBases: false);
            if (getItemMethod == null)
            {
                myLogger.Warn("Unable to find indexer method on the array returned by Node.GetChildren");
                yield return new ErrorValue(ErrorValue.RowName,
                    Strings.NodeChildrenEnumerator_ArrayIndexerMethodNotFound);
                yield break;
            }

            // Looked up lazily below and reused; assumes all children's Name properties share one type (StringName).
            var childNameMetadata = new ChildNameMetadata();
            foreach (var child in ChildChunking.SplitIntoChunks<IValueEntity>(0, childCount,
                         index => GetElementValueAt(childrenRole, index, getItemMethod, childNameMetadata, options,
                             token, rows),
                         rows.CreateChunkGroup, token))
                yield return child;
        }

        /// <summary>
        /// Compiles two lambdas and runs them inside the debuggee. Child references arrive as a CLR array and names
        /// as a single manifest string, avoiding a separate debugger request for every name. Returns false if
        /// anything goes wrong, in which case the caller falls back to fetching children one by one.
        /// </summary>
        private bool TryBulkFetchChildren(
            IObjectValueRole<TValue> childrenRole,
            IValueFetchOptions options,
            CancellationToken token,
            out IArrayValueRole<TValue> childrenArray,
            out string[] names)
        {
            childrenArray = null;
            names = null;

            // Compiling and invoking lambdas needs the debuggee to run our code. Not a permanent verdict - the user
            // can turn target invocation back on - so don't remember it.
            if (!options.AllowTargetInvoke)
                return false;

            if (myBulkFetchUnavailable)
            {
                myLogger.Trace("Bulk fetch of Godot.Node children is unavailable in this session, fetching one by one");
                return false;
            }

            var frame = childrenRole.ValueReference.OriginatingFrame;
            try
            {
                if (TryBulkFetchChildrenCore(childrenRole, frame, options, token, out childrenArray, out names))
                    return true;

                myLogger.Warn("Unable to bulk fetch the children of this Godot.Node, fetching them one by one.");
            }
            catch (Exception e)
            {
                switch (BulkFetchFailureClassifier.Classify(e))
                {
                    case BulkFetchFailureAction.Rethrow:
                        throw;

                    case BulkFetchFailureAction.RetryLater:
                        myLogger.LogExceptionSilently(e);
                        break;

                    default:
                        myBulkFetchUnavailable = true;
                        myLogger.Warn(e, ExceptionOrigin.Algorithmic,
                            "Unable to bulk fetch Godot.Node children, falling back to fetching them one by one " +
                            "for the rest of the session.");
                        break;
                }
            }

            childrenArray = null;
            names = null;
            return false;
        }

        private bool TryBulkFetchChildrenCore(
            IObjectValueRole<TValue> childrenRole,
            IStackFrame frame,
            IValueFetchOptions options,
            CancellationToken token,
            out IArrayValueRole<TValue> childrenArray,
            out string[] names)
        {
            childrenArray = null;
            names = null;

            // Godot.Collections.Array<Node> returned by GetChildren, passed to the lambdas as a plain IEnumerable
            var childrenValue = childrenRole.ValueReference.GetValue(options);

            var childrenArrayValue = InvokeEnumerableFunc(DebuggeeLambdas.ChildrenArray, childrenValue, frame, options,
                token);
            if (childrenArrayValue == null)
                return false;

            var arrayRole = new SimpleValueReference<TValue>(childrenArrayValue, frame, myValueServices.RoleFactory)
                .GetExactPrimaryRoleSafe<TValue, IArrayValueRole<TValue>>(options);
            var dimensions = arrayRole?.Dimensions;
            if (dimensions == null || dimensions.Length != 1)
                return false;

            var namesManifestValue =
                InvokeEnumerableFunc(DebuggeeLambdas.ChildNames, childrenValue, frame, options, token);
            if (namesManifestValue == null)
                return false;

            var namesManifest = new SimpleValueReference<TValue>(namesManifestValue, frame, myValueServices.RoleFactory)
                .AsStringSafe(options)
                ?.GetString();

            token.ThrowIfCancellationRequested();

            if (!ChildNamesManifest.TryParse(namesManifest, dimensions[0], out names))
                return false;

            childrenArray = arrayRole;
            return true;
        }

        /// <summary>
        /// Compiles <paramref name="lambdaExpression"/> into a delegate living in the debuggee and invokes it with
        /// the given enumerable.
        /// </summary>
        private TValue InvokeEnumerableFunc(
            string lambdaExpression,
            TValue enumerableValue,
            IStackFrame frame,
            IValueFetchOptions options,
            CancellationToken token)
        {
            token.ThrowIfCancellationRequested();

            var cachedOptions = options.AllowCachingCompilationResult();
            var func = frame.EvaluateExpression(lambdaExpression, frame.DebuggerSession.EvaluationOptions.Apply(cachedOptions));
            if (func?.GetPrimaryRole(cachedOptions) is not IValueRole<TValue> funcRole)
                return null;

            token.ThrowIfCancellationRequested();

            var funcObject = funcRole.ValueReference.AsObjectSafe(cachedOptions);
            var invokeMethod = funcObject == null
                ? null
                : MetadataTypeLiteEx.LookupInstanceMethodSafe(funcObject.ReifiedType.MetadataType,
                    MethodSelectors.Delegate_Invoke);
            return invokeMethod == null
                ? null
                : funcObject.CallInstanceMethod(cachedOptions, invokeMethod, enumerableValue);
        }

        private IValueEntity GetElementValueAt(
            IObjectValueRole<TValue> childrenRole,
            int index,
            IMetadataMethodLite getItemMethod,
            ChildNameMetadata childNameMetadata,
            IValueFetchOptions options,
            CancellationToken token,
            INodeRowFactory<TValue> rows)
        {
            try
            {
                var frame = childrenRole.ValueReference.OriginatingFrame;

                // this[index] on the array
                var indexValue = myValueServices.ValueFactory.CreatePrimitive(frame, options, index);
                var childNodeValue = childrenRole.CallInstanceMethod(options, getItemMethod, indexValue);

                var childNode = new SimpleValueReference<TValue>(childNodeValue, frame, myValueServices.RoleFactory)
                    .AsObjectSafe(options);
                if (childNode == null)
                    return new ErrorValue($"[{index}]", Strings.NodeChildrenEnumerator_ChildNodeIsNull);

                // Name is a string in Godot 3 and a StringName in Godot 4. Resolve StringName via ToString(), looked
                // up lazily once and reused.
                var nameProperty = childNode.GetInstancePropertyReference("Name", searchInBases: true);
                var nodeName = nameProperty?.AsStringSafe(options)?.GetString();
                var nameObject = nodeName == null ? nameProperty?.AsObjectSafe(options) : null;
                if (nameObject != null)
                {
                    // Resolving the name costs another round trip on top of the indexer call above
                    token.ThrowIfCancellationRequested();

                    try
                    {
                        childNameMetadata.ToStringMethod ??= MetadataTypeLiteEx.LookupInstanceMethodSafe(
                            nameObject.ReifiedType.MetadataType,
                            Mono.Debugging.MetadataLite.API.Selectors.MethodSelectors.ToString);

                        if (childNameMetadata.ToStringMethod != null)
                        {
                            var toStringInvocation =
                                nameObject.GetInstanceMethodInvocationReference(childNameMetadata.ToStringMethod);
                            nodeName = toStringInvocation?.AsStringSafe(options)?.GetString();
                        }
                    }
                    catch (EvaluatorException e)
                    {
                        myLogger.LogExceptionSilently(e);
                    }
                    catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
                    {
                        myLogger.Warn(e, ExceptionOrigin.Algorithmic,
                            $"Unable to invoke ToString() on Name property for child {index}");
                    }
                }

                return rows.CreateChild(childNode.ValueReference, nodeName, index);
            }
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
            {
                // Always show something - caller can't sensibly recover
                myLogger.LogExceptionSilently(e);
                return myValueServices.ValueRenderers.GetValueStubForException(e, $"[{index}]",
                           childrenRole.ValueReference.OriginatingFrame)
                       ?? new ErrorValue($"[{index}]", Strings.NodeChildrenEnumerator_UnableToRetrieveChildNode);
            }
        }

        private sealed class ChildNameMetadata
        {
            public IMetadataMethodLite ToStringMethod;
        }
    }
}
