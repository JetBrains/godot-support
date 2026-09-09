using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Tests
{
    /// <summary>
    /// Which of the two child-retrieval paths runs.
    /// </summary>
    /// <remarks>
    /// Invisible to a gold test - both paths render the same tree - so the boundary is pinned here instead.
    /// </remarks>
    [TestFixture]
    public class ChildFetchStrategyTests
    {
        [TestCase(0, false)]
        [TestCase(1, false)]
        [TestCase(ChildFetchStrategy.BulkFetchThreshold - 1, false)]
        [TestCase(ChildFetchStrategy.BulkFetchThreshold, false)]
        [TestCase(ChildFetchStrategy.BulkFetchThreshold + 1, true)]
        [TestCase(10_000, true)]
        public void BulkFetchStartsOneChildAboveTheThreshold(int childCount, bool expected)
        {
            Assert.That(ChildFetchStrategy.ShouldBulkFetch(childCount), Is.EqualTo(expected));
        }

        [Test]
        public void BulkFetchEngagesBeforeChunkingDoes()
        {
            // Keeps a fixture able to exercise bulk fetching without also triggering chunking - which is exactly what
            // `SceneTreeFixture/Wide` (33 children) does in DebugGodotCSharpPlayer.
            Assert.That(ChildFetchStrategy.BulkFetchThreshold, Is.LessThan(ChildChunking.ChunkSize));
        }
    }
}
