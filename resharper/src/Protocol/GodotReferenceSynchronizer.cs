using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Host.Features;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol
{
    [SolutionComponent]
    public class GodotReferenceSynchronizer
    {
        public GodotReferenceSynchronizer(Lifetime lifetime, ISolution solution, GodotReferencesTracker referencesTracker)
        {
            var model = solution.GetProtocolSolution().GetFrontendBackendModel();
            referencesTracker.HasGodotReference.Advise(lifetime,
                res => { model.HasGodotReference.SetValue(res); });
        }
    }
}