using JetBrains.Annotations;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel.Flavours;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    public static class ProjectExtensions
    {
        public static bool IsGodotProject([CanBeNull] this IProject project)
        {
            if (project == null || !project.IsValid())
                return false;

            return project.HasFlavour<GodotProjectFlavor>();
        }
    }
}