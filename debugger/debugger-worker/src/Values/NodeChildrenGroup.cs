using System.Collections.Generic;
using System.Threading;
using JetBrains.Util;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// Value group that renders the children of a Godot.Node as a "Children" category.
    /// Used for the this-path (non-scene-tree) presentation.
    /// </summary>
    /// <remarks>
    /// A group rather than a <c>SimpleEntityGroup</c> over a lazy enumerable, because a group is handed the
    /// presentation options and cancellation token that belong to the expansion, instead of capturing the ones the
    /// renderer was called with.
    /// </remarks>
    internal class NodeChildrenGroup<TValue> : ValueGroupBase
        where TValue : class
    {
        private readonly IObjectValueRole<TValue> myNodeRole;
        private readonly NodeChildrenEnumerator<TValue> myChildrenEnumerator;

        public NodeChildrenGroup(IObjectValueRole<TValue> nodeRole, NodeChildrenEnumerator<TValue> childrenEnumerator)
            : base(GroupNames.Children)
        {
            myNodeRole = nodeRole;
            myChildrenEnumerator = childrenEnumerator;
        }

        public override IEnumerable<IValueEntity> GetChildren(IPresentationOptions options,
                                                             CancellationToken token = new CancellationToken())
        {
            return myChildrenEnumerator.EnumerateChildrenSafe(myNodeRole, options, token,
                       myChildrenEnumerator.PlainRows)
                   ?? EmptyList<IValueEntity>.ReadOnly;
        }
    }
}
