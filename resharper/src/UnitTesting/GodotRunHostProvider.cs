using JetBrains.Application.Processes;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.Util.Logging;
using System.Diagnostics;
using JetBrains.Annotations;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [UnitTestHostProvider]
    public class GodotRunHostProvider : RunHostProvider
    {
        public override ITaskRunnerHostController CreateHostController(IUnitTestLaunch launch)
        {
            return new GodotTaskRunnerHostController(launch,
                Logger.GetLogger<RunHostController>());
        }
    }
    
    public class GodotTaskRunnerHostController : TaskRunnerHostControllerBase
    {
        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, IUnitTestRun run, ILogger logger)
        {
            run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
            var solution = run.Launch.Solution;
            GodotUnitTestRunStrategy.PatchStartInfoForGodot(startInfo, solution);
            var rawProcessInfo = new JetProcessStartInfo(startInfo);
            return new PreparedProcess(rawProcessInfo, logger);
        }

        public override string HostId => "GodotProcess";

        public GodotTaskRunnerHostController(
            [NotNull] IUnitTestLaunch launch,
            [NotNull] ILogger logger
        ) : base(logger, launch) { }
    }
}