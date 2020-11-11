using System;
using JetBrains.Application.Processes;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.Util.Logging;
using System.Diagnostics;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Host.Features;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel.Flavours;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

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
        
        public static void PatchStartInfoForGodot(ProcessStartInfo startInfo, ISolution solution)
        {
            var fileName = startInfo.FileName;
            var args = startInfo.Arguments;
            
            var solutionDir = solution.SolutionDirectory.QuoteIfNeeded();
            var model = solution.GetProtocolSolution().GetFrontendBackendModel();
            if (model == null)
                throw new InvalidOperationException("Missing connection to frontend.");
            if (!model.GodotPath.HasValue())
                throw new InvalidOperationException("GodotPath is unknown.");
            var godotPath = model.GodotPath.Value.QuoteIfNeeded();

            startInfo.FileName = godotPath;
            startInfo.Arguments = $"--path {solutionDir} \"res://test_runner/runner.tscn\" --unit_test_assembly \"{fileName}\" --unit_test_args \"{args}\"";
        }
    }
    
    public class GodotTaskRunnerHostController : RunHostController
    {
        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, IUnitTestRun run, ILogger logger)
        {
            if (run.RuntimeEnvironment is IModuleRuntimeEnvironment environment && environment.Project.HasFlavour<GodotProjectFlavor>())
            {
                run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
                var solution = run.Launch.Solution;
                GodotRunHostProvider.PatchStartInfoForGodot(startInfo, solution);
                var rawProcessInfo = new JetProcessStartInfo(startInfo);
                return new PreparedProcess(rawProcessInfo, logger);
            }
            return base.StartProcess(startInfo, run, logger);
        }

        public GodotTaskRunnerHostController([NotNull] IUnitTestLaunch launch, [NotNull] ISolutionProcessStartInfoPatcher processStartInfoPatcher, [NotNull] ILogger logger) 
            : base(launch, processStartInfoPatcher, logger)
        {
        }
    }
}