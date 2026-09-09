using System;
using System.Linq;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using Mono.Debugging.Client.Values.Render;
using Mono.Debugging.Evaluation;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Tests
{
    /// <summary>
    /// The row shown in place of a child node we could not retrieve.
    /// </summary>
    /// <remarks>
    /// ErrorValue implements IValue because debugger-worker transport only carries values, groups and type variables as
    /// tree entities - but there is no role or declared type behind it, so GetPrimaryRole cannot be answered. If
    /// anything on the presentation path ever asks for the role, the whole Children group dies instead of one row.
    /// </remarks>
    [TestFixture]
    public class ErrorValueTests
    {
        private const string Name = "[7]";
        private const string Message = "Unable to retrieve child node";

        private static ErrorValue CreateErrorValue() => new ErrorValue(Name, Message);

        [Test]
        public void NameIsTheRowName()
        {
            Assert.That(CreateErrorValue().SimpleName, Is.EqualTo(Name));
        }

        [Test]
        public void KeyPresentationUsesTheRowName()
        {
            // Passing null options on purpose: presentation must not depend on evaluation options, because an error
            // row has to render even when evaluation is switched off.
            var presentation = CreateErrorValue().GetKeyPresentation(null);

            Assert.That(presentation.KeyName, Is.EqualTo(Name));
            Assert.That(presentation.DeclaredType, Is.Null);
        }

        [Test]
        public void ValuePresentationCarriesTheMessage()
        {
            var presentation = CreateErrorValue().GetValuePresentation(null);

            Assert.That(presentation.DisplayValue, Does.Contain(Message));
        }

        [Test]
        public void ValuePresentationIsMarkedAsAnError()
        {
            var presentation = CreateErrorValue().GetValuePresentation(null);

            Assert.That(presentation.PresentationFlags, Is.EqualTo(PresentationFlags.Error),
                "The row has to render as an error, not as a string value");
        }

        [Test]
        public void ValuePresentationDeclaresNoChildren()
        {
            var presentation = CreateErrorValue().GetValuePresentation(null);

            Assert.That(presentation.Flags.HasFlag(ValueFlags.NoChildren), Is.True,
                "Without NoChildren the frontend offers an expander that can never produce anything");
        }

        [Test]
        public void HasNoChildren()
        {
            Assert.That(CreateErrorValue().GetChildren(null).ToList(), Is.Empty);
        }

        [Test]
        public void HasNoDeclaredType()
        {
            Assert.That(CreateErrorValue().DeclaredType, Is.Null);
        }

        [Test]
        public void GetPrimaryRoleIsNotSupported()
        {
            // Documents the hazard rather than the feature: there is no value behind an error row, so the plugin's
            // own tree path must never ask for one.
            Assert.That(() => CreateErrorValue().GetPrimaryRole(null), Throws.InstanceOf<NotSupportedException>());
        }
    }
}
