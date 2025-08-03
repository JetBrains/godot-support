using JetBrains.TextControl.DocumentMarkup;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Feature.RunMarkers
{
    [
        RegisterHighlighter(
            RUN_MARKER_ID,
            Layer = HighlighterLayer.SYNTAX + 1,
            EffectType = EffectType.GUTTER_MARK,
            GutterMarkType = typeof(ChickensoftTestRunMarkerGutterMark)
        )
    ]
    public static class ChickensoftTestRunMarkerAttributeIds
    {
        public const string RUN_MARKER_ID = "Chickensoft Godot Test Run Method Gutter Mark";
    }
}