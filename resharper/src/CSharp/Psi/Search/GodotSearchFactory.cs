using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.Settings;
using JetBrains.Lifetimes;
using JetBrains.ReSharper.Plugins.Godot.Application.Settings;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.ExtensionsAPI;
using JetBrains.ReSharper.Psi.Search;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Psi.Search
{
    [PsiSharedComponent]
    public class GodotSearchFactory : DomainSpecificSearcherFactoryBase
    {
        private readonly IContextBoundSettingsStore mySettingsStore;

        public GodotSearchFactory(Lifetime lifetime, ISettingsStore settingsStore)
        {
            mySettingsStore = settingsStore.BindToContextLive(lifetime, ContextRange.ApplicationWide);
        }
        public override bool IsCompatibleWithLanguage(PsiLanguageType languageType)
        {
            return languageType.Is<CSharpLanguage>();
        }

        public override ICollection<FindResult> TransformNavigationTargets(ICollection<FindResult> targets)
        {
            var hideGeneratedCode = mySettingsStore.GetValue(GodotSettings.HideGeneratedCodeFromNavigationAccessor);

            if (!hideGeneratedCode)
                return null;

            foreach (var result in targets)
            {
                if (IsGodotObjectGeneratedDeclaration(result))
                    return targets.Where(t => !IsGodotObjectGeneratedDeclaration(t)).ToList();
            }

            return null;
        }

        private static bool IsGodotObjectGeneratedDeclaration(FindResult result)
        {
            if (result is not FindResultDeclaration { Declaration: IClassDeclaration {IsPartial: true } classDeclaration } resultDeclaration)
                return false;
            
            if (!classDeclaration.DeclaredElement.DerivesFromGodotObject())
                return false;

            return resultDeclaration.SourceFile.IsSourceGeneratedFile();
        }
    }
}