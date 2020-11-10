using System.Diagnostics;
using System.Threading;
using JetBrains.Application.Threading;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.DotNetCore;
using JetBrains.ReSharper.Feature.Services.DebuggerFacade;
using JetBrains.ReSharper.Host.Features.UnitTesting;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
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
            StartDebuggerServer(run, startInfo);
            
            return base.PrepareDebugSessionDescriptor(run, startInfo);
        }

        private void StartDebuggerServer(IUnitTestRun run, ProcessStartInfo startInfo)
        {
            run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
            var solution = run.Launch.Solution;
            GodotTaskRunnerHostController.PatchStartInfoForGodot(startInfo, solution);
            var model = solution.GetComponent<FrontendBackendHost>().Model;
            var waitingLifetimeDefinition = Lifetime.Define(run.Lifetime);
            var waitingLifetime = waitingLifetimeDefinition.Lifetime;
            solution.Locks.ExecuteOrQueueEx(waitingLifetime, "myRiderSolutionSaver.Save", () =>
            {
                var task = model.StartDebuggerServer.Start(waitingLifetime, Unit.Instance);
                task.Result.Advise(waitingLifetime, result =>
                {
                    startInfo.EnvironmentVariables.Add("GODOT_MONO_DEBUGGER_AGENT", $"--debugger-agent=transport=dt_socket,address=127.0.0.1:{result.Result},server=n,suspend=y");
                    waitingLifetimeDefinition.Terminate();
                });
            });
            while (waitingLifetime.IsAlive)
            {
                Thread.Sleep(100);
            }
        }
    }
}