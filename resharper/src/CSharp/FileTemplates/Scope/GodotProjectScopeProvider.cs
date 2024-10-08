﻿using System.Collections.Generic;
using JetBrains.Application;
using JetBrains.Application.Parts;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Context;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Scope;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.FileTemplates.Scope
{
    // Provides the scope points that are valid for the given context
    [ShellComponent(InstantiationEx.LegacyDefault)]
    public class GodotProjectScopeProvider : ScopeProvider
    {
        public GodotProjectScopeProvider()
        {
            // Used when creating scope point from settings
            Creators.Add(TryToCreate<InGodotCSharpProject>);
        }

        public override IEnumerable<ITemplateScopePoint> ProvideScopePoints(TemplateAcceptanceContext context)
        {
            var project = context.GetProject();
            if (!project.IsGodotProject())
                yield break;

            // We could check for C# here, like InRazorCSharpProject, but we only really support C# Godot projects
            // Are there any other types?
            yield return new InGodotCSharpProject();
        }
    }
}