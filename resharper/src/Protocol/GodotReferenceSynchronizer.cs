using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol
{
    [SolutionComponent]
    public class GodotReferenceSynchronizer
    {
        public GodotReferenceSynchronizer(Lifetime lifetime, FrontendBackendHost host,
            GodotReferencesTracker referencesTracker)
        {
            referencesTracker.HasGodotReference.Advise(lifetime,
                res => { host.Model.HasGodotReference.SetValue(res); });
        }
    }
}