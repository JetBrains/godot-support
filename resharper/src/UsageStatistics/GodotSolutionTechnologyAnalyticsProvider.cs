using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Daemon;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionTechnologyAnalyticsProvider(GodotTracker godotTracker)
    : GodotSolutionTechnologyProviderBase(godotTracker), ISolutionTechnologyAnalyticsProvider
{
    public IEnumerable<string> GetSolutionTechnology(ISolution solution)
    {
        return GetSolutionTechnologyInternal();
    }
}