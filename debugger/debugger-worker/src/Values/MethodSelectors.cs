using Mono.Debugging.MetadataLite.API.Selectors;

// ReSharper disable InconsistentNaming

namespace JetBrains.ReSharper.Plugins.Godot.Rider.Debugger.Values
{
    public static class MethodSelectors
    {
        public static readonly MethodSelector NodeObject_GetChildren =
            new MethodSelector(m => m.Name == "GetChildren" && (m.Parameters.Length == 0 || m.Parameters.Length == 1));
    }
}