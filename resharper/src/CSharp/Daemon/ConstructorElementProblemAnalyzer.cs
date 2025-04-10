﻿using System.Linq;
using JetBrains.Application.Parts;
using JetBrains.ReSharper.Feature.Services.Daemon;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ReSharper.Psi.Tree;
using IClassDeclaration = JetBrains.ReSharper.Psi.CSharp.Tree.IClassDeclaration;

#nullable enable

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Daemon
{
    /// <summary>
    /// Analyzes classes that derives from Godot.GodotObject
    /// </summary>
    [ElementProblemAnalyzer(Instantiation.DemandAnyThreadSafe, typeof(IClassDeclaration), HighlightingTypes = new[] {typeof(MissingParameterlessConstructor)})]
    public class ConstructorElementProblemAnalyzer : ElementProblemAnalyzer<IClassDeclaration>
    {
        protected override void Run(IClassDeclaration element, ElementProblemAnalyzerData data, IHighlightingConsumer consumer)
        {
            var typeElement = element.DeclaredElement;
            if (typeElement == null) 
                return;
            
            if (typeElement.IsAbstract)
                return;

            if (typeElement.Module is ProjectPsiModuleBase projectPsiModuleBase)
                if (!projectPsiModuleBase.Project.IsGodotProject2()) return;
            
            if (!typeElement.DerivesFromGodotObject()) // could you please check if it only makes sense for the Node or all GodotObjects?
                return;
            
            var ctors = element.ConstructorDeclarationsEnumerable;
            if (!ctors.Any())
                return;
            foreach (var bodyChild in ctors)
            {
                if (bodyChild.DeclaredElement == null) return;
                if (bodyChild.DeclaredElement.Parameters.ToArray().Length == 0) return;
            }
            consumer.AddHighlighting(new MissingParameterlessConstructor(element, element.NameIdentifier.GetDocumentRange()));
        }
    }
}