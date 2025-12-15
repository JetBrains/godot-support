#nullable enable

using System.Collections.Generic;
using JetBrains.Application.UI.Controls.BulbMenu.Anchors;
using JetBrains.Application.UI.Controls.BulbMenu.Items;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
using JetBrains.Rider.Backend.Features.RunMarkers;
using JetBrains.TextControl.DocumentMarkup;
using JetBrains.UI.RichText;
using JetBrains.UI.ThemedIcons;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Feature.RunMarkers
{
    public class ChickensoftTestRunMarkerGutterMark() : RunMarkerGutterMarkBase<ChickensoftTestRunMarkerHighlighting>(RunMarkersThemedIcons.DebugThis.Id)
    {
        protected override IEnumerable<BulbMenuItem> GetBulbMenuItems(ISolution solution, ChickensoftTestRunMarkerHighlighting runMarker,
            IHighlighter highlighter)
        {
            var host = solution.GetComponent<FrontendBackendHost>();
            var testScriptIdentifier = runMarker.TestIdentifier;
            var iconId = RunMarkersThemedIcons.RunThis.Id;
            yield return new BulbMenuItem(new ExecutableItem(() =>
                {
                    host.Model?.RunChickensoftTest(testScriptIdentifier);
                }),
                new RichText(runMarker.ToolTip),
                iconId,
                BulbMenuAnchors.PermanentBackgroundItems);
        }
    }
}