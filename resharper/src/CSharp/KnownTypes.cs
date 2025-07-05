using JetBrains.Metadata.Reader.API;
using JetBrains.Metadata.Reader.Impl;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp
{
    public static class KnownTypes
    {
        public static readonly IClrTypeName GodotObject = new ClrTypeName("Godot.GodotObject");
    }
}