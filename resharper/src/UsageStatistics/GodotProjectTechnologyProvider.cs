using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Application;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotProjectTechnologyProvider(IGodotVersion godotVersion, GodotTracker godotTracker)
    : IProjectTechnologyProvider
{
    public IEnumerable<string> GetProjectTechnology(IProject project)
    {
        if (project.IsGodotProject())
        {
            yield return "Godot";
            yield return "GameDev";
            yield return $"Godot version: {godotVersion.ActualVersionForSolution.Maybe.ValueOrDefault?.ToString() ?? "Unknown"}" ;
                
            if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
                yield return "Pure GdScript project";
        }
    }
}
    
[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionTechnologyProvider(GodotTracker godotTracker, IGodotVersion godotVersion) : ISolutionTechnologyProvider
{
    public IEnumerable<string> GetSolutionTechnology(ISolution solution)
    {
        if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
        {
            yield return "Godot";
            yield return "GameDev";
            yield return "GDScript";
            yield return $"Godot version: {godotVersion.ActualVersionForSolution.Maybe.ValueOrDefault?.ToString() ?? "Unknown"}" ;
        }
    }
}