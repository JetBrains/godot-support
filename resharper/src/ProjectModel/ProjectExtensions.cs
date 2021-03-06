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
    }
}