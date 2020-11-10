using JetBrains.Application.Processes;
using JetBrains.ProjectModel;
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
            var fileName = startInfo.FileName;
            var args = startInfo.Arguments;

            var solution = run.Launch.Solution;
            var solutionDir = solution.SolutionDirectory.QuoteIfNeeded();
            
            startInfo.FileName = "/home/ivan-shakhov/Downloads/Godot_v3.2.3-stable_mono_x11_64/Godot_v3.2.3-stable_mono_x11.64";
            startInfo.Arguments = $"--path {solutionDir} --unit_test_assembly \"{fileName}\" --unit_test_args \"{args}\"";
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