using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.Common;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Application;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Daemon;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionTechnologyProvider(GodotTracker godotTracker, IGodotVersion godotVersion)
    : GodotSolutionTechnologyProviderBase(godotTracker), ISolutionTechnologyProvider
{
    public IEnumerable<string> GetSolutionTechnology(ISolution solution)
    {
        foreach (var s in GetSolutionTechnologyInternal()) yield return s;

        if (godotVersion.ActualVersionForSolution != null)
            yield return $"Godot version: {godotVersion.ActualVersionForSolution}";
        
        // todo: implement after RIDER-127238 [AIA] Refactor chat context preparation logic
//         yield return @"## Key Files for Project Analysis:
// - `project.godot` - Check `[editor_plugins]` section for available tools (e.g., testing frameworks)
// - `.godot/editor/project_metadata.cfg` - Contains `executable_path` to Godot executable
//
// ## Usage:
// Use the Godot executable path to check files for errors and run tests.";
        
    }
}