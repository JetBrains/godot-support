using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using JetBrains.Diagnostics;
using JetBrains.DocumentModel;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.CodeCompletion;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.AspectLookupItems.BaseInfrastructure;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems.Impl;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.Match;
using JetBrains.ReSharper.Feature.Services.CSharp.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.Lookup;
using JetBrains.ReSharper.Features.Intellisense.CodeCompletion.CSharp;
using JetBrains.ReSharper.Features.Intellisense.CodeCompletion.CSharp.Rules;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.CSharp.Util.Literals;
using JetBrains.ReSharper.Psi.ExpectedTypes;
using JetBrains.ReSharper.Psi.Resources;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Rider.Model;
using JetBrains.TextControl;
using JetBrains.UI.Icons;
using JetBrains.UI.RichText;
using JetBrains.Util;
using JetBrains.Util.Logging;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.Completions
{
    [Language(typeof(CSharpLanguage))]
    public class GodotResourcePathCodeCompletion : CSharpItemsProviderBase<CSharpCodeCompletionContext>
    {
        protected override bool IsAvailable(CSharpCodeCompletionContext context)
        {
            return context.BasicContext.CodeCompletionType == CodeCompletionType.BasicCompletion;
        }

        protected override bool AddLookupItems(CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            try
            {
                var projectPath = context.ProjectPath();
                if (projectPath is null) 
                    return false;
                var stringLiteral = context.StringLiteral();
                if (stringLiteral is null)
                    return false;

                var prefix = "res://";
                if (!(stringLiteral.ConstantValue.AsString() is string originalString
                      && originalString.StartsWith(prefix)))
                {
                    return false;
                }

                var relativePathString = originalString.Substring(prefix.Length);
                var fullPath = VirtualFileSystemPath.ParseRelativelyTo(relativePathString, projectPath);


                var items =
                    (from name in PathCompletions(fullPath)
                    select new ResourcePathItem(originalString, name, context.CompletionRanges))
                    .ToList();

                foreach (var item in items)
                {
                    collector.Add(item);
                }

                return !items.IsEmpty();
            }
            catch (Exception)
            {
                return false;
            }
        }

        // Suggests child files or directories of the given path
        // with the aim of completing a path to any existing regular file.
        // If path is an existing regular file, the path is complete, so no suggestions are returned.
        // Otherwise, lists the entries of the last directory in the path.
        private static IEnumerable<string> PathCompletions(VirtualFileSystemPath path)
        {
            VirtualFileSystemPath searchDir;
            switch (path.Exists)
            {
                case FileSystemPath.Existence.Directory: searchDir = path;        break;
                case FileSystemPath.Existence.Missing:   searchDir = path.Parent; break;
                default:                                 searchDir = null;        break;
            }

            if (searchDir is null)
            {
                return Enumerable.Empty<string>();
            }

            return
                from child in searchDir.GetChildren()
                select child.GetAbsolutePath().Name;
        }

        private sealed class ResourcePathItem : TextLookupItemBase
        {
            private readonly string myDisplayName;

            public ResourcePathItem(string originalFilePathString, string itemFileName, TextLookupRanges ranges)
            {
                myDisplayName = itemFileName;
                Ranges = ranges;
                var text = $"\"{originalFilePathString}{itemFileName}\"";
                Text = text;
            }

            protected override RichText GetDisplayName() => LookupUtil.FormatLookupString(myDisplayName, TextColor);

            public override IconId Image => PsiSymbolsThemedIcons.Const.Id;

            protected override void OnAfterComplete(ITextControl textControl, ref DocumentRange nameRange, ref DocumentRange decorationRange,
                TailType tailType, ref Suffix suffix, ref IRangeMarker caretPositionRangeMarker)
            {
                base.OnAfterComplete(textControl, ref nameRange, ref decorationRange, tailType, ref suffix, ref caretPositionRangeMarker);
                // Consistently move caret to end of path; i.e., end of the string literal, before closing quote
                textControl.Caret.MoveTo(Ranges.ReplaceRange.StartOffset + Text.Length - 1, CaretVisualPlacement.DontScrollIfVisible);
            }

            public override void Accept(
                ITextControl textControl, DocumentRange nameRange, LookupItemInsertType insertType,
                Suffix suffix, ISolution solution, bool keepCaretStill)
            {
                // Force replace + keep caret still in order to place caret at consistent position (see override of OnAfterComplete)
                base.Accept(textControl, nameRange, LookupItemInsertType.Replace, suffix, solution, true);
            }
        }
    }

    static class CompletionExtensions
    {
        public static VirtualFileSystemPath ProjectPath(this CSharpCodeCompletionContext context)
            => context.BasicContext.Solution.SolutionProject.ProjectLocationLive.Value;

        public static ICSharpLiteralExpression StringLiteral(this CSharpCodeCompletionContext context)
            => context.NodeInFile is ITokenNode nodeInFile
               && nodeInFile.Parent is ICSharpLiteralExpression literalExpression
               && literalExpression.Literal.IsAnyStringLiteral()
                    ? literalExpression
                    : null;
    }

}