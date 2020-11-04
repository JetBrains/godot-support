using System.Collections.Generic;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Scope;
using JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates.Scope;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates
{
    [ScopeCategoryUIProvider(Priority = Priority)]
    public class GodotScopeCategoryUiProvider : ScopeCategoryUIProvider
    {
        // Needs to be less than other priorities in R#'s built in ScopeCategoryUIProvider
        // to push it to the end of the list
        private const int Priority = -200;

        public GodotScopeCategoryUiProvider() : base()
        {
            MainPoint = new InGodotCSharpProject();
        }

        public override IEnumerable<ITemplateScopePoint> BuildAllPoints()
        {
            yield return new InGodotCSharpProject();
        }

        public override string CategoryCaption => "Godot";

        public override string Present(ITemplateScopePoint point)
        {
            if (point is InGodotCSharpProject)
                return "In Godot project";
            return base.Present(point);
        }
    }
}