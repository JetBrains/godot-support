using JetBrains.DocumentModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Feature.Services.Daemon.Attributes;
using JetBrains.ReSharper.Psi.CSharp.Tree;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemon
{
    [StaticSeverityHighlighting(Severity.WARNING, typeof(GodotHighlightingsGroup), Languages = "CSHARP", AttributeId = AnalysisHighlightingAttributeIds.WARNING, OverlapResolve = OverlapResolveKind.WARNING, ToolTipFormatString = Tooltip)]
    public class MissingParameterlessConstructorError : IHighlighting
    {
        private const string Tooltip = "Parameterless constructor is required for the GodotEngine to initialize a script/game object";
        private readonly DocumentRange _documentRange;
        public readonly IClassDeclaration ClassDeclaration;

        public MissingParameterlessConstructorError(IClassDeclaration classDeclaration, DocumentRange documentRange)
        {
            _documentRange = documentRange;
            ClassDeclaration = classDeclaration;
        }
        public bool IsValid()
        {
            return ClassDeclaration.IsValid();
        }

        public DocumentRange CalculateRange()
        {
            return _documentRange;
        }

        public string ToolTip => Tooltip;
        public string ErrorStripeToolTip => ToolTip;
    }
}