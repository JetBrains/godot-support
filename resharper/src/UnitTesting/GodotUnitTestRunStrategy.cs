using System.Threading.Tasks;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.ReSharper.UnitTestFramework.Strategy;
using JetBrains.ReSharper.UnitTestProvider.nUnit.v30;
using JetBrains.Util.Dotnet.TargetFrameworkIds;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionComponent]
    public class GodotUnitTestRunStrategy : IUnitTestRunStrategy
    {
        private readonly NUnitTestRunnerRunStrategy myNUnitTestRunnerRunStrategy;
        public bool RequiresProjectBuild(IProject project) { return true; }
        public bool RequiresProjectExplorationAfterBuild(IProject project) { return true; }
        public bool RequiresProjectPropertiesRefreshBeforeLaunch() { return true; }

        public IRuntimeEnvironment GetRuntimeEnvironment(IUnitTestLaunch launch, IProject project, TargetFrameworkId targetFrameworkId, IUnitTestElement element)
        {
            return myNUnitTestRunnerRunStrategy.GetRuntimeEnvironment(launch, project, targetFrameworkId, element);
        }
        
        public GodotUnitTestRunStrategy(ISolution solution)
        {
            myNUnitTestRunnerRunStrategy = solution.GetComponent<NUnitTestRunnerRunStrategy>();
        }

        public Task Run(IUnitTestRun run)
        {
            return myNUnitTestRunnerRunStrategy.Run(run);
        }

        public void Cancel(IUnitTestRun run)
        {
           myNUnitTestRunnerRunStrategy.Cancel(run);
        }

        public void Abort(IUnitTestRun run)
        { 
            myNUnitTestRunnerRunStrategy.Abort(run);
        }
    }
}