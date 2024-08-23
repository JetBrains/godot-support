using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.UsageStatistics;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.UsageStatistics
{
    [SolutionComponent(Instantiation.DemandAnyThreadUnsafe)]
    public class GodotProjectTechnologyProvider : IProjectTechnologyProvider
    {
        public IEnumerable<string> GetProjectTechnology(IProject project)
        {
            if (project.IsGodotProject())
            {
                yield return "Godot";
                yield return "GameDev";
            }
        }
    }
    
    [SolutionComponent(Instantiation.DemandAnyThreadUnsafe)]
    public class GodotSolutionTechnologyProvider : ISolutionTechnologyProvider
    {
        public IEnumerable<string> GetSolutionTechnology(ISolution solution)
        {
            if (solution.IsGdScriptSolution())
            {
                yield return "Godot";
                yield return "GameDev";
                yield return "GDScript";
            }
        }
    }
}