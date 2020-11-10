using System.Diagnostics;
using JetBrains.Application.Processes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.DotNetCore;
using JetBrains.ReSharper.Feature.Services.DebuggerFacade;
using JetBrains.ReSharper.Host.Features.UnitTesting;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.Util;
using JetBrains.Util.Logging;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [UnitTestHostProvider]
    public class GodotRiderDebugHostProvider : RiderDebugHostProvider
    {
        private readonly DotNetCorePlatformSelector myDotNetCorePlatformSelector;
        public GodotRiderDebugHostProvider(IDebuggerFacade debuggerFacade, ILogger logger, DotNetCorePlatformSelector dotNetCorePlatformSelector) : base(debuggerFacade, logger, dotNetCorePlatformSelector)
        {
            myDotNetCorePlatformSelector = dotNetCorePlatformSelector;
        }

        public override ITaskRunnerHostController CreateHostController(IUnitTestLaunch launch)
        {
            return new GodotRiderDebugHostController(launch, myDebuggerFacade, myDotNetCorePlatformSelector, Logger.GetLogger<RiderDebugHostController>());
        }
    }

    public class GodotRiderDebugHostController : RiderDebugHostController
    {
        public GodotRiderDebugHostController(IUnitTestLaunch launch, IDebuggerFacade debuggerFacade, DotNetCorePlatformSelector dotNetCorePlatformSelector, ILogger logger) 
            : base(launch, debuggerFacade, dotNetCorePlatformSelector, logger)
        {
        }

        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, IUnitTestRun run, ILogger logger)
        {
            run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
            var solution = run.Launch.Solution;
            var strategy = solution.GetComponent<GodotUnitTestRunStrategy>();
            GodotTaskRunnerHostController.PatchStartInfoForGodot(startInfo, solution);
            startInfo.EnvironmentVariables.Add("GODOT_MONO_DEBUGGER_AGENT", $"--debugger-agent=transport=dt_socket,address=127.0.0.1:{strategy.DebugPort},server=n,suspend=y");
            var rawProcessInfo = new JetProcessStartInfo(startInfo);
            return new PreparedProcess(rawProcessInfo, logger);
        }

        private FileSystemPath[] GetAssembliesUnderDebug(
            IRuntimeEnvironment runtimeEnvironment)
        {
            if (!(runtimeEnvironment is IModuleRuntimeEnvironment runtimeEnvironment1) || !runtimeEnvironment1.TargetFrameworkId.IsNetCoreApp)
                return EmptyArray<FileSystemPath>.Instance;
            return new FileSystemPath[1]
            {
                runtimeEnvironment1.Project.GetOutputFilePath(runtimeEnvironment1.TargetFrameworkId)
            };
        }
    }
}