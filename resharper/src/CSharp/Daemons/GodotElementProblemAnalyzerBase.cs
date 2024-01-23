using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.Tree;
using System.Linq;
using IClassDeclaration = JetBrains.ReSharper.Psi.CSharp.Tree.IClassDeclaration;

#nullable enable

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemons
{
    [ElementProblemAnalyzer(typeof(IClassDeclaration), HighlightingTypes = new[] {typeof(NoCtorWarn)})]
    public class GodotElementProblemAnalyzerBase : ElementProblemAnalyzer<IClassDeclaration>
    {
        protected override void Run(IClassDeclaration element, ElementProblemAnalyzerData data, IHighlightingConsumer consumer)
        {
            var ctors = element.ConstructorDeclarationsEnumerable;
            if (!ctors.Any())
                return;
            foreach (var bodyChild in ctors)
            {
                if (bodyChild.DeclaredElement.Parameters.ToArray().Length == 0)
                    return;
            }
            consumer.AddHighlighting(new NoCtorWarn(element, element.NameIdentifier.GetDocumentRange()));
        }
    }
}