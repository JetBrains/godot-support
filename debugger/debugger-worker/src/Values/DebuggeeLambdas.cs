namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    /// <summary>
    /// C# expressions we compile into delegates that live and run inside the debuggee, so that fetching a
    /// Godot.Node's children costs a constant number of target invocations instead of one per child.
    /// </summary>
    /// <remarks>
    /// Both lambdas may only reference types from the runtime's core library. We hand the compiled delegate back to
    /// the debugger and invoke it in a second step, and the assembly holding it is loaded into the debuggee without a
    /// binding context - so it cannot bind to any other assembly. Naming a type from GodotSharp (even just
    /// <c>Godot.Node</c>) makes the invocation throw <c>FileNotFoundException</c> in the debuggee, which is why
    /// <see cref="ChildNames"/> reaches the node name through reflection rather than a cast, and why
    /// <see cref="ChildrenArray"/> enumerates non-generically - a children collection that is not an
    /// <c>IEnumerable&lt;Node&gt;</c> would fail a covariant cast to <c>IEnumerable&lt;object&gt;</c>.
    /// See <c>DataTableCollectionUtils.TransformToIEnumerable</c> for the same constraint hit from the platform side.
    /// </remarks>
    public static class DebuggeeLambdas
    {
        /// <summary>
        /// Copies the children into a CLR array, so their references arrive in one round trip.
        /// </summary>
        public const string ChildrenArray =
            "new System.Func<System.Collections.IEnumerable, object[]>(items => {" +
            "var list = new System.Collections.Generic.List<object>();" +
            "foreach (object c in items) list.Add(c);" +
            "return list.ToArray();" +
            "})";

        /// <summary>
        /// Packs every child's name into a single <see cref="ChildNamesManifest"/> string, so names cost one round
        /// trip rather than one target invocation each.
        /// </summary>
        /// <remarks>
        /// The <c>Name</c> property is looked up once per child type, not once per child, and the most derived
        /// declaration wins - <c>GetProperty("Name")</c> without <c>DeclaredOnly</c> throws
        /// <c>AmbiguousMatchException</c> when a subclass declares <c>new ... Name</c>.
        /// The null marker written here is <see cref="ChildNamesManifest.NullNameMarker"/>; the two are kept in step
        /// by <c>ChildNamesManifestTests.LambdaWritesTheNullMarkerTheParserExpects</c>.
        /// <para>
        /// Reading one child's name can throw - Godot's wrapper for a freed object throws on every member access,
        /// and reflection hands that back wrapped in a <c>TargetInvocationException</c>. One such child must not cost
        /// us the names of its thousand siblings, so it degrades to the null marker and the row falls back to its
        /// index. Catching inside the debuggee is the only place this is possible: once the invocation itself fails
        /// there is no partial result to salvage.
        /// </para>
        /// </remarks>
        public const string ChildNames =
            """
            new System.Func<System.Collections.IEnumerable, string>(items => {
                var builder = new System.Text.StringBuilder();
                var properties = new System.Collections.Generic.Dictionary<System.Type, System.Reflection.PropertyInfo>();
                foreach (object c in items) {
                    var type = c == null ? null : c.GetType();
                    System.Reflection.PropertyInfo property = null;
                    if (type != null && !properties.TryGetValue(type, out property)) {
                        for (var t = type; t != null && property == null; t = t.BaseType)
                            property = t.GetProperty("Name", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.Public | System.Reflection.BindingFlags.DeclaredOnly);
                        properties[type] = property;
                    }
                    string text = null;
                    if (property != null) {
                        try {
                            var name = property.GetValue(c);
                            text = name == null ? null : name.ToString();
                        } catch {
                            text = null;
                        }
                    }
                    if (text == null) builder.Append("-1:");
                    else builder.Append(text.Length).Append(':').Append(text);
                }
                return builder.ToString();
            })
            """;
    }
}
