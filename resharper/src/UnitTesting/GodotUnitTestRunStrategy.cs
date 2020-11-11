using System;
using System.Threading.Tasks;
using JetBrains.Application.Threading;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
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

        public int DebugPort;

        public GodotUnitTestRunStrategy(ISolution solution)
        {
            myNUnitTestRunnerRunStrategy = solution.GetComponent<NUnitTestRunnerRunStrategy>();
        }

        public Task Run(IUnitTestRun run)
        {
            var tcs = new TaskCompletionSource<bool>();
            var taskLifetimeDef = Lifetime.Define(run.Lifetime);
            taskLifetimeDef.SynchronizeWith(tcs);
            var taskLifetime = taskLifetimeDef.Lifetime;
            var solution = run.Launch.Solution;
            var model = solution.GetComponent<FrontendBackendHost>();
            
            var hostId = run.HostController.HostId;
            switch (hostId)
            {
                case WellKnownHostProvidersIds.DebugProviderId:
                    solution.Locks.ExecuteOrQueueEx(taskLifetime, "AttachDebuggerToUnityEditor", () =>
                    {
                        if (!taskLifetime.IsAlive || model.Model == null)
                        {
                            tcs.TrySetCanceled();
                            return;
                        }

                        var task = model.Model.StartDebuggerServer.Start(taskLifetime, Unit.Instance);
                        task.Result.Advise(taskLifetime, result =>
                        {
                            if (!run.Lifetime.IsAlive)
                                tcs.TrySetCanceled();
                            else if (result.Result <= 0)
                                tcs.SetException(new Exception("Unable to start debugger."));
                            else
                            {
                                DebugPort = result.Result;
                                tcs.SetResult(true);
                            }
                        });
                    });
                    break;
                default:
                    tcs.SetResult(true);
                    break;
            }

            return tcs.Task.ContinueWith(_ => myNUnitTestRunnerRunStrategy.Run(run)).Unwrap();
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