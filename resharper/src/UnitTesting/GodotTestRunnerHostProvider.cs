using JetBrains.ProjectModel;
using JetBrains.ReSharper.Host.Features;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.UnitTestFramework.TestRunner;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util.Dotnet.TargetFrameworkIds;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionComponent]
    public class GodotTestRunnerHostProvider : ITestRunnerHostProvider
    {
        public GodotTestRunnerHostProvider(ISolution solution)
        {
            var model = solution.GetProtocolSolution().GetFrontendBackendGodotModel(); // do not remove - would brake calling the same in GodotPatcher
        }
        
        public ITestRunnerHost TryGetHost(IProject project, TargetFrameworkId targetFrameworkId)
        {
            return project.IsGodotProject() ? GodotTestRunnerHost.Instance : null;
        }
    }
}