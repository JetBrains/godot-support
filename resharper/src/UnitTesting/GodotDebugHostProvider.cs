using System;
using System.Diagnostics;
using System.Threading.Tasks;
using JetBrains.Application.Processes;
using JetBrains.Application.Threading;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel.DotNetCore;
using JetBrains.ReSharper.Feature.Services.DebuggerFacade;
using JetBrains.ReSharper.Host.Features;
using JetBrains.ReSharper.Host.Features.UnitTesting;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel.Flavours;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.Rider.Model.Godot.FrontendBackend;
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
        private int myDebugPort;
        public GodotRiderDebugHostController(IUnitTestLaunch launch, IDebuggerFacade debuggerFacade, DotNetCorePlatformSelector dotNetCorePlatformSelector, ILogger logger) 
            : base(launch, debuggerFacade, dotNetCorePlatformSelector, logger)
        {
        }

        protected override Task PrepareForRunCore(IUnitTestRun run)
        {
            if (run.RuntimeEnvironment is IModuleRuntimeEnvironment environment && environment.Project.HasFlavour<GodotProjectFlavor>())
            {
                
                var tcs = new TaskCompletionSource<bool>();
                var taskLifetimeDef = Lifetime.Define(run.Lifetime);
                taskLifetimeDef.SynchronizeWith(tcs);
                var taskLifetime = taskLifetimeDef.Lifetime;

                var solution = run.Launch.Solution;
                var model = solution.GetProtocolSolution().GetFrontendBackendGodotModel();
                solution.Locks.ExecuteOrQueueEx(taskLifetime, "AttachDebuggerToUnityEditor", () =>
                {
                    if (!taskLifetime.IsAlive || model == null)
                    {
                        tcs.TrySetCanceled();
                        return;
                    }

                    var task = model.StartDebuggerServer.Start(taskLifetime, Unit.Instance);
                    task.Result.Advise(taskLifetime, result =>
                    {
                        if (!run.Lifetime.IsAlive)
                            tcs.TrySetCanceled();
                        else if (result.Result <= 0)
                            tcs.SetException(new Exception("Unable to start debugger."));
                        else
                        {
                            myDebugPort = result.Result;
                            tcs.SetResult(true);
                        }
                    });
                });

                return  tcs.Task.ContinueWith(_ => base.PrepareForRunCore(run)).Unwrap();
            }
            
            
            return base.PrepareForRunCore(run);
        }

        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, IUnitTestRun run, ILogger logger)
        {
            if (run.RuntimeEnvironment is IModuleRuntimeEnvironment environment && environment.Project.HasFlavour<GodotProjectFlavor>())
            {
                            run.Launch.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
                            var solution = run.Launch.Solution;
                            GodotRunHostProvider.PatchStartInfoForGodot(startInfo, solution);
                            startInfo.EnvironmentVariables.Add("GODOT_MONO_DEBUGGER_AGENT", $"--debugger-agent=transport=dt_socket,address=127.0.0.1:{myDebugPort},server=n,suspend=y");
                            var rawProcessInfo = new JetProcessStartInfo(startInfo);
                            return new PreparedProcess(rawProcessInfo, logger);
            }    
            
            return base.StartProcess(startInfo, run, logger);
        }
    }
}