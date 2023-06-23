using System;
using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.Application.UI.Icons.Shell;
using JetBrains.Diagnostics;
using JetBrains.DocumentModel;
using JetBrains.Metadata.Reader.API;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Resources;
using JetBrains.ReSharper.Feature.Services.CodeCompletion;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.LookupItems.Impl;
using JetBrains.ReSharper.Feature.Services.CodeCompletion.Infrastructure.Match;
using JetBrains.ReSharper.Feature.Services.CSharp.CodeCompletion.Infrastructure;
using JetBrains.ReSharper.Feature.Services.Lookup;
using JetBrains.ReSharper.Features.Intellisense.CodeCompletion.CSharp.Rules;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.Psi.ExpectedTypes;
using JetBrains.ReSharper.Psi.Resources;
using JetBrains.ReSharper.Psi.Tree;
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
        private const string Prefix = "res://";


        protected override bool IsAvailable(CSharpCodeCompletionContext context)
        {
            return context.BasicContext.CodeCompletionType == CodeCompletionType.BasicCompletion;
        }

        protected override bool AddLookupItems(CSharpCodeCompletionContext context, IItemsCollector collector)
        {
            var client = context.BasicContext.Solution.GetComponent<GodotMessagingClient>();
            
            if (!IsAvailable(context))
                return false;

            var project = context.NodeInFile.GetProject();
            if (project is null)
                return false;

            if (!project.IsGodotProject())
                return false;

            //return Logger.CatchSilent(() =>
            {
                var projectPath = project.ProjectLocationLive.Value;
                if (projectPath is null) 
                    return false;
                var stringLiteral = context.StringLiteral();
                if (stringLiteral is null)
                    return false;

                var originalString = string.Empty;
                if (stringLiteral.ConstantValue.AsString() is string os)
                {
                    originalString = os;
                }

                var relativePathString = string.Empty;
                if (originalString.StartsWith(Prefix)) 
                    relativePathString = originalString.Substring(Prefix.Length);
                var searchPath = VirtualFileSystemPath.ParseRelativelyTo(relativePathString, projectPath);

                var completions = FullPathCompletions(context, searchPath).ToList();

                // If path leads outside project (e.g., due to `..` going up too many levels), don't provide completions.
                if (!projectPath.IsPrefixOf(searchPath))
                {
                    return false;
                }

                if (originalString.StartsWith(Prefix))
                {
                    completions.AddRange(OneLevelPathCompletions(searchPath));
                }
                
                var items = 
                    (from completion in completions.Distinct() 
                     select new ResourcePathItem(projectPath, completion, context.CompletionRanges))
                    .ToList();
                foreach (var item in items)
                {
                    collector.Add(item);
                }
                
                if (!originalString.StartsWith(Prefix) && completions.Any())
                {
                    // workarounds RIDER-90857
                    var resItem = new StringLiteralItem(Prefix);
                    var ranges = context.CompletionRanges;
                    var range = new TextLookupRanges(new DocumentRange(ranges.InsertRange.StartOffset + 1,
                            ranges.InsertRange.EndOffset + 1),
                        new DocumentRange(ranges.ReplaceRange.StartOffset + 1,
                            ranges.ReplaceRange.EndOffset - 1)
                    );

                    resItem.InitializeRanges(range, context.BasicContext);
                    collector.Add(resItem);
                }
                return !items.IsEmpty();
            }
                //);
        }

        static GodotResourcePathCodeCompletion()
        {
            ourFileExtensionsByType = new Dictionary<IClrTypeName, IList<string>>();
            // Scenes
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.PackedScene, "tscn");

            // Crypto keys
            // https://github.com/godotengine/godot/blob/297241f368632dd91a3e7df47da3d9e5197e4f1e/core/crypto/crypto.cpp#L168
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.X509Certificate, "crt");
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.CryptoKey, "key", "pub");

            // AudioStreamMP3 (.mp3)
            // https://github.com/godotengine/godot/blob/a5bc65bbadad814a157283749c1ef8552f1663c4/modules/minimp3/resource_importer_mp3.cpp#L50
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.AudioStreamMP3, "mp3");

            // AudioStreamWAV (.wav)
            // https://github.com/godotengine/godot/blob/1c820f19b1a0ba72316896ad354cb31391638a3b/editor/import/resource_importer_wav.cpp#L50
            // AudioStreamSample is older type name: https://github.com/godotengine/godot/blob/1c0d9eef7a697af1e9142a2a95c824c9554a6ca1/editor/import/resource_importer_wav.cpp#L58
            ourFileExtensionsByType.InsertOrAppendAtEach(new[] { GodotTypes.AudioStreamWAV, GodotTypes.AudioStreamSample }, "wav");

            // AudioStreamOggVorbis (.ogg)
            // https://github.com/godotengine/godot/blob/2bf8c4a6d0c553d450695d1988fac39df638ad9a/modules/vorbis/resource_importer_ogg_vorbis.cpp#L52
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.AudioStreamOggVorbis, "ogg");

            // AudioStream
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.AudioStream, "mp3", "wav", "ogg");

            // Images
            // PNG     (.png):        https://github.com/godotengine/godot/blob/dcdc6954f89876249bd0600b154a900e5bf83d36/drivers/png/image_loader_png.cpp#L55
            // JPG     (.jpg, .jpeg): https://github.com/godotengine/godot/blob/40c360b8703449bb6a3299878600fab45abf9f86/modules/jpg/image_loader_jpegd.cpp#L122
            // BMP     (.bmp)         https://github.com/godotengine/godot/blob/dcdc6954f89876249bd0600b154a900e5bf83d36/modules/bmp/image_loader_bmp.cpp#L290
            // SVG     (.svg):        https://github.com/godotengine/godot/blob/79463aa5defb083569d193658a62755223f14dc4/modules/svg/image_loader_svg.cpp#L136
            // WebP    (.webp):       https://github.com/godotengine/godot/blob/c18d0f20357a11bd9cfa2f57b8b9b500763413bc/modules/webp/image_loader_webp.cpp#L66
            // HDR     (.hdr):        https://github.com/godotengine/godot/blob/d27f60f0e8d78059f8d075e16f0d242a7673bba0/modules/hdr/image_loader_hdr.cpp#L149
            // OpenEXR (.exr):        https://github.com/godotengine/godot/blob/d27f60f0e8d78059f8d075e16f0d242a7673bba0/modules/tinyexr/image_loader_tinyexr.cpp#L292
            // TGA     (.tga):        https://github.com/godotengine/godot/blob/dcdc6954f89876249bd0600b154a900e5bf83d36/modules/tga/image_loader_tga.cpp#L337
            ourFileExtensionsByType.InsertOrAppendAtEach(
                new[] { GodotTypes.Texture, GodotTypes.StreamTexture, }, 
                "png", 
                "jpg", "jpeg",
                "bmp",
                "svg",
                "webp", 
                "hdr",
                "exr",
                "tga");

            // DDS (.dds): https://github.com/godotengine/godot/blob/d26442e709f6361af9ac755ec9291bb43f2cd69b/modules/dds/texture_loader_dds.cpp#L431
            ourFileExtensionsByType.InsertOrAppendAtEach(new[] { GodotTypes.Texture, GodotTypes.ImageTexture, }, "dds");

            // VideoStreamTheora
            // https://github.com/godotengine/godot/blob/d26442e709f6361af9ac755ec9291bb43f2cd69b/modules/theora/video_stream_theora.cpp#L694
            ourFileExtensionsByType.InsertOrAppendAtEach(new[] { GodotTypes.VideoStreamTheora, GodotTypes.VideoStream }, "ogv");

            // Translations
            // https://github.com/godotengine/godot/blob/297241f368632dd91a3e7df47da3d9e5197e4f1e/core/io/translation_loader_po.cpp#L359
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.Translation, "po", "mo");

            // Scripts
            // GodotScript (.gd): https://github.com/godotengine/godot/blob/ba3734e69a2f2a4f6c4f908958268762fd805cd2/modules/gdscript/gdscript.cpp#L2410
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.GDScript, "gd");
            // C#          (.cs): https://github.com/godotengine/godot/blob/14d021287bced6a7f5ab9db24936bd07b4cfdfd0/modules/mono/csharp_script.cpp#L1238
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.CSharpScript, "cs");
            ourFileExtensionsByType.InsertOrAppend(GodotTypes.Script, "gd", "cs");
        }

        private static readonly Dictionary<IClrTypeName, IList<string>> ourFileExtensionsByType;

        // Suggests full paths to resource files based on the string completion's context --
        // in particular, if the string is an argument to a Godot load (GD.Load or ResourceLoader.Load).
        // Resource type is inferred based on Load's type argument or assignment destination.
        private IEnumerable<CompletionItem> FullPathCompletions(CSharpCodeCompletionContext context, VirtualFileSystemPath searchPath)
        {
            if (!(context.IfGodotLoadGetResourceType() is IClrTypeName resourceType))
                return Enumerable.Empty<CompletionItem>();

            ourFileExtensionsByType.TryGetValue(resourceType, out var matchingFileExtensions);
            return matchingFileExtensions is null 
                ? Enumerable.Empty<CompletionItem>()
                : ResourceFiles(searchPath, matchingFileExtensions);
        }

        // Suggests child files or directories of the given path
        // with the aim of completing a path to any existing regular file.
        // If path is an existing regular file, the path is complete, so no suggestions are returned.
        // Otherwise, lists the entries of the last directory in the path.
        private IEnumerable<CompletionItem> OneLevelPathCompletions(VirtualFileSystemPath path)
        {
            var searchDir = SearchDir(path);
            if (searchDir is null)
            {
                return Enumerable.Empty<CompletionItem>();
            }

            return
                from child in searchDir.GetChildren()
                where !ShouldIgnore(child.GetAbsolutePath())
                select new CompletionItem(child.IsDirectory, searchDir, child.GetAbsolutePath(), child.GetAbsolutePath().ExtensionWithDot);
        }

        private static bool ShouldIgnore(VirtualFileSystemPath path)
        {
            // Do not check or suggest:
            // - dotfiles or directories starting with "."
            // - Godot .import files
            // - iml - some subsidiary file, which Rider creates
            // - project.godot
            return path.Name.StartsWith(".")
                   || "import".Equals(path.ExtensionNoDot, StringComparison.OrdinalIgnoreCase)
                   || "iml".Equals(path.ExtensionNoDot, StringComparison.OrdinalIgnoreCase)
                   || "project.godot".Equals(path.Name, StringComparison.OrdinalIgnoreCase)
                   || path.ExtensionNoDot.Equals("csproj", StringComparison.OrdinalIgnoreCase)
                   || path.ExtensionNoDot.Equals("sln", StringComparison.OrdinalIgnoreCase);
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

        private IEnumerable<CompletionItem> ResourceFiles(VirtualFileSystemPath path, IList<string> extensions)
        {
            var searchDir = SearchDir(path);
            if (searchDir is null)
            {
                return Enumerable.Empty<CompletionItem>();
            }

            return
                from p in ResourceFilesInner(searchDir, extensions)
                select new CompletionItem(p.ExistsDirectory, searchDir,p, p.ExtensionWithDot) ;
        }

        private static IEnumerable<VirtualFileSystemPath> ResourceFilesInner(VirtualFileSystemPath path, IList<string> extensions)
        {
            if (ShouldIgnore(path))
            {
                return Enumerable.Empty<VirtualFileSystemPath>();
            }

            if (path.ExistsFile && extensions.Any(ext => ext.Equals(path.ExtensionNoDot, StringComparison.OrdinalIgnoreCase)))
            {
                return new[] { path };
            }

            if (path.ExistsDirectory)
            {
                return
                    path.GetChildren()
                        .SelectMany(child => ResourceFilesInner(child.GetAbsolutePath(), extensions));
            }

            return Enumerable.Empty<VirtualFileSystemPath>();
        }

        private class CompletionItem
        {
            public readonly bool IsDirectory;
            public readonly VirtualFileSystemPath OriginalFolder;
            public readonly VirtualFileSystemPath Completion;
            public readonly string ExtensionWithDot;

            public CompletionItem(bool isDirectory, VirtualFileSystemPath originalFolder ,VirtualFileSystemPath completion, string extensionWithDot)
            {
                OriginalFolder = originalFolder;
                IsDirectory = isDirectory;
                Completion = completion;
                ExtensionWithDot = extensionWithDot;
            }
        }

        private sealed class ResourcePathItem : TextLookupItemBase
        {
            private readonly CompletionItem myCompletionItem;
            
            public ResourcePathItem(VirtualFileSystemPath projectPath,
                CompletionItem completionItem, TextLookupRanges ranges)
            {
                myCompletionItem = completionItem;
                Ranges = ranges;
                Text = $"\"{Prefix}{completionItem.Completion.MakeRelativeTo(projectPath).NormalizeSeparators(FileSystemPathEx.SeparatorStyle.Unix)}\"";
            }

            protected override RichText GetDisplayName() => LookupUtil.FormatLookupString(myCompletionItem.Completion.MakeRelativeTo(myCompletionItem.OriginalFolder).NormalizeSeparators(FileSystemPathEx.SeparatorStyle.Unix), TextColor);

            public override IconId Image => myCompletionItem.IsDirectory
                ? ProjectModelThemedIcons.Directory.Id
                : ShellFileIcon.Create(myCompletionItem.ExtensionWithDot);

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
                return new MatchingResult(matchingResult.MatchedIndices, matchingResult.AdjustedScore - 100, matchingResult.OriginalScore);
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