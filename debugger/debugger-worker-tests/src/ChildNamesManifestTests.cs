using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Tests
{
    /// <summary>
    /// The manifest is a hand-rolled wire format read from a string produced inside the debuggee, so it is parsed from
    /// input we do not fully control. A malformed manifest must always degrade to "return false and let the caller
    /// fetch children one by one" - never an exception, and never a silently wrong set of names.
    /// </summary>
    [TestFixture]
    public class ChildNamesManifestTests
    {
        private static string[] Parse(string manifest, int expectedCount)
        {
            Assert.That(ChildNamesManifest.TryParse(manifest, expectedCount, out var names), Is.True,
                $"Expected to parse {Quote(manifest)} as {expectedCount} name(s)");
            return names;
        }

        private static void AssertRejected(string manifest, int expectedCount)
        {
            Assert.That(ChildNamesManifest.TryParse(manifest, expectedCount, out var names), Is.False,
                $"Expected to reject {Quote(manifest)} as {expectedCount} name(s)");

            // The caller uses a null names array as the signal to fall back, so a rejection must not leak a
            // partially filled array.
            Assert.That(names, Is.Null, "A rejected manifest must not produce names");
        }

        private static string Quote(string value) => value == null ? "<null>" : $"\"{value}\"";

        // ---------------------------------------------------------------------------------------------------
        // Well-formed input
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void SingleName()
        {
            Assert.That(Parse("4:Node", 1), Is.EqualTo(new[] { "Node" }));
        }

        [Test]
        public void SeveralNames()
        {
            Assert.That(Parse("4:Node3:Foo6:Player", 3), Is.EqualTo(new[] { "Node", "Foo", "Player" }));
        }

        [Test]
        public void NoChildren()
        {
            Assert.That(Parse(string.Empty, 0), Is.Empty);
        }

        [Test]
        public void EmptyName()
        {
            Assert.That(Parse("0:", 1), Is.EqualTo(new[] { string.Empty }));
        }

        [Test]
        public void DuplicateNamesArePreserved()
        {
            // Godot allows sibling names to be made unique, but we must not deduplicate or reorder.
            Assert.That(Parse("4:Node4:Node", 2), Is.EqualTo(new[] { "Node", "Node" }));
        }

        [Test]
        public void UnreachableNameBecomesNull()
        {
            Assert.That(Parse("-1:4:Node", 2), Is.EqualTo(new[] { null, "Node" }));
        }

        [Test]
        public void AllNamesUnreachable()
        {
            Assert.That(Parse("-1:-1:", 2), Is.EqualTo(new string[] { null, null }));
        }

        // ---------------------------------------------------------------------------------------------------
        // Payloads that would break a delimiter-scanning parser
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void NameContainingDigitsAndColon()
        {
            Assert.That(Parse("5:12:34", 1), Is.EqualTo(new[] { "12:34" }));
        }

        [Test]
        public void NameThatLooksLikeTheNullMarker()
        {
            // "-1:" as an actual node name, length-prefixed, must not be read as "no name".
            Assert.That(Parse("3:-1:", 1), Is.EqualTo(new[] { "-1:" }));
        }

        [Test]
        public void NameStartingWithMinus()
        {
            Assert.That(Parse("4:-abc", 1), Is.EqualTo(new[] { "-abc" }));
        }

        [Test]
        public void NameConsistingOnlyOfDigits()
        {
            Assert.That(Parse("3:123", 1), Is.EqualTo(new[] { "123" }));
        }

        [Test]
        public void NameContainingNewline()
        {
            Assert.That(Parse("3:a\nb", 1), Is.EqualTo(new[] { "a\nb" }));
        }

        [Test]
        public void NameContainingSurrogatePair()
        {
            // The debuggee writes text.Length, which counts UTF-16 units, so an emoji costs 2. If the reader ever
            // starts counting codepoints instead, every name after the first emoji shifts.
            const string emoji = "\U0001F642"; // U+1F642, two UTF-16 units
            Assert.That(emoji.Length, Is.EqualTo(2), "Precondition: the test emoji is a surrogate pair");
            Assert.That(Parse("2:" + emoji + "4:Node", 2), Is.EqualTo(new[] { emoji, "Node" }));
        }

        [Test]
        public void NameContainingNonAsciiLetters()
        {
            Assert.That(Parse("6:Кнопка", 1), Is.EqualTo(new[] { "Кнопка" }));
        }

        // ---------------------------------------------------------------------------------------------------
        // Malformed input - must be rejected, never throw
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void NullManifestIsRejected()
        {
            AssertRejected(null, 1);
        }

        [Test]
        public void NegativeExpectedCountIsRejected()
        {
            AssertRejected(string.Empty, -1);
        }

        [Test]
        public void TrailingGarbageIsRejected()
        {
            // We disagree with the debuggee about how many children there are - don't guess which side is right.
            AssertRejected("4:Nodexx", 1);
        }

        [Test]
        public void MoreEntriesThanExpectedIsRejected()
        {
            AssertRejected("4:Node3:Foo", 1);
        }

        [Test]
        public void FewerEntriesThanExpectedIsRejected()
        {
            AssertRejected("4:Node", 2);
        }

        [Test]
        public void TruncatedPayloadIsRejected()
        {
            AssertRejected("9:Node", 1);
        }

        [Test]
        public void EmptyManifestWithChildrenExpectedIsRejected()
        {
            AssertRejected(string.Empty, 1);
        }

        [Test]
        public void MissingSeparatorIsRejected()
        {
            AssertRejected("4Node", 1);
        }

        [Test]
        public void MissingLengthIsRejected()
        {
            AssertRejected(":Node", 1);
        }

        [Test]
        public void LengthWithoutPayloadIsRejected()
        {
            AssertRejected("4", 1);
        }

        [TestCase("-")]
        [TestCase("-1")]
        [TestCase("-2:")]
        [TestCase("-11:")]
        [TestCase("- 1:")]
        public void MalformedNullMarkerIsRejected(string manifest)
        {
            AssertRejected(manifest, 1);
        }

        [Test]
        public void LengthOverflowIsRejected()
        {
            // Must be rejected by the overflow guard rather than wrapping to a small positive length (which would
            // silently return a truncated name) or throwing.
            AssertRejected("99999999999:x", 1);
        }

        [Test]
        public void LengthOfIntMaxValueIsRejected()
        {
            AssertRejected("2147483647:x", 1);
        }

        [Test]
        public void NonNumericLengthIsRejected()
        {
            AssertRejected("x:Node", 1);
        }

        [Test]
        public void PlusSignedLengthIsRejected()
        {
            AssertRejected("+4:Node", 1);
        }

        // ---------------------------------------------------------------------------------------------------
        // Writer/reader agreement
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void LambdaWritesTheNullMarkerTheParserExpects()
        {
            // The writer lives in a string that no compiler here ever sees, so the two halves of the format can only
            // be kept in step by hand. If they drift, every manifest containing an unreachable name is rejected and
            // the fast path silently disappears for every node that has one.
            Assert.That(DebuggeeLambdas.ChildNames, Does.Contain($"(\"{ChildNamesManifest.NullNameMarker}\")"),
                "DebuggeeLambdas.ChildNames must append ChildNamesManifest.NullNameMarker verbatim");
        }
    }
}
