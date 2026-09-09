using Mono.Debugging.MetadataLite.API;
using Mono.Debugging.MetadataLite.API.Selectors;

// ReSharper disable InconsistentNaming

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    public static class MethodSelectors
    {
        public static readonly MethodSelector NodeObject_GetChildren =
            new(m => m.Name == "GetChildren" && !m.IsGenericMethod &&
                     (m.Parameters.Length == 0 ||
                      // GetChildren(bool includeInternal). Constrained to bool so the hard-coded `false` we pass
                      // stays honest if Godot ever adds another single-argument overload.
                      m.Parameters.Length == 1 && m.Parameters[0].Type.IsBoolean()));

        // Indexer getter (this[int]) on the Godot.Collections.Array<Node> returned by Node.GetChildren
        public static readonly MethodSelector ArrayLike_GetItem =
            new(m => m.Name == "get_Item" && !m.IsGenericMethod && m.Parameters.Length == 1 &&
                     m.Parameters[0].Type.IsInt32());

        // Func<IEnumerable, T>.Invoke(IEnumerable), used to call the lambdas we compile into the debuggee.
        // See DebuggeeLambdas.
        public static readonly MethodSelector Delegate_Invoke =
            new(m => m.Name == "Invoke" && !m.IsGenericMethod && m.Parameters.Length == 1);

        public static readonly MethodSelector NodeObject_GetParent =
            new(m => m.Name == "GetParent" && !m.IsGenericMethod && m.Parameters.Length == 0);
    }
}
