namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// Chooses between fetching a Godot.Node's children one by one and bulk-fetching them through compiled lambdas.
    /// </summary>
    public static class ChildFetchStrategy
    {
        /// <summary>
        /// A per-child round trip costs ~1ms and needs ~3 target invocations per child (~3s for 1000 children). Above
        /// this many children, bulk fetching pays for its two extra evaluations and the delegate allocations in the
        /// debuggee; below it, it does not.
        /// </summary>
        /// <remarks>
        /// The Roslyn compilation of the helper lambdas is cached, but only because we ask for it - the platform
        /// consults its cache solely when <c>AllowCachingCompilationResult</c> is set, and that option is off in the
        /// session's evaluation options. The cache key includes the frame's IL offset, so the saving is on repeated
        /// expansions at one stop rather than across steps.
        /// <para>
        /// Deliberately below <see cref="ChildChunking.ChunkSize"/>, so a fixture can exercise bulk fetching without
        /// also triggering chunking - see the child counts in <c>scene-tree-fixture.tscn</c>.
        /// </para>
        /// </remarks>
        public const int BulkFetchThreshold = 32;

        public static bool ShouldBulkFetch(int childCount) => childCount > BulkFetchThreshold;
    }
}
