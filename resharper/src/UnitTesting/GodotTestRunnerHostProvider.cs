using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.UnitTestFramework.TestRunner;
using JetBrains.Util.Dotnet.TargetFrameworkIds;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionComponent]
    public class GodotTestRunnerHostProvider : ITestRunnerHostProvider
    {
        public ITestRunnerHost TryGetHost(IProject project, TargetFrameworkId targetFrameworkId)
        {
            return project.IsGodotProject() ? new GodotTestRunnerHost() : null;
        }
    }
}