namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// The wire format used to carry the names of a Godot.Node's children out of the debuggee in one string.
    /// Written by <see cref="DebuggeeLambdas.ChildNames"/>, read by <see cref="TryParse"/>.
    /// </summary>
    /// <remarks>
    /// A manifest is the concatenation of one entry per child, in child order:
    /// <code>
    ///   "&lt;length&gt;:&lt;text&gt;"  a name, where &lt;length&gt; is the UTF-16 unit count of &lt;text&gt;
    ///   "-1:"                          a child whose name could not be reached
    /// </code>
    /// The explicit length is the point of the format: a node name may contain digits, colons or newlines, and the
    /// reader never has to scan the payload for a delimiter. <c>"5:12:34"</c> is the single name <c>12:34</c>, and
    /// <c>"3:-1:"</c> is the single name <c>-1:</c>, not a null marker.
    ///
    /// A node name can't contain `:`, see <see href="https://docs.godotengine.org/en/stable/classes/class_node.html#class-node-property-name">Godot Node docs</see>
    /// </remarks>
    public static class ChildNamesManifest
    {
        /// <summary>
        /// Marker written in place of "&lt;length&gt;:" for a child whose name could not be reached.
        /// Unambiguous because a length never starts with '-'.
        /// </summary>
        public const string NullNameMarker = "-1:";

        /// <summary>
        /// Parses a manifest produced by <see cref="DebuggeeLambdas.ChildNames"/>.
        /// </summary>
        /// <param name="manifest">The manifest string, as read from the debuggee.</param>
        /// <param name="expectedCount">
        /// How many entries the manifest must contain - the length of the child array fetched alongside it.
        /// </param>
        /// <param name="names">
        /// On success, exactly <paramref name="expectedCount"/> names, with <c>null</c> for an unreachable name.
        /// On failure, <c>null</c> - callers rely on that to fall back to per-child retrieval.
        /// </param>
        /// <returns>
        /// False if the manifest is malformed, truncated, longer than <paramref name="expectedCount"/> entries, or
        /// does not account for every character. Never throws: a broken manifest is a fallback, not an error.
        /// </returns>
        public static bool TryParse(string manifest, int expectedCount, out string[] names)
        {
            names = null;
            if (manifest == null || expectedCount < 0)
                return false;

            var result = new string[expectedCount];
            var offset = 0;
            for (var i = 0; i < expectedCount; i++)
            {
                if (!TryReadName(manifest, ref offset, out result[i]))
                    return false;
            }

            // Trailing characters mean we disagree with the debuggee about the child count - don't guess.
            if (offset != manifest.Length)
                return false;

            names = result;
            return true;
        }

        private static bool TryReadName(string manifest, ref int offset, out string name)
        {
            name = null;
            if (offset == manifest.Length)
                return false;

            if (manifest[offset] == '-')
            {
                if (manifest.Length - offset < NullNameMarker.Length ||
                    string.CompareOrdinal(manifest, offset, NullNameMarker, 0, NullNameMarker.Length) != 0)
                    return false;

                offset += NullNameMarker.Length;
                return true;
            }

            var length = 0;
            var lengthStart = offset;
            while (offset < manifest.Length && manifest[offset] >= '0' && manifest[offset] <= '9')
            {
                var digit = manifest[offset] - '0';
                if (length > (int.MaxValue - digit) / 10)
                    return false;

                length = length * 10 + digit;
                offset++;
            }

            if (offset == lengthStart || offset == manifest.Length || manifest[offset] != ':')
                return false;

            offset++;
            if (length > manifest.Length - offset)
                return false;

            name = manifest.Substring(offset, length);
            offset += length;
            return true;
        }
    }
}