using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Psi.Tree;
using System.Linq;
using IClassDeclaration = JetBrains.ReSharper.Psi.CSharp.Tree.IClassDeclaration;

#nullable enable

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemons
{
    /// <summary>
    /// Analyzes classes that derives from Godot.GodotObject
    /// </summary>
    [ElementProblemAnalyzer(typeof(IClassDeclaration), HighlightingTypes = new[] {typeof(NoParameterlessConstructorError)})]
    public class ConstructorElementProblemAnalyzer : ElementProblemAnalyzer<IClassDeclaration>
    {
        protected override void Run(IClassDeclaration element, ElementProblemAnalyzerData data, IHighlightingConsumer consumer)
        {
            var typeElement = element.DeclaredElement;
            if (typeElement == null) 
                return;
            
            if (!typeElement.DerivesFromGodotObject()) // could you please check if it only makes sense for the Node or all GodotObjects?
                return;
            
            var ctors = element.ConstructorDeclarationsEnumerable;
            if (!ctors.Any())
                return;
            foreach (var bodyChild in ctors)
            {
                if (bodyChild.DeclaredElement.Parameters.ToArray().Length == 0)
                    return;
            }
            consumer.AddHighlighting(new NoParameterlessConstructorError(element, element.NameIdentifier.GetDocumentRange()));
        }
    }
}