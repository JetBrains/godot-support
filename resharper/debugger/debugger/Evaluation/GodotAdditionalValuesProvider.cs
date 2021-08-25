using System.Collections.Generic;
using JetBrains.Annotations;
using JetBrains.Util;
using MetadataLite.API.Selectors;
using Mono.Debugger.Soft;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client;
using Mono.Debugging.Client.CallStacks;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Evaluation;
using Mono.Debugging.Soft;

namespace JetBrains.ReSharper.Plugins.Godot.debugger.Evaluation
{
    [DebuggerSessionComponent(typeof(SoftDebuggerType))]
    public class GodotAdditionalValuesProvider : GodotAdditionalValuesProvider<Value>
    {
        public GodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<Value> valueServices,
                                             ILogger logger)
            : base(session, valueServices, logger)
        {
        }
    }

    public class GodotAdditionalValuesProvider<TValue> : IAdditionalValuesProvider
        where TValue : class
    {
        private readonly IDebuggerSession mySession;
        private readonly IValueServicesFacade<TValue> myValueServices;
        private readonly ILogger myLogger;

        protected GodotAdditionalValuesProvider(IDebuggerSession session, IValueServicesFacade<TValue> valueServices,
                                                ILogger logger)
        {
            mySession = session;
            myValueServices = valueServices;
            myLogger = logger;
        }

        public IEnumerable<IValueEntity> GetAdditionalLocals(IStackFrame frame)
        {
            // Do nothing if "Allow property evaluations..." option is disabled.
            // The debugger works in two steps - get value entities/references, and then get value presentation.
            // Evaluation is always allowed in the first step, but depends on user options for the second. This allows
            // evaluation to calculate children, e.g. expanding the Results node of IEnumerable, but presentation might
            // require clicking "refresh". We should be returning un-evaluated value references here.
            // TODO: Make "Active Scene" and "this.gameObject" lazy in 212
            if (!mySession.EvaluationOptions.AllowTargetInvoke) //!myUnityOptions.ExtensionsEnabled || 
                yield break;

            // If `this` is a MonoBehaviour, promote `this.gameObject` to top level to make it easier to find,
            // especially if inherited properties are hidden
            var thisGameObject = GetThisGameObjectForMonoBehaviour(frame);
            if (thisGameObject != null)
                yield return thisGameObject.ToValue(myValueServices);
        }

        [CanBeNull]
        private IValueReference<TValue> GetThisGameObjectForMonoBehaviour(IStackFrame frame)
        {
            return myLogger.CatchEvaluatorException<TValue, IValueReference<TValue>>(() =>
                {
                    var thisObj = frame.GetThis(mySession.EvaluationOptions);
                    if (thisObj?.DeclaredType?.FindTypeThroughHierarchy("Godot.Node") == null)
                        return null;

                    if (!(thisObj.GetPrimaryRole(mySession.EvaluationOptions) is IObjectValueRole<TValue> role))
                    {
                        myLogger.Warn("Unable to get 'this' as object value");
                        return null;
                    }

                    var gameObjectReference = role.GetInstanceMethodInvocationReference(new MethodSelector(m => m.Name == "GetTree" && m.Parameters.Length == 0));
                    if (gameObjectReference == null)
                    {
                        myLogger.Warn("Unable to find 'this.gameObject' as a property reference");
                        return null;
                    }

                    // var gameObject = gameObjectReference..GetValue(mySession.EvaluationOptions);
                    // var gameObjectType = gameObjectReference.GetValueType(mySession.EvaluationOptions,
                    //     myValueServices.ValueMetadataProvider);

                    // Don't show type for each child game object. It's always "GameObject", and we know they're game
                    // objects from the synthetic group.
                    // return new SimpleValueReference<TValue>(gameObject, gameObjectType, "this.gameObject",
                    //     ValueOriginKind.Property,
                    //     ValueFlags.None | ValueFlags.IsDefaultTypePresentation | ValueFlags.IsReadOnly, frame,
                    //     myValueServices.RoleFactory);
                    return null;
                },
                exception => myLogger.LogThrownUnityException(exception, frame, myValueServices, mySession.EvaluationOptions));
        }
    }
}