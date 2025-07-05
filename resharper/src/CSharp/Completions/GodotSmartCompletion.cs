using System;
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
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    [Language(typeof(CSharpLanguage))]
    public class GodotSmartCompletion : CSharpItemsProviderBase<CSharpCodeCompletionContext>
    {
        private readonly ILogger myLogger;

        public GodotSmartCompletion(ILogger logger)
        {
            myLogger = logger;
        }
        
        protected override bool IsAvailable(CSharpCodeCompletionContext context)
        {
            return context.BasicContext.CodeCompletionType == CodeCompletionType.BasicCompletion;
        }

        protected override bool AddLookupItems(CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            if (!IsAvailable(context))
                return false;

            var project = context.NodeInFile.GetProject();
            if (project is null)
                return false;

            if (!project.IsGodotProject())
                return false;

            if (!project.GetComponent<GodotMessagingClient>().IsReady())
                return false;

            var stringLiteral = context.StringLiteral();
            if (stringLiteral is null)
                return false;

            if (context.NodeInFile.Parent is { Parent: ICSharpArgument })
                return LookupItemsForArgument(context, collector);

            if (context.NodeInFile.Parent?.Parent is { Parent: ILocalVariableDeclaration localVariableDeclaration })
                return LookupNodePathsForVariableDeclaration(localVariableDeclaration, context, collector);

            return false;
        }

        private bool LookupItemsForArgument(CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            var invocationExpression = InvocationExpressionNavigator.GetByArgument(
                CSharpArgumentNavigator.GetByValue(context.NodeInFile.Parent as ICSharpLiteralExpression));

            var itemsCollected = false;
            itemsCollected |= LookupInputActions(invocationExpression, context, collector);
            itemsCollected |= LookupNodePathsForInvocation(invocationExpression, context, collector);

            return itemsCollected;
        }

        private bool LookupNodePathsForVariableDeclaration(ILocalVariableDeclaration localVariableDeclaration,
            CSharpCodeCompletionContext context,
            IItemsCollector collector)
        {
            if (localVariableDeclaration.Type is not IDeclaredType declaredType)
                return false;

            if (!Equals(declaredType.GetClrName().GetPersistent(), GodotTypes.NodePath))
                return false;

            return RequestNodePathCompletion(context, collector);
        }

        private bool LookupNodePathsForInvocation(IInvocationExpression invocationExpression,
            CSharpCodeCompletionContext context,
            IItemsCollector collector)
        {
            if (!GodotTypes.Node.Equals(invocationExpression.InvokedMethodContainingType()))
                return false;
            if (context.NodeInFile.Parent is not { Parent: ICSharpArgument argument })
                return false;
            if (argument.MatchingParameter == null || argument.MatchingParameter.Type is not IDeclaredType declaredType)
                return false;
            if (!Equals(declaredType.GetClrName().GetPersistent(), GodotTypes.NodePath))
                return false;

            return RequestNodePathCompletion(context, collector);
        }

        private bool RequestNodePathCompletion(CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            var client = context.BasicContext.Solution.GetComponent<GodotMessagingClient>();
            var fullPath = context.BasicContext.SourceFile.GetLocation().FullPath;

            var task = client.SendNodePathRequest(fullPath);
            if (!task.Wait(TimeSpan.FromSeconds(.5)))
            {
                myLogger.Error("Call to the GodotEditor SendInputActionsRequest wasn't finished in 0.5 seconds.");
                return false;
            }

            var response = task.Result;
            if (response == null)
                return false;

            foreach (var suggestion in response.Suggestions)
            {
                var item = new StringLiteralItem(suggestion);
                item.InitializeRanges(context.CompletionRanges, context.BasicContext);
                collector.Add(item);
            }

            return true;
        }

        private bool LookupInputActions(IInvocationExpression invocationExpression,
            CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            var containingType = invocationExpression.InvokedMethodContainingType();
            if (!GodotTypes.Input.Equals(containingType) && !GodotTypes.InputEvent.Equals(containingType))
                return false;
            if (context.NodeInFile.Parent is not { Parent: ICSharpArgument argument })
                return false;
            if (argument.MatchingParameter == null ||
                argument.MatchingParameter.Type is not IDeclaredType declaredType)
                return false;
            if (!Equals(declaredType.GetClrName().GetPersistent(), GodotTypes.StringName))
                return false;

            var client = context.BasicContext.Solution.GetComponent<GodotMessagingClient>();
            var fullPath = context.BasicContext.SourceFile.GetLocation().FullPath;
            
            var task = client.SendInputActionsRequest(fullPath);
            if (!task.Wait(TimeSpan.FromSeconds(.5)))
            {
                myLogger.Error("Call to the GodotEditor SendInputActionsRequest wasn't finished in 0.5 seconds.");
                return false;
            }

            var response = task.Result;
            if (response == null)
                return false;
                
            foreach (var suggestion in response.Suggestions)
            {
                var item = new StringLiteralItem(suggestion);
                item.InitializeRanges(context.CompletionRanges, context.BasicContext);
                collector.Add(item);
            }

            return true;
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
                return new MatchingResult(matchingResult.MatchedIndices, matchingResult.AdjustedScore - 1000,
                    matchingResult.OriginalScore);
            }

            public override void Accept(
                ITextControl textControl, DocumentRange nameRange, LookupItemInsertType insertType,
                Suffix suffix, ISolution solution, bool keepCaretStill)
            {
                base.Accept(textControl, nameRange, LookupItemInsertType.Replace, suffix, solution,
                    keepCaretStill);
            }

            public bool UseMLSort() => false;
        }
    }
}