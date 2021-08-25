using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using JetBrains.Util;
using Mono.Debugger.Soft;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Client;
using Mono.Debugging.Client.CallStacks;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using Mono.Debugging.Soft;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Evaluation
{
    [DebuggerSessionComponent(typeof(SoftDebuggerType))]
    public class GodotAdditionalValuesProvider : GodotAdditionalValuesProvider<Value>
    {
        public GodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<Value> valueServices,
                                             IOptions options, ILogger logger)
            : base(session, valueServices, options, logger)
        {
        }
    }

    public class GodotAdditionalValuesProvider<TValue> : IAdditionalValuesProvider
        where TValue : class
    {
        private readonly IDebuggerSession mySession;
        private readonly IValueServicesFacade<TValue> myValueServices;
        private readonly IOptions myOptions;
        private readonly ILogger myLogger;

        protected GodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<TValue> valueServices,
            IOptions options, ILogger logger)
        {
            mySession = session;
            myValueServices = valueServices;
            myOptions = options;
            myLogger = logger;
        }

        public IEnumerable<IValueEntity> GetAdditionalLocals(IStackFrame frame)
        {
            // Do nothing if "Allow property evaluations..." option is disabled.
            // The debugger works in two steps - get value entities/references, and then get value presentation.
            // Evaluation is always allowed in the first step, but depends on user options for the second. This allows
            // evaluation to calculate children, e.g. expanding the Results node of IEnumerable, but presentation might
            // require clicking "refresh". We should be returning un-evaluated value references here.
            // TODO: Make lazy
            if (!myOptions.ExtensionsEnabled || !mySession.EvaluationOptions.AllowTargetInvoke)
                yield break;

            // Add "Active Scene" as a top level item to mimic the Hierarchy window in Godot
            var activeScene = GetActiveScene(frame);
            if (activeScene != null)
                yield return activeScene.ToValue(myValueServices);
        }

        [CanBeNull]
        private IValueReference<TValue> GetActiveScene(IStackFrame frame)
        {
            return myLogger.CatchEvaluatorException<TValue, IValueReference<TValue>>(() =>
                {
                    var type = myValueServices.GetReifiedType(frame,
                                               "Godot.Engine, GodotSharp");
                    if (type == null)
                    {
                        myLogger.Warn("Unable to get typeof(Engine). Not a Godot project?");
                        return null;
                    }

                    var getActiveSceneMethod = type.MetadataType.GetMethods()
                        .FirstOrDefault(m => m.IsStatic && m.Parameters.Length == 0 && m.Name == "GetMainLoop");
                    if (getActiveSceneMethod == null)
                    {
                        myLogger.Warn("Unable to find Engine.GetMainLoop method");
                        return null;
                    }

                    // GetActiveScene can throw a UnityException if we call it from the wrong location, such as the
                    // constructor of a MonoBehaviour
                    var activeScene =
                        type.CallStaticMethod(frame, mySession.EvaluationOptions, getActiveSceneMethod);
                    if (activeScene == null)
                    {
                        myLogger.Warn("Unexpected response: Engine.GetMainLoop() == null");
                        return null;
                    }

                    // Don't show type presentation. We know it's a scene, the clue's in the name
                    return new SimpleValueReference<TValue>(activeScene, type.MetadataType,
                        "Active scene", ValueOriginKind.Property,
                        ValueFlags.None | ValueFlags.IsReadOnly | ValueFlags.IsDefaultTypePresentation, frame,
                        myValueServices.RoleFactory);
                }, exception => { });
        }
        //
        // [CanBeNull]
        // private IValueReference<TValue> GetThisGameObjectForMonoBehaviour(IStackFrame frame)
        // {
        //     return myLogger.CatchEvaluatorException<TValue, IValueReference<TValue>>(() =>
        //         {
        //             var thisObj = frame.GetThis(mySession.EvaluationOptions);
        //             if (thisObj?.DeclaredType?.FindTypeThroughHierarchy("Godot.Node") == null)
        //                 return null;
        //
        //             if (!(thisObj.GetPrimaryRole(mySession.EvaluationOptions) is IObjectValueRole<TValue> role))
        //             {
        //                 myLogger.Warn("Unable to get 'this' as object value");
        //                 return null;
        //             }
        //
        //             var gameObjectReference = role.GetInstanceMethodInvocationReference(new MethodSelector(m => m.Name == "GetTree" && m.Parameters.Length == 0));
        //             if (gameObjectReference == null)
        //             {
        //                 myLogger.Warn("Unable to find 'this.gameObject' as a property reference");
        //                 return null;
        //             }
        //
        //             // var gameObject = gameObjectReference..GetValue(mySession.EvaluationOptions);
        //             // var gameObjectType = gameObjectReference.GetValueType(mySession.EvaluationOptions,
        //             //     myValueServices.ValueMetadataProvider);
        //
        //             // Don't show type for each child game object. It's always "GameObject", and we know they're game
        //             // objects from the synthetic group.
        //             // return new SimpleValueReference<TValue>(gameObject, gameObjectType, "this.gameObject",
        //             //     ValueOriginKind.Property,
        //             //     ValueFlags.None | ValueFlags.IsDefaultTypePresentation | ValueFlags.IsReadOnly, frame,
        //             //     myValueServices.RoleFactory);
        //             return null;
        //         });
        // }
    }
}