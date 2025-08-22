using System.Collections.Generic;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Daemon;

public class GodotSolutionTechnologyProviderBase(GodotTracker godotTracker)
{
    protected IEnumerable<string> GetSolutionTechnologyInternal()
    {
        if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
        {
            yield return "Godot";
            yield return "GameDev";
            yield return "GDScript";
        }
    }
}