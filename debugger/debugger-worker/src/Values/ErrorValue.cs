using System;
using System.Collections.Generic;
using System.Threading;
using Mono.Debugging.Backend;
using Mono.Debugging.Backend.Values.ValueRoles;
using Mono.Debugging.Client.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using Mono.Debugging.MetadataLite.API;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    // An error message in place of a value. It implements IValue because debugger-worker transport only supports
    // values, groups, and type variables as tree entities; there is no role or declared type behind it.
    public class ErrorValue : IValue
    {
        /// <summary>
        /// Row name for an error that belongs to the whole child list rather than to one child. Deliberately not a
        /// plausible node name - it sits among real nodes on the inline scene-tree path.
        /// </summary>
        public const string RowName = "<error>";

        private readonly string myMessage;

        public ErrorValue(string name, string message)
        {
            myMessage = message;
            SimpleName = name;
        }

        public IValueKeyPresentation GetKeyPresentation(IPresentationOptions options,
                                                        CancellationToken token = new())
        {
            return new ValueKeyPresentation(SimpleName, ValueOriginKind.Other, ValueFlags.None, DeclaredType);
        }

        public IValuePresentation GetValuePresentation(IPresentationOptions options,
                                                       CancellationToken token = new())
        {
            return SimplePresentation.Create(PresentationBuilder.New().Error(myMessage), PresentationFlags.Error,
                ValueFlags.NoChildren, DeclaredType);
        }

        public IEnumerable<IValueEntity> GetChildren(IPresentationOptions options, CancellationToken token = new())
        {
            yield break;
        }

        public string SimpleName { get; }
        public IValueRole GetPrimaryRole(IValueFetchOptions options) => throw new NotSupportedException();
        public IMetadataTypeLite DeclaredType => null;
    }
}
