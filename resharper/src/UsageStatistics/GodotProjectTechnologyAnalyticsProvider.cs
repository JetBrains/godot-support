using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.ProjectsHost;
using JetBrains.ProjectModel.ProjectsHost.Impl;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotProjectTechnologyAnalyticsProvider : IProjectTechnologyAnalyticsProvider
{
    public IEnumerable<string> GetProjectTechnology(IProject project)
    {
        if (project.IsGodotProject())
        {
            yield return "Godot";
            yield return "GameDev";
        }
        
        var solution = project.GetSolution();
        // Godot Engine source itself
        if (solution.SolutionFile?.Name.Equals("godot.sln") ?? false)
        {
            yield return "Godot";
            yield return "GameDev";
            yield return "Godot C++";
        }
        
        // GDExtension with sln
        if (solution.GetSolutionMark() is SolutionMark && solution.SolutionDirectory.Combine("godot-cpp").ExistsDirectory)
        {
            yield return "GDExtension";
        }
        
        // GDExtension with CMake is in CMakeProjectTechnologyAnalyticsProvider
    }
}
