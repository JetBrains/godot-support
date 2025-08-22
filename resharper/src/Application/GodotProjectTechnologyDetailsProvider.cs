using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotProjectTechnologyDetailsProvider(GodotTracker godotTracker, IGodotVersion godotVersion) 
    : GodotProjectProviderBase, IProjectTechnologyDetailsProvider
{
    public IEnumerable<string> GetProjectTechnology(IProject project)
    {
        foreach (var s in GetProjectTechnologyInternal(project)) 
            yield return s;

        if (!project.IsGodotProject()) 
            yield break;

        if (godotVersion.ActualVersionForSolution.Maybe.HasValue)
            yield return $"Godot version: {godotVersion.ActualVersionForSolution.Maybe.ValueOrDefault}";
        
        if (godotTracker.GodotDescriptor is { IsPureGdScriptProject: true })
            yield return "Pure GdScript project";
    }
}