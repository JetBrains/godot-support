using System.Collections.Generic;
using JetBrains.Application;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.ChatContexts.Common;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.AI;

[ShellComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotSolutionLanguageDetailsChatContextProvider : ISolutionLanguageDetailsProvider
{
    public IEnumerable<LanguageDetails> GetLanguageDetails(ISolution solution)
    {
        var tracker = solution.TryGetComponent<GodotTracker>();
        if (tracker?.GodotDescriptor == null) return [];
        if (tracker.GodotDescriptor?.IsPureGdScriptProject == true)
            return [ new LanguageDetails("GDScript") ];
        
        var sdk = tracker.MainProject?.ProjectProperties.DotNetCorePlatform?.Sdk;
        if (sdk != null)
            return [ new LanguageDetails("C#") ];
        return [];
    }
}