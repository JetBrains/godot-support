using System.Collections.Generic;
using System.Linq;
using System.Threading;
using JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values;
using NUnit.Framework;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Tests
{
    /// <summary>
    /// Index arithmetic for the <c>[a..b]</c> chunk groups, plus the laziness that makes a large scene tree
    /// affordable: building a chunk must not build the children inside it.
    /// </summary>
    [TestFixture]
    public class ChildChunkingTests
    {
        private const int ChunkSize = ChildChunking.ChunkSize;

        private abstract class Row
        {
        }

        private sealed class Leaf : Row
        {
            public Leaf(int index) => Index = index;
            public int Index { get; }
            public override string ToString() => $"leaf({Index})";
        }

        private sealed class Group : Row
        {
            public Group(string label, IEnumerable<Row> children)
            {
                Label = label;
                Children = children;
            }

            public string Label { get; }
            public IEnumerable<Row> Children { get; }
            public override string ToString() => Label;
        }

        /// <summary>
        /// Counts how many children were actually built, so laziness can be asserted.
        /// </summary>
        private sealed class CountingFactory
        {
            public int CreatedChildren { get; private set; }
            public int CreatedGroups { get; private set; }

            public IEnumerable<Row> Split(int startIndex, int length, CancellationToken token = default)
            {
                return ChildChunking.SplitIntoChunks<Row>(startIndex, length,
                    index =>
                    {
                        CreatedChildren++;
                        return new Leaf(index);
                    },
                    (label, children) =>
                    {
                        CreatedGroups++;
                        return new Group(label, children);
                    },
                    token);
            }
        }

        private static List<Row> Split(int startIndex, int length, CancellationToken token = default)
        {
            return new CountingFactory().Split(startIndex, length, token).ToList();
        }

        private static Group AsGroup(Row row)
        {
            Assert.That(row, Is.InstanceOf<Group>(), $"Expected a group but got {row}");
            return (Group)row;
        }

        /// <summary>Walks the whole tree and returns the leaf indices in presentation order.</summary>
        private static List<int> FlattenLeafIndices(IEnumerable<Row> rows)
        {
            var result = new List<int>();
            foreach (var row in rows)
            {
                if (row is Leaf leaf)
                    result.Add(leaf.Index);
                else
                    result.AddRange(FlattenLeafIndices(AsGroup(row).Children));
            }

            return result;
        }

        [TestCase(0)]
        [TestCase(1)]
        [TestCase(2)]
        [TestCase(ChunkSize - 1)]
        [TestCase(ChunkSize)]
        public void ShortRunIsNotChunked(int length)
        {
            var rows = Split(0, length);
            Assert.That(rows, Has.Count.EqualTo(length));
            Assert.That(rows, Is.All.InstanceOf<Leaf>());
            Assert.That(rows.Cast<Leaf>().Select(l => l.Index), Is.EqualTo(Enumerable.Range(0, length)));
        }

        // ---------------------------------------------------------------------------------------------------
        // One level of chunking
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void JustOverChunkSizeSplitsIntoTwoGroups()
        {
            var rows = Split(0, ChunkSize + 1);
            Assert.That(rows.Select(r => AsGroup(r).Label), Is.EqualTo(new[] { "[0..99]", "[100..100]" }));
            Assert.That(FlattenLeafIndices(rows), Is.EqualTo(Enumerable.Range(0, ChunkSize + 1)));
        }

        [Test]
        public void ExactMultipleOfChunkSizeHasNoRemainderGroup()
        {
            var rows = Split(0, 2 * ChunkSize);
            Assert.That(rows.Select(r => AsGroup(r).Label), Is.EqualTo(new[] { "[0..99]", "[100..199]" }));
        }

        [Test]
        public void MaximumFlatGroupCountDoesNotNest()
        {
            // ChunkSize groups of ChunkSize children is the largest run that still fits in one level.
            var rows = Split(0, ChunkSize * ChunkSize);
            Assert.That(rows, Has.Count.EqualTo(ChunkSize));
            Assert.That(AsGroup(rows[0]).Label, Is.EqualTo("[0..99]"));
            Assert.That(AsGroup(rows[ChunkSize - 1]).Label, Is.EqualTo("[9900..9999]"));
            Assert.That(AsGroup(rows[0]).Children, Is.All.InstanceOf<Leaf>(),
                "A group at the only level must hold children directly");
        }

        // ---------------------------------------------------------------------------------------------------
        // Chunks of chunks
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void OneChildOverTheFlatLimitStartsNesting()
        {
            var rows = Split(0, ChunkSize * ChunkSize + 1);
            Assert.That(rows.Select(r => AsGroup(r).Label), Is.EqualTo(new[] { "[0..9999]", "[10000..10000]" }));

            var subGroups = AsGroup(rows[0]).Children.ToList();
            Assert.That(subGroups, Has.Count.EqualTo(ChunkSize));
            Assert.That(AsGroup(subGroups[0]).Label, Is.EqualTo("[0..99]"));
            Assert.That(AsGroup(subGroups[0]).Children, Is.All.InstanceOf<Leaf>());

            // The one-element remainder skips a level - it is short enough to hold leaves directly.
            Assert.That(AsGroup(rows[1]).Children, Is.All.InstanceOf<Leaf>());
        }

        [Test]
        public void ThreeLevelsOfNesting()
        {
            var rows = Split(0, ChunkSize * ChunkSize * ChunkSize + 1);
            Assert.That(rows.Select(r => AsGroup(r).Label),
                Is.EqualTo(new[] { "[0..999999]", "[1000000..1000000]" }));

            // Walk only the first branch - materialising a million leaves is not the point of this test.
            var level2 = AsGroup(rows[0]).Children.ToList();
            Assert.That(level2, Has.Count.EqualTo(ChunkSize));
            Assert.That(AsGroup(level2[0]).Label, Is.EqualTo("[0..9999]"));

            var level3 = AsGroup(level2[0]).Children.ToList();
            Assert.That(level3, Has.Count.EqualTo(ChunkSize));
            Assert.That(AsGroup(level3[0]).Label, Is.EqualTo("[0..99]"));
            Assert.That(AsGroup(level3[0]).Children, Is.All.InstanceOf<Leaf>());
        }

        // ---------------------------------------------------------------------------------------------------
        // Every child is present, exactly once, in order
        // ---------------------------------------------------------------------------------------------------

        [TestCase(ChunkSize + 1)]
        [TestCase(ChunkSize + 37)]
        [TestCase(2 * ChunkSize)]
        [TestCase(999)]
        [TestCase(ChunkSize * ChunkSize)]
        [TestCase(ChunkSize * ChunkSize + 1)]
        [TestCase(ChunkSize * ChunkSize + ChunkSize)]
        [TestCase(31337)]
        public void EveryChildAppearsExactlyOnceInOrder(int length)
        {
            Assert.That(FlattenLeafIndices(Split(0, length)), Is.EqualTo(Enumerable.Range(0, length)));
        }

        [TestCase(0, 250)]
        [TestCase(7, 250)]
        [TestCase(1000, 10001)]
        public void ChunkingRespectsStartIndex(int startIndex, int length)
        {
            var rows = Split(startIndex, length);
            Assert.That(AsGroup(rows[0]).Label, Does.StartWith($"[{startIndex}.."));
            Assert.That(FlattenLeafIndices(rows), Is.EqualTo(Enumerable.Range(startIndex, length)));
        }

        [Test]
        public void GroupLabelsAreContiguousAndNonOverlapping()
        {
            var rows = Split(0, 10001);
            var expectedNext = 0;
            foreach (var group in rows.Select(AsGroup))
            {
                var bounds = group.Label.Trim('[', ']').Split(new[] { ".." }, System.StringSplitOptions.None);
                Assert.That(int.Parse(bounds[0]), Is.EqualTo(expectedNext), $"Gap or overlap before {group.Label}");
                expectedNext = int.Parse(bounds[1]) + 1;
            }

            Assert.That(expectedNext, Is.EqualTo(10001), "Chunk labels must cover the whole range");
        }

        // ---------------------------------------------------------------------------------------------------
        // Laziness
        // ---------------------------------------------------------------------------------------------------

        [Test]
        public void EnumeratingTopLevelDoesNotBuildAnyChild()
        {
            var factory = new CountingFactory();

            var rows = factory.Split(0, ChunkSize * ChunkSize + 1).ToList();

            Assert.That(rows, Has.Count.EqualTo(2), "Precondition: the run is chunked");
            Assert.That(factory.CreatedChildren, Is.Zero,
                "Listing the top-level chunks must not evaluate a single child - an unexpanded chunk has to be free");
        }

        [Test]
        public void ExpandingOneChunkBuildsOnlyThatChunksChildren()
        {
            var factory = new CountingFactory();
            var rows = factory.Split(0, 2 * ChunkSize).ToList();

            var firstChunkChildren = AsGroup(rows[0]).Children.ToList();

            Assert.That(firstChunkChildren, Has.Count.EqualTo(ChunkSize));
            Assert.That(factory.CreatedChildren, Is.EqualTo(ChunkSize),
                "Expanding one chunk must not build the children of its siblings");
        }

        [Test]
        public void ExpandingNestedChunkDoesNotBuildLeaves()
        {
            var factory = new CountingFactory();
            var rows = factory.Split(0, ChunkSize * ChunkSize + 1).ToList();

            // Descend one level - still only groups, so still no children.
            AsGroup(rows[0]).Children.ToList();

            Assert.That(factory.CreatedChildren, Is.Zero,
                "Descending into a chunk of chunks must not reach any leaf");
        }

        [Test]
        public void NothingIsBuiltUntilTheEnumerableIsConsumed()
        {
            var factory = new CountingFactory();

            factory.Split(0, 500);

            Assert.That(factory.CreatedChildren, Is.Zero);
            Assert.That(factory.CreatedGroups, Is.Zero);
        }

        // ---------------------------------------------------------------------------------------------------
        // Cancellation
        // ---------------------------------------------------------------------------------------------------

        [TestCase(5)]
        [TestCase(ChunkSize + 1)]
        [TestCase(ChunkSize * ChunkSize + 1)]
        public void AlreadyCancelledTokenStopsEnumeration(int length)
        {
            using (var cts = new CancellationTokenSource())
            {
                cts.Cancel();

                // Deliberately not materialised here - the enumerable must be lazy enough that constructing it is
                // free and the cancellation only surfaces once someone enumerates.
                var enumerable = new CountingFactory().Split(0, length, cts.Token);

                Assert.That(() => enumerable.ToList(), Throws.InstanceOf<System.OperationCanceledException>());
            }
        }

        [Test]
        public void CancellationDuringEnumerationStopsEnumeration()
        {
            using (var cts = new CancellationTokenSource())
            {
                var factory = new CountingFactory();
                var produced = 0;

                Assert.That(() =>
                {
                    foreach (var unused in factory.Split(0, 5 * ChunkSize, cts.Token))
                    {
                        produced++;
                        cts.Cancel();
                    }
                }, Throws.InstanceOf<System.OperationCanceledException>());

                Assert.That(produced, Is.EqualTo(1), "Enumeration must stop at the first check after cancellation");
            }
        }

        [Test]
        public void CancellationIsCheckedInsideChunks()
        {
            using (var cts = new CancellationTokenSource())
            {
                var factory = new CountingFactory();
                var rows = factory.Split(0, 2 * ChunkSize, cts.Token).ToList();
                cts.Cancel();

                Assert.That(() => AsGroup(rows[0]).Children.ToList(),
                    Throws.InstanceOf<System.OperationCanceledException>(),
                    "A chunk expanded after cancellation must not keep evaluating children in the debuggee");
            }
        }
    }
}
