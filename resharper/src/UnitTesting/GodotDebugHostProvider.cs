using System.Diagnostics;
using JetBrains.ProjectModel.DotNetCore;
using JetBrains.ReSharper.Feature.Services.DebuggerFacade;
using JetBrains.ReSharper.Host.Features.UnitTesting;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
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
        public GodotRiderDebugHostController(IUnitTestLaunch launch, IDebuggerFacade debuggerFacade, DotNetCorePlatformSelector dotNetCorePlatformSelector, ILogger logger) : base(launch, debuggerFacade, dotNetCorePlatformSelector, logger)
        {
        }
        
        protected override DebugSessionDescriptor PrepareDebugSessionDescriptor(IUnitTestRun run, ProcessStartInfo startInfo)
        {
            run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
            GodotTaskRunnerHostController.PatchStartInfoForGodot(startInfo, run);
            startInfo.EnvironmentVariables.Add("GODOT_MONO_DEBUGGER_AGENT", "--debugger-agent=transport=dt_socket,address=127.0.0.1:23685,server=n,suspend=y");

            return base.PrepareDebugSessionDescriptor(run, startInfo);
        }
    }
    //
    // public class GodotDebugHostController : RiderDebugHostProvider
    // {
    //     public override ITaskRunnerHostController CreateHostController(IUnitTestLaunch launch)
    //     {
    //         return base.CreateHostController(launch);
    //     }
    //
    //     public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, IUnitTestRun run)
    //     {
    //         run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
    //         var godotProcessStartInfo = new ProcessStartInfo("/home/ivan-shakhov/Downloads/Godot_v3.2.3-stable_mono_x11_64/Godot_v3.2.3-stable_mono_x11.64");
    //         godotProcessStartInfo.Arguments = $"--path \"/home/ivan-shakhov/Work/godot-demo-projects/mono/dodge_the_creeps\" --unit_test_assembly \"{startInfo.FileName}\" --unit_test_args \"{startInfo.Arguments}\"";
    //
    //         var descriptor = PrepareDebugSessionDescriptor(run, godotProcessStartInfo);
    //         var session = myDebuggerFacade.RunDebuggerSession(run.Launch.Lifetime, "Unit-Testing debug session", run.Launch.Solution, descriptor, () => { });
    //
    //         return PreparedProcess.FromExistingProcess(session.ProcessId.Value, godotProcessStartInfo, logger);
    //     }
    //
    //     public GodotDebugHostController(IDebuggerFacade debuggerFacade, ILogger logger, DotNetCorePlatformSelector dotNetCorePlatformSelector) : base(debuggerFacade, logger, dotNetCorePlatformSelector)
    //     {
    //     }
    // }
}