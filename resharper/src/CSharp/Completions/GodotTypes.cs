using JetBrains.Metadata.Reader.API;
using JetBrains.Metadata.Reader.Impl;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    static class GodotTypes
    {
        private static IClrTypeName GodotTypeName(string typeName)
            => new ClrTypeName($"Godot.{typeName}");

        public static readonly IClrTypeName GD                    = GodotTypeName("GD");
        public static readonly IClrTypeName ResourceLoader        = GodotTypeName("ResourceLoader");
        public static readonly IClrTypeName PackedScene           = GodotTypeName("PackedScene");
        public static readonly IClrTypeName X509Certificate       = GodotTypeName("X509Certificate");
        public static readonly IClrTypeName CryptoKey             = GodotTypeName("CryptoKey");
        public static readonly IClrTypeName StreamTexture         = GodotTypeName("StreamTexture");
        public static readonly IClrTypeName ImageTexture          = GodotTypeName("ImageTexture");
        public static readonly IClrTypeName Texture               = GodotTypeName("Texture");
        public static readonly IClrTypeName AudioStreamMP3        = GodotTypeName("AudioStreamMP3");
        public static readonly IClrTypeName AudioStreamWAV        = GodotTypeName("AudioStreamWAV");
        public static readonly IClrTypeName AudioStreamSample     = GodotTypeName("AudioStreamSample");
        public static readonly IClrTypeName AudioStreamOggVorbis  = GodotTypeName("AudioStreamOggVorbis");
        public static readonly IClrTypeName AudioStream           = GodotTypeName("AudioStream");
        public static readonly IClrTypeName Translation           = GodotTypeName("Translation");
        public static readonly IClrTypeName VideoStream           = GodotTypeName("VideoStream");
        public static readonly IClrTypeName VideoStreamTheora     = GodotTypeName("VideoStreamTheora");
        public static readonly IClrTypeName Script                = GodotTypeName("Script");
        public static readonly IClrTypeName GDScript              = GodotTypeName("GDScript");
        public static readonly IClrTypeName CSharpScript          = GodotTypeName("CSharpScript");

        public static readonly IClrTypeName Input                 = GodotTypeName("Input");
    }
}