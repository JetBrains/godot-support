using System.Collections.Generic;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.ChatContexts.Common;
using JetBrains.ReSharper.Plugins.Godot.Application;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.AI;

[SolutionComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionChatContextProvider(GodotTracker godotTracker, IGodotVersion godotVersion)
    : ISolutionTechnologyDetailsProvider
{
    public IEnumerable<TechnologyDetails> GetSolutionTechnologyDetails(ISolution solution)
    {
        var descriptor = godotTracker.GodotDescriptor;
        if (descriptor == null)
            yield break;

        var version = godotVersion.ActualVersionForSolution?.ToString();
        yield return new GameEngineDetails("Godot", version, false);
        
        if (descriptor.IsPureGdScriptProject)
            yield return new LanguageDetails("GDScript");
        else
        {
            var sdk = godotTracker.MainProject?.ProjectProperties.DotNetCorePlatform?.Sdk;
            if (sdk != null)
                yield return new TechnologyDetails(sdk, TechnologyDetails.TechnologyType.SDK);
        }
        
        // todo: implement after RIDER-127238 [AIA] Refactor chat context preparation logic
        //         yield return @"## Key Files for Project Analysis:
        // - `project.godot` - Check `[editor_plugins]` section for available tools (e.g., testing frameworks)
        // - `.godot/editor/project_metadata.cfg` - Contains `executable_path` to Godot executable
        //
        // ## Usage:
        // Use the Godot executable path to check files for errors and run tests.";
    }
}