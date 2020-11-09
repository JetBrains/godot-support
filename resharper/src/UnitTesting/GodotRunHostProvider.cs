using JetBrains.Application.Processes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.Util.Logging;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [UnitTestHostProvider]
    public class GodotRunHostProvider : RunHostProvider
    {
        public override ITaskRunnerHostController CreateHostController(IUnitTestLaunch launch)
        {
            return new GodotTaskRunnerHostController(launch,
                launch.Solution.GetComponent<ISolutionProcessStartInfoPatcher>(),
                Logger.GetLogger<RunHostController>());
        }
    }
}