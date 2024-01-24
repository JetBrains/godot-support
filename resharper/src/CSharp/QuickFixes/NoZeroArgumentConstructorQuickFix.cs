using System;
using JetBrains.Annotations;
using JetBrains.Application.Progress;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.QuickFixes;
using JetBrains.ReSharper.Plugins.Godot.CSharp.Daemons;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.TextControl;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.QuickFixes
{
    [QuickFix]
    public class NoZeroArgumentConstructorQuickFix : QuickFixBase
    {
        private readonly NoCtorWarn error;

        public NoZeroArgumentConstructorQuickFix([NotNull] NoCtorWarn warning) => error = warning;
        
        protected override Action<ITextControl> ExecutePsiTransaction(ISolution solution, IProgressIndicator progress)
        {
            var psiModule = error.ClassDeclaration.GetPsiModule();
            var factory = CSharpElementFactory.GetInstance(error.ClassDeclaration);
            IConstructorDeclaration statement = factory.CreateConstructorDeclaration();
            error.ClassDeclaration.AddClassMemberDeclaration(statement);
            return null;
        }

        public override string Text { get; } = "Add empty constructor with no parameters";
        public override bool IsAvailable(IUserDataHolder cache) => error.IsValid();
    }
}