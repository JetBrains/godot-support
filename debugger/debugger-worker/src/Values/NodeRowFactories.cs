using System.Collections.Generic;
using JetBrains.Annotations;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values.ValueReferences;
using Mono.Debugging.Backend.Values;
using Mono.Debugging.Backend.Values.ValueReferences;
using Mono.Debugging.Client.Values;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// Builds the rows for one presentation of a Godot.Node's children. The two implementations are what separates
    /// the inline scene tree from the "Children" category; everything upstream of them is shared.
    /// </summary>
    internal interface INodeRowFactory<TValue>
        where TValue : class
    {
        IValueEntity CreateChild([NotNull] IValueReference<TValue> childReference, [CanBeNull] string nodeName,
            int index);

        IValueEntity CreateChunkGroup([NotNull] string label, [NotNull] IEnumerable<IValueEntity> children);
    }

    internal abstract class NodeRowFactoryBase<TValue> : INodeRowFactory<TValue>
        where TValue : class
    {
        protected NodeRowFactoryBase([NotNull] IValueServicesFacade<TValue> valueServices) =>
            ValueServices = valueServices;

        [NotNull]
        protected IValueServicesFacade<TValue> ValueServices { get; }

        public IValueEntity CreateChild(IValueReference<TValue> childReference, string nodeName, int index)
        {
            // The name is ours, so tell the value presenter to hide the value's own name and type presentation.
            return CreateChild(new CalculatedValueReferenceDecorator<TValue>(
                childReference, ValueServices.RoleFactory, ChildRowName.For(nodeName, index),
                allowNameInValue: false, allowDefaultTypePresentation: false));
        }

        public abstract IValueEntity CreateChunkGroup(string label, IEnumerable<IValueEntity> children);

        protected abstract IValueEntity CreateChild([NotNull] IValueReference<TValue> decoratedReference);
    }

    /// <summary>
    /// Rows of the remote scene tree: a child is a scene-tree node itself and presents its own children inline, so
    /// chunk groups have to sort above the trailing "Properties" group.
    /// </summary>
    internal sealed class SceneTreeRowFactory<TValue> : NodeRowFactoryBase<TValue>
        where TValue : class
    {
        private readonly NodeChildrenEnumerator<TValue> myChildrenEnumerator;

        public SceneTreeRowFactory([NotNull] IValueServicesFacade<TValue> valueServices,
            [NotNull] NodeChildrenEnumerator<TValue> childrenEnumerator)
            : base(valueServices) =>
            myChildrenEnumerator = childrenEnumerator;

        protected override IValueEntity CreateChild(IValueReference<TValue> decoratedReference) =>
            myChildrenEnumerator.CreateSceneTreeNode(decoratedReference);

        public override IValueEntity CreateChunkGroup(string label, IEnumerable<IValueEntity> children) =>
            new SimpleEntityGroup(label, children, isTop: true);
    }

    /// <summary>
    /// Rows of the "Children" category on a node reached through code: plain node values that render normally.
    /// </summary>
    internal sealed class PlainNodeRowFactory<TValue> : NodeRowFactoryBase<TValue>
        where TValue : class
    {
        public PlainNodeRowFactory([NotNull] IValueServicesFacade<TValue> valueServices) : base(valueServices)
        {
        }

        protected override IValueEntity CreateChild(IValueReference<TValue> decoratedReference) =>
            decoratedReference.ToValue(ValueServices);

        public override IValueEntity CreateChunkGroup(string label, IEnumerable<IValueEntity> children) =>
            new SimpleEntityGroup(label, children);
    }
}
