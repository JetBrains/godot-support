using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionTechnologyAnalyticsProvider(GodotTracker godotTracker) : ISolutionTechnologyAnalyticsProvider
{
    public IEnumerable<string> GetSolutionTechnology(ISolution solution)
    {
        if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
        {
            yield return "Godot";
            yield return "GameDev";
            yield return "GDScript";
        }
    }
}