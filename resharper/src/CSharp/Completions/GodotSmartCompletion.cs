using JetBrains.Annotations;
using JetBrains.DocumentModel;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.CodeCompletion;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems.Impl;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.Match;
using JetBrains.ReSharper.Feature.Services.CSharp.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.Lookup;
using JetBrains.ReSharper.Features.Intellisense.CodeCompletion.CSharp.Rules;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.Resources;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.TextControl;
using JetBrains.UI.Icons;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    [Language(typeof(CSharpLanguage))]
    public class GodotSmartCompletion : CSharpItemsProviderBase<CSharpCodeCompletionContext>
    {
        protected override bool IsAvailable(CSharpCodeCompletionContext context)
        {
            return context.BasicContext.CodeCompletionType == CodeCompletionType.BasicCompletion;
        }

        protected override bool AddLookupItems(CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            // TODO: refactor with GodotResourcePathCodeCompletion
            if (!IsAvailable(context))
                return false;

            var project = context.NodeInFile.GetProject();
            if (project is null)
                return false;

            if (!project.IsGodotProject())
                return false;

            if (context.NodeInFile.Parent is not IInvocationExpression parent)
                return false;

            var client = context.BasicContext.Solution.GetComponent<GodotMessagingClient>();

            var invokedMethodName = parent.InvokedMethodName();
            if (invokedMethodName.Equals("IsActionPressed"))
            {
                var response = client.SendInputActionsRequest().Result;
                foreach (var suggestion in response.Suggestions)
                {
                    var item = new StringLiteralItem(suggestion);
                    item.InitializeRanges(context.CompletionRanges, context.BasicContext);
                    collector.Add(item);
                }

                return true;
            }

            return false;
        }
        
        private sealed class StringLiteralItem : TextLookupItemBase, IMLSortingAwareItem
        {
            public StringLiteralItem([NotNull] string text)
            {
                Text = text;
            }

            public override IconId Image => PsiSymbolsThemedIcons.Const.Id;

            public override MatchingResult Match(PrefixMatcher prefixMatcher)
            {
                var matchingResult = prefixMatcher.Match(Text);
                if (matchingResult == null)
                    return null;
                return new MatchingResult(matchingResult.MatchedIndices, matchingResult.AdjustedScore - 1000, matchingResult.OriginalScore);
            }

            public override void Accept(
                ITextControl textControl, DocumentRange nameRange, LookupItemInsertType insertType,
                Suffix suffix, ISolution solution, bool keepCaretStill)
            {
                base.Accept(textControl, nameRange, LookupItemInsertType.Replace, suffix, solution, keepCaretStill);
            }

            public bool UseMLSort() => false;
        }
    }
}