using JetBrains.DocumentModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.CSharp.Tree;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemon
{
    [ConfigurableSeverityHighlighting(HIGHLIGHTING_ID, "CSHARP", Languages = "CSHARP", OverlapResolve = OverlapResolveKind.NONE, ToolTipFormatString = Tooltip)]
    public class MissingParameterlessConstructor : IHighlighting
    {
        private const string Tooltip = "Parameterless constructor is required for the GodotEngine to initialize a script/game object";
        public const string HIGHLIGHTING_ID = "Godot.MissingParameterlessConstructor";
        private readonly DocumentRange _documentRange;
        public readonly IClassDeclaration ClassDeclaration;

        public MissingParameterlessConstructor(IClassDeclaration classDeclaration, DocumentRange documentRange)
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