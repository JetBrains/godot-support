using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values.ValueReferences;
using JetBrains.Util;
using Mono.Debugging.Autofac;
using Mono.Debugging.Backend.Values.Render.ChildrenRenderers;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.MetadataLite.API;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values.Renderer.ChildrenRenderers
{
    // Adds a "Children" group to Godot.Node.
    [DebuggerSessionComponent]
    public class NodeObjectChildrenRenderer<TValue> : ChildrenRendererBase<TValue, IObjectValueRole<TValue>>
        where TValue : class
    {
        private readonly IOptions myOptions;
        private readonly ILogger myLogger;

        public NodeObjectChildrenRenderer(IOptions options, ILogger logger)
        {
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
            return myOptions.ExtensionsEnabled && type.FindTypeThroughHierarchy("Godot.Node") != null;
        }
        
        protected override IEnumerable<IValueEntity> GetChildren(IObjectValueRole<TValue> valueRole,
            IMetadataTypeLite instanceType,
            IPresentationOptions options,
            IUserDataHolder dataHolder,
            CancellationToken token)
        {
            return myLogger.CatchEvaluatorException<TValue, IEnumerable<IValueEntity>>(
                () => GetChildrenImpl(valueRole, options),
                exception => { myLogger.Error(exception); }) ?? Array.Empty<IValueEntity>();
        }

        private IValueEntity[] GetChildrenImpl(IObjectValueRole<TValue> valueRole, IValueFetchOptions options)
        {
            if (!TryInvokeGetChildren(valueRole, options, out var role))
                return EmptyArray<IValueEntity>.Instance;

            var name = role.GetInstancePropertyReference("Name")
                ?.AsStringSafe(options)?.GetString() ?? "Children";

            // Tell the value presenter to hide the name field, as we're using it for the key name. Also hide the type presentation
            return new IValueEntity[]
            {
                new CalculatedValueReferenceDecorator<TValue>(role.ValueReference,
                    ValueServices.RoleFactory, name, false, false).ToValue(ValueServices)
            };
        }

        private bool TryInvokeGetChildren(IObjectValueRole<TValue> role,
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

            if (method.Parameters.Any())
            {
                var frame = role.ValueReference.OriginatingFrame;
                var param = ValueServices.ValueFactory.CreatePrimitive(frame, options, method.Parameters.First().DefaultValue);
                returnedPropertyRole = new SimpleValueReference<TValue>(
                        role.CallInstanceMethod(options, method, param),
                        role.ValueReference.OriginatingFrame, ValueServices.RoleFactory)
                    .AsObjectSafe(options);
            }
            else
            {
                returnedPropertyRole = new SimpleValueReference<TValue>(
                        role.CallInstanceMethod(options, method),
                        role.ValueReference.OriginatingFrame, ValueServices.RoleFactory)
                    .AsObjectSafe(options);    
            }
            
            if (returnedPropertyRole == null)
            {
                myLogger.Warn("Unable to invoke GetChildren");
                return false;
            }

            return true;
        }
    }
}