using System.Collections.Generic;
using System.Threading;
using JetBrains.Annotations;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    // Groups are special entities that are not fields or properties. They are used for "Raw View", "Non-public members"
    // and "Results". They are not sorted with other entities but pushed to the top or bottom of the view, in the order
    // added. We use them for the "Children" category of a Godot.Node.
    // "base" is an instance of ConcreteObjectRoleReference, so could theoretically be sorted separately.
    // Unlike SimpleEntityGroup, a group is handed the presentation options and cancellation token of the expansion,
    // rather than capturing the ones its renderer was called with.
    public abstract class ValueGroupBase : IValueGroup
    {
        protected ValueGroupBase(string simpleName)
        {
            SimpleName = simpleName;
        }

        public string SimpleName { get; }
        public virtual bool IsTop => true;

        [NotNull]
        public virtual IValueKeyPresentation GetKeyPresentation(IPresentationOptions options,
                                                                CancellationToken token = new CancellationToken())
        {
            return new ValueKeyPresentation(SimpleName, ValueOriginKind.Group, ValueFlags.None);
        }

        [NotNull]
        public virtual IValuePresentation GetValuePresentation(IPresentationOptions options,
                                                               CancellationToken token = new CancellationToken())
        {
            return SimplePresentation.EmptyPresentation;
        }

        [NotNull]
        public abstract IEnumerable<IValueEntity> GetChildren(IPresentationOptions options,
                                                              CancellationToken token = new CancellationToken());
    }
}