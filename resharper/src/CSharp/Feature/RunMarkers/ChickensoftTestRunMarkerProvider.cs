#nullable enable

using JetBrains.Application.Settings;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Caches.SymbolCache;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Rider.Backend.Features.RunMarkers;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Feature.RunMarkers;

[Language(typeof(CSharpLanguage))]
// [HighlightingSource(HighlightingTypes = [typeof(ChickensoftTestRunMarkerHighlighting)])] is not allowed by contract
public class ChickensoftTestRunMarkerProvider : IRunMarkerProvider
{
    public void CollectRunMarkers(IFile file, IContextBoundSettingsStore settings, IHighlightingConsumer consumer)
    {
        if (file.GetSolution().GetComponent<GodotTracker>().GodotDescriptor == null) return;
        if (file is not ICSharpFile csharpFile) return;

        foreach (var declaration in CachedDeclarationsCollector.Run<IMethodDeclaration>(csharpFile))
        {
            if (declaration.DeclaredElement is not { } method) continue;

            if (!ChickensoftTestRunMarkerUtil.IsSuitableMethod(method)) continue;
            var range = declaration.GetNameDocumentRange();
            var type = method.ContainingType;
            var testIdentifier = $"{type.GetClrName().ShortName}.{method.ShortName}";
            var highlighting = new ChickensoftTestRunMarkerHighlighting(testIdentifier, declaration.IsValid(),
                ChickensoftTestRunMarkerAttributeIds.RUN_MARKER_ID, range);
            consumer.AddHighlighting(highlighting, range);
        }

        foreach (var declaration in CachedDeclarationsCollector.Run<IClassDeclaration>(csharpFile))
        {
            if (declaration.DeclaredElement is not { } @class) continue;
            if (!@class.DerivesFromChickenTest()) continue;

            var classNameRange = declaration.GetNameDocumentRange();
            var classIdentifier = declaration.NameIdentifier.Name;
            var classNameHighlighting = new ChickensoftTestRunMarkerHighlighting(classIdentifier, true,
                ChickensoftTestRunMarkerAttributeIds.RUN_MARKER_ID, classNameRange);
            consumer.AddHighlighting(classNameHighlighting, classNameRange);
        }
    }

    public double Priority => RunMarkerProviderPriority.DEFAULT;
}