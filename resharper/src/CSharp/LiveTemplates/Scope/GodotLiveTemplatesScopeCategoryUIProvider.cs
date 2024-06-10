using System.Collections.Generic;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Scope;
using JetBrains.ReSharper.Plugins.Godot.CSharp.FileTemplates.Scope;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates.Scope;

// Defines a category for the UI, and the scope points that it includes
[ScopeCategoryUIProvider(Priority = Priority)]
public class GodotLiveTemplatesScopeCategoryUIProvider:ScopeCategoryUIProvider
{
    // Needs to be less than other priorities in R#'s built in ScopeCategoryUIProvider
    // to push it to the end of the list
    private const int Priority = -200;
    
    public override IEnumerable<ITemplateScopePoint> BuildAllPoints()
    {
        yield return new InGodotCSharpProject();
    }

    public override string CategoryCaption => "Godot C#";
}