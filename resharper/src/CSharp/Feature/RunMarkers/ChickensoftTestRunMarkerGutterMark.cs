#nullable enable

using System.Collections.Generic;
using JetBrains.Application.UI.Controls.BulbMenu.Anchors;
using JetBrains.Application.UI.Controls.BulbMenu.Items;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.Rider.Backend.Features.RunMarkers;
using JetBrains.TextControl.DocumentMarkup;
using JetBrains.UI.RichText;
using JetBrains.UI.ThemedIcons;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Feature.RunMarkers
{
    public class ChickensoftTestRunMarkerGutterMark() : RunMarkerGutterMark(RunMarkersThemedIcons.DebugThis.Id)
    {
        public override IEnumerable<BulbMenuItem> GetBulbMenuItems(IHighlighter highlighter)
        {
            if (highlighter.GetHighlighting() is not ChickensoftTestRunMarkerHighlighting runMarker) yield break;

            var solution = Shell.Instance.GetComponent<SolutionsManager>().Solution;
            if (solution == null) yield break;

            switch (runMarker.AttributeId)
            {
                case ChickensoftTestRunMarkerAttributeIds.RUN_MARKER_ID:
                    foreach (var item in GetRunItems(solution, runMarker)) yield return item;
                    yield break;

                default:
                    yield break;
            }
        }

        private static IEnumerable<BulbMenuItem> GetRunItems(ISolution solution,
            ChickensoftTestRunMarkerHighlighting runMarker)
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