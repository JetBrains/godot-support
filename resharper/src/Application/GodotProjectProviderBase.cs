using System.Collections.Generic;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

public abstract class GodotProjectProviderBase
{
    protected static IEnumerable<string> GetProjectTechnologyInternal(IProject project)
    {
        if (project.IsGodotProject())
        {
            yield return "Godot";
            yield return "GameDev";
        } 
    }
}