using JetBrains.DocumentModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.CSharp.Tree;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemon
{
    [StaticSeverityHighlighting(Severity.ERROR, typeof(HighlightingGroupIds.GutterMarks))]
    public class MissingParameterlessConstructorError : IHighlighting
    {
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

        public string ToolTip => "Parameterless constructor is required to initialize a script/game object";
        public string ErrorStripeToolTip { get; }
    }
}