using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.IDE;
using JetBrains.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Application;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotProjectTechnologyDetailsProvider : GodotProjectProviderBase, IProjectTechnologyDetailsProvider
{
    public IEnumerable<string> GetProjectTechnology(IProject project)
    {
        return GetProjectTechnologyInternal(project);
    }
}