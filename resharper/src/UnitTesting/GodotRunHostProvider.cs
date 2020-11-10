using System;
using JetBrains.Application.Processes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.Util.Logging;
using System.Diagnostics;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
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
            PatchStartInfoForGodot(startInfo, run);
            var rawProcessInfo = new JetProcessStartInfo(startInfo);
            return new PreparedProcess(rawProcessInfo, logger);
        }

        internal static void PatchStartInfoForGodot(ProcessStartInfo startInfo, IUnitTestRun run)
        {
            var fileName = startInfo.FileName;
            var args = startInfo.Arguments;

            var solution = run.Launch.Solution;
            var solutionDir = solution.SolutionDirectory.QuoteIfNeeded();
            var model = solution.GetComponent<FrontendBackendHost>().Model;
            if (model == null)
                throw new InvalidOperationException("Missing connection to frontend.");
            if (!model.GodotPath.HasValue())
                throw new InvalidOperationException("GodotPath is unknown.");
            var godotPath = model.GodotPath.Value.QuoteIfNeeded();

            startInfo.FileName = godotPath;
            startInfo.Arguments = $"--path {solutionDir} --unit_test_assembly \"{fileName}\" --unit_test_args \"{args}\"";
        }

        public override string HostId => "GodotProcess";

        public GodotTaskRunnerHostController(
            [NotNull] IUnitTestLaunch launch,
            [NotNull] ILogger logger
        ) : base(logger, launch) { }
    }
}