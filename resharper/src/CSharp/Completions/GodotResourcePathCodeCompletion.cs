using System;
using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.DocumentModel;
using JetBrains.Metadata.Reader.API;
using JetBrains.Metadata.Reader.Impl;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.CodeCompletion;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems.Impl;
using JetBrains.ReSharper.Feature.Services.CSharp.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.Lookup;
using JetBrains.ReSharper.Features.Intellisense.CodeCompletion.CSharp.Rules;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.CSharp.Tree;
using JetBrains.ReSharper.Psi.CSharp.Util;
using JetBrains.ReSharper.Psi.CSharp.Util.Literals;
using JetBrains.ReSharper.Psi.ExpectedTypes;
using JetBrains.ReSharper.Psi.Resources;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.ReSharper.Psi.Util;
using JetBrains.TextControl;
using JetBrains.UI.Icons;
using JetBrains.UI.RichText;
using JetBrains.Util;
using JetBrains.Util.Special;

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

                // If path leads outside project (e.g., due to `..` going up too many levels), don't provide completions.
                if (!fullPath.IsDescendantOf(projectPath))
                {
                    return false;
                }

                var completions = Enumerable.Empty<string>();
                if (ExpectsSceneResource(context))
                {
                    completions = completions.Concat(SceneFiles(fullPath));
                }
                completions = completions.Concat(PathCompletions(fullPath));

                var items = 
                    (from completion in completions.Distinct() 
                     select new ResourcePathItem(originalString, completion, context.CompletionRanges))
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

        private static bool ExpectsSceneResource(CSharpCodeCompletionContext context)
        {
            // Get invocation which is using the string being completed
            if (!(
                InvocationExpressionNavigator.GetByArgument(
                    CSharpArgumentNavigator.GetByValue(
                        context.NodeInFile.Parent as ICSharpLiteralExpression))
                is IInvocationExpression invocation))
            {
                return false;
            }

            // GD.Load or ResourceLoader.Load expecting PackedScene
            if (invocation.IsGodotLoad(GodotTypes.PackedScene))
            {
                return true;
            }

            // Load with no generic type argument but being assigned to PackedScene
            return AssignmentExpressionNavigator.GetBySource(invocation)
                    is IAssignmentExpression assignment 
                   && assignment.Dest.Type() is IDeclaredType lhsType
                   && GodotTypes.PackedScene.Equals(lhsType.GetClrName())
                   && invocation.IsGodotLoad(null);
        }

        // Suggests child files or directories of the given path
        // with the aim of completing a path to any existing regular file.
        // If path is an existing regular file, the path is complete, so no suggestions are returned.
        // Otherwise, lists the entries of the last directory in the path.
        private static IEnumerable<string> PathCompletions(VirtualFileSystemPath path)
        {
            var searchDir = SearchDir(path);
            if (searchDir is null)
            {
                return Enumerable.Empty<string>();
            }

            return
                from child in searchDir.GetChildren()
                select child.GetAbsolutePath().Name;
        }

        private static VirtualFileSystemPath SearchDir(VirtualFileSystemPath path)
        {
            switch (path.Exists)
            {
                case FileSystemPath.Existence.Directory: return path;
                case FileSystemPath.Existence.Missing:   return path.Parent;
                default:                                 return null;
            }
        }

        private static IEnumerable<string> SceneFiles(VirtualFileSystemPath path)
        {
            var searchDir = SearchDir(path);
            if (searchDir is null)
            {
                return Enumerable.Empty<string>();
            }

            return
                from p in SceneFilesInner(searchDir)
                select p.MakeRelativeTo(path).ToString().Replace('\\', '/');
        }

        private static IEnumerable<VirtualFileSystemPath> SceneFilesInner(VirtualFileSystemPath path)
        {
            if (path.ExistsFile && path.ExtensionNoDot == "tscn")
            {
                return new[] { path };
            }

            if (path.ExistsDirectory)
            {
                return
                    path.GetChildren()
                        .SelectMany(child => SceneFilesInner(child.GetAbsolutePath()));
            }

            return Enumerable.Empty<VirtualFileSystemPath>();
        }

        private sealed class ResourcePathItem : TextLookupItemBase
        {
            private readonly string myDisplayName;

            public ResourcePathItem(string originalFilePathString, string completion, TextLookupRanges ranges)
            {
                myDisplayName = completion;
                Ranges = ranges;
                var text = $"\"{originalFilePathString}{completion}\"";
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

    static class GodotTypes
    {
        private static IClrTypeName GodotTypeName(string typeName)
            => new ClrTypeName($"Godot.{typeName}");

        public static readonly IClrTypeName GD             = GodotTypeName("GD");
        public static readonly IClrTypeName ResourceLoader = GodotTypeName("ResourceLoader");
        public static readonly IClrTypeName PackedScene    = GodotTypeName("PackedScene");
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

        public static bool IsDescendantOf(this VirtualFileSystemPath path, VirtualFileSystemPath other)
            => path.FullPath.StartsWith(other.FullPath);


        public static bool IsGodotLoad(
            this IInvocationExpression invocation,
            [CanBeNull] IClrTypeName typeArg)
        {
            var typeArgs = 
                typeArg is IClrTypeName ta
                    ? new[] { ta } 
                    : new IClrTypeName[] { };
            return invocation.InvokesMethod(GodotTypes.GD,             "Load", typeArgs)
                || invocation.InvokesMethod(GodotTypes.ResourceLoader, "Load", typeArgs);
        }

        public static bool InvokesMethod(
            this IInvocationExpression invocationExpression, 
            IClrTypeName typeName,
            string name,
            params IClrTypeName[] expectedTypeArgs)
        {
            var actualTypeArgs = invocationExpression.Reference.Invocation.TypeArguments;
            var typeArgsMatch = 
                actualTypeArgs.Count == expectedTypeArgs.Length
                 && Enumerable.Range(0, expectedTypeArgs.Length)
                    .All(i =>
                        actualTypeArgs[i] is IDeclaredType dt
                         && dt.GetClrName().Equals(expectedTypeArgs[i]));
            return invocationExpression.Reference.Resolve().DeclaredElement is IMethod method
                   && name.Equals(method.ShortName)
                   && method.ContainingType is ITypeElement type
                   && type.GetClrName().Equals(typeName)
                   && typeArgsMatch;
        }
    }

}