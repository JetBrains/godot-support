using System;
using System.Collections.Generic;
using System.Threading;
using JetBrains.Annotations;

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// Groups a long run of children into (possibly nested) <c>[a..b]</c> chunks, so the debugger tree never shows
    /// thousands of rows at once.
    /// </summary>
    public static class ChildChunking
    {
        /// <summary>
        /// Largest number of rows shown at one level. Also, the branching factor of the nested chunks.
        /// </summary>
        public const int ChunkSize = 100;

        /// <summary>
        /// Yields <paramref name="length"/> children built by <paramref name="createChild"/>, grouping them into
        /// (possibly nested) chunks of <see cref="ChunkSize"/> when there are too many to show at once.
        /// </summary>
        /// <remarks>
        /// Lazy: <paramref name="createChild"/> is not called until the returned enumerable is consumed down to the
        /// leaf that needs it, so an unexpanded chunk costs nothing.
        /// </remarks>
        /// <param name="startIndex">Index of the first child, used for both the factory and the chunk labels.</param>
        /// <param name="length">Number of children.</param>
        /// <param name="createChild">Builds the row for a single child index.</param>
        /// <param name="createGroup">Builds a collapsed group row from its label and its (lazy) contents.</param>
        public static IEnumerable<T> SplitIntoChunks<T>(
            int startIndex,
            int length,
            [NotNull] Func<int, T> createChild,
            [NotNull] Func<string, IEnumerable<T>, T> createGroup,
            CancellationToken token)
        {
            if (length <= ChunkSize)
            {
                for (var i = startIndex; i < startIndex + length; i++)
                {
                    token.ThrowIfCancellationRequested();
                    yield return createChild(i);
                }

                yield break;
            }

            // Chunks of chunks - grow the step until the top level holds at most ChunkSize groups
            var step = ChunkSize;
            var div = length / step;
            while (div > ChunkSize || div == ChunkSize && length % step > 0)
            {
                token.ThrowIfCancellationRequested();
                step *= ChunkSize;
                div = length / step;
            }

            for (long i = 0; i < length; i += step)
            {
                token.ThrowIfCancellationRequested();

                var chunkStartIndex = (int)(startIndex + i);
                var chunkEndExclusive = Math.Min((long)chunkStartIndex + step, (long)startIndex + length);
                var chunkEndIndex = (int)(chunkEndExclusive - 1);
                var chunkLength = (int)(chunkEndExclusive - chunkStartIndex);
                yield return createGroup($"[{chunkStartIndex}..{chunkEndIndex}]",
                    SplitIntoChunks(chunkStartIndex, chunkLength, createChild, createGroup, token));
            }
        }
    }
}
