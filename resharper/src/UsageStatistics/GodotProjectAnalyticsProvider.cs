using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Application;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotProjectAnalyticsProvider : GodotProjectProviderBase, IProjectTechnologyProvider
{
    public IEnumerable<string> GetProjectTechnology(IProject project)
    {
        return GetProjectTechnologyInternal(project);
    }
}