#nullable enable

using JetBrains.Application.I18n;
using JetBrains.DocumentModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Plugins.Godot.Resources;
using JetBrains.Rider.Backend.Features.RunMarkers;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Feature.RunMarkers;

[StaticSeverityHighlighting(Severity.INFO, typeof(RunMarkerHighlighting.RunMarkers), OverlapResolve = OverlapResolveKind.NONE)]
public class ChickensoftTestRunMarkerHighlighting(
    string testIdentifier,
    bool isValid,
    string attributeId,
    DocumentRange range)
    : ICustomAttributeIdHighlighting
{
    public string AttributeId { get; } = attributeId;

    public string ToolTip => Strings.Debug_Chickensoft_Test.Format(TestIdentifier.NON_LOCALIZABLE());
    public string? ErrorStripeToolTip => null;

    public bool IsValid() => isValid && range.IsValid();

    public DocumentRange CalculateRange() => range;

    public string TestIdentifier { get; } = testIdentifier;
}