#nullable enable
using System;
using System.Text.RegularExpressions;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Parts;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.Util;
using JetBrains.Util.Logging;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotVersion : IGodotVersion
{
    private readonly IFileSystemTracker myFileSystemTracker;
    private Version? myVersion;
    private readonly VirtualFileSystemPath? mySolutionDirectory;
    private static readonly ILogger ourLogger = Logger.GetLogger<GodotVersion>();
    public ViewableProperty<Version> ActualVersionForSolution { get; } = new(new Version(0, 0));

    public GodotVersion(ISolution solution, IFileSystemTracker fileSystemTracker, GodotTracker? godotTracker, Lifetime lifetime)
    {
        myFileSystemTracker = fileSystemTracker;
        mySolutionDirectory = solution.SolutionDirectory;
        if (mySolutionDirectory is { IsAbsolute: false })
            mySolutionDirectory = solution.SolutionDirectory.ToAbsolutePath( FileSystemUtil.GetCurrentDirectory().ToVirtualFileSystemPath());

        if (godotTracker != null)
            SetActualVersionForSolution(lifetime); 
    }

    private static VirtualFileSystemPath GetProjectFilePath(VirtualFileSystemPath solutionDirectory)
    {
        var projectVersionTxtPath = solutionDirectory.Combine("project.godot");
        return projectVersionTxtPath;
    }
    private Version? TryGetVersionFromProjectVersion(VirtualFileSystemPath solutionDirectory)
    {
        var version = GetProjectSettingVersion(solutionDirectory);
        if (version == null)
            return null;
            
        return Parse(version);
    }

    private static Version? Parse(string input)
    {
        if (string.IsNullOrEmpty(input))
            return null;
        return Version.TryParse(input, out var version) ? version : null;
    }

    private static string? GetProjectSettingVersion(VirtualFileSystemPath solutionDirectory)
    {
        var projectVersionTxtPath = GetProjectFilePath(solutionDirectory);
        if (!projectVersionTxtPath.ExistsFile)
            return null;
            
        var text = projectVersionTxtPath.ReadAllText2().Text;
        var match = Regex.Match(text, @"^config/features=PackedStringArray\(""(?<version>[^""]+)""\)", RegexOptions.Multiline);
        var groups = match.Groups;
        if (match.Success)
            return groups["version"].Value;

        return null;
    }
    private void UpdateActualVersionForSolution()
    {
        var version = GetActualVersionForSolution();
        ourLogger.Verbose($"UpdateActualVersionForSolution to {version}");
        ActualVersionForSolution.SetValue(version);
    }
    private Version GetActualVersionForSolution()
    {
        if (myVersion != null)
            return myVersion;

        return new Version(0, 0);
    }

    private void SetActualVersionForSolution(Lifetime lt)
    {
        if (mySolutionDirectory == null)
            return;
        
        var projectVersionTxtPath = GetProjectFilePath(mySolutionDirectory);
        myFileSystemTracker.AdviseFileChanges(lt,
            projectVersionTxtPath,
            _ =>
            {
                myVersion = TryGetVersionFromProjectVersion(mySolutionDirectory);
                UpdateActualVersionForSolution();
            });
        myVersion = TryGetVersionFromProjectVersion(mySolutionDirectory);

        UpdateActualVersionForSolution();
    }
}