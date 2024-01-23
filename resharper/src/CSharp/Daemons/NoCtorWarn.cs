using JetBrains.DocumentModel;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.Tree;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemons
{
    [StaticSeverityHighlighting(Severity.ERROR, typeof(HighlightingGroupIds.GutterMarks))]
    public class NoCtorWarn : IHighlighting
    {
        private readonly DocumentRange _documentRange;
        public readonly IClassDeclaration ClassDeclaration;

        public NoCtorWarn(IClassDeclaration classDeclaration, DocumentRange documentRange)
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

        public string ToolTip => "Constructor with no parameters is required to initialize a script/game object";
        public string ErrorStripeToolTip { get; }
    }
}