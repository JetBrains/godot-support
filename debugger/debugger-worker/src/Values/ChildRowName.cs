namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// The name shown for one child of a Godot.Node.
    /// </summary>
    public static class ChildRowName
    {
        /// <summary>
        /// The child's own name, or its position when we have no usable one.
        /// </summary>
        /// <remarks>
        /// A missing name means we failed to read it, not that the node has none - Godot always assigns one. Falling
        /// back to the position is honest about that; substituting something plausible (the class name, say) would
        /// render a row the user cannot tell apart from a real name. A blank name is treated the same way, because an
        /// empty row is unusable even when it is what the debuggee reported.
        /// </remarks>
        public static string For(string nodeName, int index) =>
            string.IsNullOrWhiteSpace(nodeName) ? $"[{index}]" : nodeName;
    }
}
