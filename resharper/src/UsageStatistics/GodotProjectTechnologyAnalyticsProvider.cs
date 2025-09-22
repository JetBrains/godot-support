using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Application;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotProjectTechnologyAnalyticsProvider(IGodotVersion godotVersion, GodotTracker godotTracker)
    : IProjectTechnologyAnalyticsProvider
{
    public IEnumerable<string> GetProjectTechnology(IProject project)
    {
        if (project.IsGodotProject())
        {
            yield return "Godot";
            yield return "GameDev";
            yield return $"Godot version: {godotVersion.ActualVersionForSolution?.ToString() ?? "Unknown"}" ;
                
            if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
                yield return "Pure GdScript project";
        }
    }
}
    
[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionTechnologyAnalyticsProvider(GodotTracker godotTracker, IGodotVersion godotVersion) : ISolutionTechnologyAnalyticsProvider
{
    public IEnumerable<string> GetSolutionTechnology(ISolution solution)
    {
        if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
        {
            yield return "Godot";
            yield return "GameDev";
            yield return "GDScript";
            yield return $"Godot version: {godotVersion.ActualVersionForSolution?.ToString() ?? "Unknown"}" ;
        }
    }
}