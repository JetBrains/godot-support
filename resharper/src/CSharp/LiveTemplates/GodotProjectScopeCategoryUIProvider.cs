using System.Collections.Generic;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Scope;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Templates;
using JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates.Scope;
using JetBrains.UI.ThemedIcons;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates
{
    // Defines a category for the UI, and the scope points that it includes
    [ScopeCategoryUIProvider(Priority = Priority, ScopeFilter = ScopeFilter.Project)]
    public class GodotProjectScopeCategoryUIProvider : ScopeCategoryUIProvider
    {
        static GodotProjectScopeCategoryUIProvider()
        {
            // These get added to a static dictionary, so they can be referenced by name from templates
            TemplateImage.Register("GodotCSharp", GodotLogoIconsThemedIcons.Godot.Id);
        }

        // Needs to be less than other priorities in R#'s built in ScopeCategoryUIProvider
        // to push it to the end of the list
        private const int Priority = -200;

        public GodotProjectScopeCategoryUIProvider()
            : base(GodotLogoIconsThemedIcons.Godot.Id)
        {
            // The main scope point is used to the UID of the QuickList for this category.
            // It does nothing unless there is also a QuickList stored in settings.
            MainPoint = new InGodotCSharpProject();
        }

        public override IEnumerable<ITemplateScopePoint> BuildAllPoints()
        {
            yield return new InGodotCSharpProject();
        }

        public override string CategoryCaption => "Godot";
    }
}