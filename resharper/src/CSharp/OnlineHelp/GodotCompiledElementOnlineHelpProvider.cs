using System;
using JetBrains.Annotations;
using JetBrains.Application;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Feature.Services.OnlineHelp;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.Modules;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.OnlineHelp
{
    [ShellComponent]
    public class GodotCompiledElementOnlineHelpProvider : CompiledElementOnlineHelpProvider
    {
        public override string GetPresentableName(IDeclaredElement element)
        {
            if (!(element is ICompiledElement compiledElement)) 
                return base.GetPresentableName(element);
            
            if (!IsGodotCompiledCode(compiledElement)) return base.GetPresentableName(element);
            
            return base.GetPresentableName(element);
        }

        public override Uri GetUrl(ICompiledElement element)
        {
            if (!IsGodotCompiledCode(element)) return null;

            var searchableText = element.GetSearchableText();
            if (searchableText == null) return null;

            return GetUri(searchableText);
        }

        private static bool IsGodotCompiledCode(ICompiledElement element)
        {
            var tracker = element.GetSolution().GetComponent<GodotTracker>();
            if (!tracker.IsGodot) return false;
            if (element.Module is not IAssemblyPsiModule module) return false;
            var assemblyLocation = module.Assembly.Location;
            if (assemblyLocation?.AssemblyPhysicalPath?.ExistsFile != true)
                return false;

            if (!assemblyLocation.Name.Equals("GodotSharp.dll", StringComparison.OrdinalIgnoreCase))
                return false;
            return true;
        }
        
        [NotNull]
        private Uri GetUri([NotNull] string keyword)
        {
            var onlineKeyword = keyword.Replace(".#", ".").Replace(".-", ".");

            var url =
                $"https://docs.godotengine.org/en/stable/search.html?q={onlineKeyword}&check_keywords=yes&area=default";
            
            return new Uri(url);
        }
        
        // setting this to be more preferable then MsdnOnlineHelpProvider
        public override int Priority => 7; 
        public override bool ShouldValidate => false;
    }
}