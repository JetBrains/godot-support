using System;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Propoerties;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    public static class ProjectExtensions
    {
        public static bool IsGodotProject([CanBeNull] this IProject project)
        {
            if (project == null || !project.IsValid())
                return false;

            return project.ProjectProperties.ActiveConfigurations.Configurations.Any(a => a is CSharpProjectConfiguration configuration &&
                configuration.DefineConstants.Contains("GODOT;"));
        }
        
        public static bool IsGodotProject2(this IProject project)
        {
            return project.ProjectProperties.DotNetCorePlatform?.Sdk != null && project.ProjectProperties.DotNetCorePlatform.Sdk.StartsWith("Godot.NET.SDK", StringComparison.OrdinalIgnoreCase);
        }
    }

    public static class SolutionExtensions
    {
        public static bool IsGdScriptSolution([CanBeNull] this ISolution solution)
        {
            if (solution == null || !solution.IsValid()) return false;
            return solution.SolutionFile == null && solution.SolutionDirectory.Combine("project.godot").ExistsFile;
        }
    }
}