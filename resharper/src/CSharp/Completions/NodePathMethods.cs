using System.Collections.Generic;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    internal static class NodePathMethods
    {
        internal static readonly List<string> Methods = new()
        {
            "GetNode", "GetNodeOrNull"
        };
    }
}