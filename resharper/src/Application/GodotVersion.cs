using System;
using System.Text.RegularExpressions;
using JetBrains.Application.FileSystemTracker;
using JetBrains.Application.Parts;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotVersion : IGodotVersion
{
    private readonly ILogger myLogger;
    private readonly IFileSystemTracker myFileSystemTracker;
    private readonly GodotTracker myGodotTracker;
    public Version? ActualVersionForSolution { get; set; }

    public GodotVersion(ILogger logger, IFileSystemTracker fileSystemTracker, GodotTracker godotTracker, Lifetime lifetime)
    {
        myLogger = logger;
        myFileSystemTracker = fileSystemTracker;
        myGodotTracker = godotTracker;
        
        if (godotTracker.ProjectGodotPath == null)
            return;
        
        SetActualVersionForSolution(lifetime); 
    }
    
    private Version? TryGetVersionFromProjectGodot(VirtualFileSystemPath projectGodotPath)
    {
        var version = GetVersionFromProjectGodot(projectGodotPath);
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

    private string? GetVersionFromProjectGodot(VirtualFileSystemPath projectGodotPath)
    {
        if (!projectGodotPath.ExistsFile)
        {
            myLogger.Error($"{projectGodotPath} does not exist.");
            return null;
        }
            
        var text = projectGodotPath.ReadAllText2().Text;
        var match = Regex.Match(text, @"^config/features=PackedStringArray\(""(?<version>[^""]+)""\)", RegexOptions.Multiline);
        var groups = match.Groups;
        if (match.Success)
            return groups["version"].Value;

        return null;
    }

    private void SetActualVersionForSolution(Lifetime lt)
    {
        var projectGodotPath = myGodotTracker.ProjectGodotPath;
        myFileSystemTracker.AdviseFileChanges(lt,
            projectGodotPath,
            _ =>
            {
                ActualVersionForSolution = TryGetVersionFromProjectGodot(projectGodotPath);
            });
        ActualVersionForSolution = TryGetVersionFromProjectGodot(projectGodotPath);
    }
}