using System;
using System.Collections.Generic;
using System.Threading;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values.ValueReferences;
using JetBrains.Util;
using MetadataLite.API;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.Render.ChildrenRenderers;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Soft;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values.Renderer.ChildrenRenderers
{
    // Adds a "Children" group to Godot.Node.
    [DebuggerSessionComponent(typeof(SoftDebuggerType))]
    public class NodeObjectChildrenRenderer<TValue> : ChildrenRendererBase<TValue, IObjectValueRole<TValue>>
    where TValue : class
    {
        private readonly IOptions myOptions;
        private readonly ILogger myLogger;

        public NodeObjectChildrenRenderer(IOptions options, ILogger logger)
        {
            logger.Info("NodeObjectChildrenRenderer ctor.");
            myOptions = options;
            myLogger = logger;
        }

        // This is higher than ObjectChildrenRenderer
        public override int Priority => 50;

        // Let's just add a single item. Our priority means we'll be called first, so our children will be added to the
        // top. We also add a group which is pushed to top or bottom anyway.
        public override bool IsExclusive => false;

        protected override bool IsApplicable(IObjectValueRole<TValue> role, IMetadataTypeLite type,
                                            IPresentationOptions options, IUserDataHolder dataHolder)
        {
            myLogger.Info("NodeObjectChildrenRenderer.IsApplicable.");
            return myOptions.ExtensionsEnabled && type.FindTypeThroughHierarchy("Godot.Node") != null;
        }

        protected override IEnumerable<IValueEntity> GetChildren(IObjectValueRole<TValue> valueRole,
                                                                 IMetadataTypeLite instanceType,
                                                                 IPresentationOptions options,
                                                                 IUserDataHolder dataHolder,
                                                                 CancellationToken token)
        {
            return new[] {new ChildrenGroup(valueRole, ValueServices, myLogger)};
        }

        private class ChildrenGroup : ValueGroupBase
        {
            private readonly IObjectValueRole<TValue> myRole;
            private readonly IValueServicesFacade<TValue> myValueServices;
            private readonly ILogger myLogger;

            public ChildrenGroup(IObjectValueRole<TValue> role,
                                 IValueServicesFacade<TValue> valueServices,
                                 ILogger logger)
                : base("Children")
            {
                myRole = role;
                myValueServices = valueServices;
                myLogger = logger;
            }

            public override IEnumerable<IValueEntity> GetChildren(IPresentationOptions options,
                                                                  CancellationToken token = new CancellationToken())
            {
                return myLogger.CatchEvaluatorException<TValue, IEnumerable<IValueEntity>>(
                    () => GetChildrenImpl(options),
                    exception =>
                    {
                        myLogger.Error(exception);
                    }) ?? Array.Empty<IValueEntity>();
            }

            private IValueEntity[] GetChildrenImpl(IValueFetchOptions options)
            {
                if (!TryInvokeGetIterator(myRole, options, out var role))
                    return EmptyArray<IValueEntity>.Instance;

                var name = role.GetInstancePropertyReference("name")
                    ?.AsStringSafe(options)?.GetString() ?? "Child";

                // Tell the value presenter to hide the name field, as we're using it for the key name. Also hide the
                // type presentation - of course it's a SerializedProperty
                return new IValueEntity[]
                {
                    new CalculatedValueReferenceDecorator<TValue>(role.ValueReference,
                        myValueServices.RoleFactory, name, false, false).ToValue(myValueServices)
                };

                // Technically, we should now repeatedly call Copy() and Next(false) until Next returns false so that we
                // show all child properties of the SerializedObject. But empirically, there is only one direct child of
                // SerializedObject, called "Base", with a depth of -1. We'll avoid the unnecessary method invocations,
                // unless it turns out to be an actual issue.
            }

            private bool TryInvokeGetIterator(IObjectValueRole<TValue> role,
                                              IValueFetchOptions options,
                                              out IObjectValueRole<TValue> returnedPropertyRole)
            {
                returnedPropertyRole = null;

                var method = MetadataTypeLiteEx.LookupInstanceMethodSafe(role.ReifiedType.MetadataType,
                    MethodSelectors.NodeObject_GetChildren);
                if (method == null)
                {
                    myLogger.Warn("Cannot find GetChildren method on NodeObject");
                    return false;
                }

                returnedPropertyRole = new SimpleValueReference<TValue>(
                        role.CallInstanceMethod(method),
                        role.ValueReference.OriginatingFrame, myValueServices.RoleFactory)
                    .AsObjectSafe(options);
                if (returnedPropertyRole == null)
                {
                    myLogger.Warn("Unable to invoke GetChildren");
                    return false;
                }

                return true;
            }
        }
    }
}