using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Tests
{
    /// <summary>
    /// Every row in the scene tree is labelled through here, on both the bulk and the per-child path.
    /// </summary>
    [TestFixture]
    public class ChildRowNameTests
    {
        [Test]
        public void RealNameWins()
        {
            Assert.That(ChildRowName.For("Player", 3), Is.EqualTo("Player"));
        }

        [Test]
        public void NameIsNotTrimmed()
        {
            // Godot allows leading/trailing spaces in a node name; the tree must show what the node is actually called.
            Assert.That(ChildRowName.For(" Player ", 3), Is.EqualTo(" Player "));
        }

        [TestCase(null)]
        [TestCase("")]
        [TestCase(" ")]
        [TestCase("\t")]
        public void UnusableNameFallsBackToThePosition(string nodeName)
        {
            // A name we could not read, or one that would render as a blank row. The position is the honest answer -
            // a Godot node always has a name, so a missing one means the lookup failed, not that there is none.
            Assert.That(ChildRowName.For(nodeName, 7), Is.EqualTo("[7]"));
        }

        [Test]
        public void FallbackMatchesTheErrorRowsForTheSameChild()
        {
            // NodeChildrenEnumerator labels a child it could not retrieve "[index]" too, so a half-broken list stays
            // readable instead of mixing two positional formats.
            Assert.That(ChildRowName.For(null, 12), Is.EqualTo("[12]"));
        }
    }
}
