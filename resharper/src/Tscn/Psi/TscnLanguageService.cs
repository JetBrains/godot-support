using System.Collections.Generic;
using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Caches2;
using JetBrains.ReSharper.Psi.Modules;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.Tscn.Psi
{
    public class TscnLanguageService : LanguageService
    {
        public TscnLanguageService([NotNull] PsiLanguageType psiLanguageType, [NotNull] IConstantValueService constantValueService) : base(psiLanguageType, constantValueService)
        {
        }

        public override ILexerFactory GetPrimaryLexerFactory()
        {
            throw new System.NotImplementedException();
        }

        public override ILexer CreateFilteringLexer(ILexer lexer)
        {
            throw new System.NotImplementedException();
        }

        public override IParser CreateParser(ILexer lexer, IPsiModule module, IPsiSourceFile sourceFile)
        {
            throw new System.NotImplementedException();
        }

        public override IEnumerable<ITypeDeclaration> FindTypeDeclarations(IFile file)
        {
            throw new System.NotImplementedException();
        }

        public override ILanguageCacheProvider CacheProvider { get; }
        public override bool IsCaseSensitive { get; }
        public override bool SupportTypeMemberCache { get; }
        public override ITypePresenter TypePresenter { get; }

        private class TscnLexerFactory : ILexerFactory
        {
            public ILexer CreateLexer(IBuffer buffer)
            {
                throw new System.NotImplementedException();
            }
        }
    }
}