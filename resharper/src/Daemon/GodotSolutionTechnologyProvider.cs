using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE.Common;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Application;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Daemon;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionTechnologyProvider(GodotTracker godotTracker, IGodotVersion godotVersion)
    : GodotSolutionTechnologyProviderBase(godotTracker), ISolutionTechnologyProvider
{
    private readonly GodotTracker myGodotTracker = godotTracker;

    public IEnumerable<string> GetSolutionTechnology(ISolution solution)
    {
        foreach (var s in GetSolutionTechnologyInternal()) yield return s;

        if (myGodotTracker.GodotDescriptor is { IsPureGdScriptProject: true } && godotVersion.ActualVersionForSolution.Maybe.HasValue)
            yield return $"Godot version: {godotVersion.ActualVersionForSolution.Maybe.ValueOrDefault}";
    }
}