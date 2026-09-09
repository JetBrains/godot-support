using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.Lifetimes;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using JetBrains.Util;
using Mono.Debugger.Soft;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client;
using Mono.Debugging.Client.CallStacks;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using Mono.Debugging.Soft;
using Mono.Debugging.Win32;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Evaluation
{
    [DebuggerSessionComponent(typeof(SoftDebuggerType))]
    internal class MonoGodotAdditionalValuesProvider : GodotAdditionalValuesProvider<Value>
    {
        public MonoGodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<Value> valueServices,
            IOptions options, ILogger logger, NodeChildrenEnumerator<Value> childrenEnumerator)
            : base(session, valueServices, options, logger, childrenEnumerator)
        {
        }
    }

    [DebuggerSessionComponent(typeof(CorDebuggerType))]
    internal class CorGodotAdditionalValuesProvider : GodotAdditionalValuesProvider<ICorValue>
    {
        public CorGodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<ICorValue> valueServices,
            IOptions options, ILogger logger, NodeChildrenEnumerator<ICorValue> childrenEnumerator)
            : base(session, valueServices, options, logger, childrenEnumerator)
        {
        }
    }

    internal class GodotAdditionalValuesProvider<TValue> : IAdditionalValuesProvider
        where TValue : class
    {
        private readonly IDebuggerSession mySession;
        private readonly IValueServicesFacade<TValue> myValueServices;
        private readonly IOptions myOptions;
        private readonly ILogger myLogger;
        private readonly NodeChildrenEnumerator<TValue> myChildrenEnumerator;
        private bool? myIsGodotModule;

        protected GodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<TValue> valueServices,
            IOptions options, ILogger logger, NodeChildrenEnumerator<TValue> childrenEnumerator)
        {
            mySession = session;
            myValueServices = valueServices;
            myOptions = options;
            myLogger = logger;
            myChildrenEnumerator = childrenEnumerator;
        }

        public IEnumerable<IValueEntity> GetAdditionalLocals(IStackFrame frame, Lifetime lifetime)
        {
            // Do nothing if "Allow property evaluations..." option is disabled.
            // The debugger works in two steps - get value entities/references, and then get value presentation.
            // Evaluation is always allowed in the first step, but depends on user options for the second. This allows
            // evaluation to calculate children, e.g. expanding the Results node of IEnumerable, but presentation might
            // require clicking "refresh".
            if (!myOptions.ExtensionsEnabled || !mySession.EvaluationOptions.AllowTargetInvoke)
                yield break;

            // Only needed for the Cor
            // Avoid evaluating Scene when running not in the Game, for example in the unit-tests
            if (this is CorGodotAdditionalValuesProvider)
            {
                if (!myIsGodotModule.HasValue)
                    myIsGodotModule = IsGodotProcess(frame.DebuggerSession);

                if (!myIsGodotModule.Value)
                    yield break;
            }

            // Add the scene tree root ("/root") as a top level item to mimic the Remote scene tree in Godot.
            var rootNode = GetSceneTreeRoot(frame, lifetime);
            if (rootNode != null)
                yield return myChildrenEnumerator.CreateSceneTreeNode(rootNode);
        }

        private bool IsGodotProcess(IDebuggerSession session)
        {
            var processId = session.GetProcessInfo().Id;
            if (processId == null) return false;

            try
            {
                int pid = Convert.ToInt32(processId);
                var mainModule = Process.GetProcessById(pid).MainModule;
                return mainModule != null &&
                       mainModule.ModuleName.StartsWith("godot", StringComparison.OrdinalIgnoreCase);
            }
            catch (Exception e)
            {
                // The process may have exited, or MainModule may be unreadable (access denied, bitness mismatch).
                // We are inside an iterator, so letting this out would drop every local, not just our row.
                myLogger.LogExceptionSilently(e);
                return false;
            }
        }

        // A failure here must cost us the scene tree row only - we are called from inside the GetAdditionalLocals
        // iterator, so anything that escapes drops every local instead of just our row.
        [CanBeNull]
        private IValueReference<TValue> GetSceneTreeRoot(IStackFrame frame, Lifetime lifetime)
        {
            try
            {
                var engineType = myValueServices.GetReifiedType(frame, "Godot.Engine, GodotSharp");
                if (engineType == null)
                {
                    myLogger.Warn("Unable to get typeof(Engine). Not a Godot project?");
                    return null;
                }

                lifetime.ThrowIfNotAlive();
                var getMainLoop = engineType.MetadataType.GetMethods()
                    .FirstOrDefault(m => m.IsStatic && m.Parameters.Length == 0 && m.Name == "GetMainLoop");
                if (getMainLoop == null)
                {
                    myLogger.Warn("Unable to find Engine.GetMainLoop method");
                    return null;
                }

                lifetime.ThrowIfNotAlive();
                // GetMainLoop can throw a exception if we call it from the wrong location
                var mainLoop = engineType.CallStaticMethod(frame, mySession.EvaluationOptions, getMainLoop);
                if (mainLoop == null)
                {
                    myLogger.Warn("Unexpected response: Engine.GetMainLoop() == null");
                    return null;
                }

                lifetime.ThrowIfNotAlive();
                var sceneTreeType = myValueServices.GetReifiedType(frame, "Godot.SceneTree, GodotSharp");
                if (sceneTreeType == null)
                {
                    myLogger.Warn("Unable to get typeof(SceneTree).");
                    return null;
                }

                var nodeType = myValueServices.GetReifiedType(frame, "Godot.Node, GodotSharp");
                if (nodeType == null)
                {
                    myLogger.Warn("Unable to get typeof(Node).");
                    return null;
                }

                var mainLoopReference = new SimpleValueReference<TValue>(mainLoop, sceneTreeType.MetadataType,
                    "MainLoop", ValueOriginKind.Property,
                    ValueFlags.None | ValueFlags.IsReadOnly | ValueFlags.IsTypeCanBeDerivedFromContext, frame,
                    myValueServices.RoleFactory);

                if (!(mainLoopReference.GetPrimaryRole(mySession.EvaluationOptions) is IObjectValueRole<TValue> role))
                {
                    myLogger.Warn("Unable to get role from 'MainLoop' reference");
                    return null;
                }

                lifetime.ThrowIfNotAlive();
                var rootReference = role.GetInstancePropertyReference(new[] { "Root" });
                if (rootReference == null)
                {
                    myLogger.Warn("Unexpected response: Root == null");
                    return null;
                }

                // The row name is not localized on purpose - `/root` node in Godot Editor
                return new SimpleValueReference<TValue>(rootReference.GetValue(mySession.EvaluationOptions),
                    nodeType.MetadataType, "root", ValueOriginKind.Property,
                    ValueFlags.None | ValueFlags.IsReadOnly | ValueFlags.IsTypeCanBeDerivedFromContext, frame,
                    myValueServices.RoleFactory);
            }
            catch (EvaluatorAbortedException e)
            {
                // Evaluation has been aborted, e.g. the user has continued before evaluation has completed
                myLogger.LogExceptionSilently(e);
            }
            catch (EvaluatorExceptionThrownException<TValue> e)
            {
                // Reaching the scene tree runs several Godot APIs that are only valid at some locations, so the
                // debuggee throwing here is expected rather than a failure - but record what it threw, because
                // "scene tree row missing and nothing in the log" is impossible to act on from a user report.
                myLogger.Verbose(e, comment: ThrownDebuggeeException.GetMessage(e, frame, myValueServices,
                    mySession.EvaluationOptions, myLogger));
            }
            catch (Exception e) when (!EvaluationFailures.ShouldPropagate(e))
            {
                // We're not expecting this exception, log it as an error so we can fix it
                myLogger.LogException(e);
            }

            return null;
        }
    }
}