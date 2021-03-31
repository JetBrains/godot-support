using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Reflection;
using System.Threading.Tasks;
using JetBrains.Annotations;
using JetBrains.Application.Processes;
using JetBrains.Application.Threading;
using JetBrains.Collections.Viewable;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Host.Features;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Extensions;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.ReSharper.UnitTestFramework.TestRunner;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    public class GodotTestRunnerHost : DefaultTestRunnerHost
    {
        [NotNull] public new static readonly ITestRunnerHost Instance = new GodotTestRunnerHost();
        private int myDebugPort;
        private const string pluginDirectory = "RiderTestRunner";
        private const string runnerScene = "Runner.tscn";

        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, ITestRunnerContext context)
        {
            var solution = context.RuntimeEnvironment.Project.GetSolution();
            var solutionDirectory = solution.SolutionDirectory;
            if (!solutionDirectory.Combine(pluginDirectory).ExistsDirectory || !solutionDirectory.Combine(pluginDirectory).Combine(runnerScene).ExistsFile)
                throw new Exception("Please manually put folder with files from https://github.com/van800/godot-demo-projects/tree/nunit/mono/dodge_the_creeps/RiderTestRunner to your project.");
            
            context.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);

            if (context is ITestRunnerExecutionContext executionContext &&
                executionContext.Run.HostController.HostId == WellKnownHostProvidersIds.DebugProviderId)
            {
                PrepareDebuggerServer(executionContext.Run).Wait();
                startInfo.EnvironmentVariables.Add("GODOT_MONO_DEBUGGER_AGENT",
                    $"--debugger-agent=transport=dt_socket,address=127.0.0.1:{myDebugPort},server=n,suspend=y");
            }

            var rawStartInfo = new JetProcessStartInfo(startInfo);
            var patcher = new GodotPatcher(solution);
            var request = context.RuntimeEnvironment.ToJetProcessRuntimeRequest();
            var patch = new JetProcessStartInfoPatch(patcher, request);
            return new PreparedProcess(rawStartInfo, patch);
        }

        public override IEnumerable<Assembly> InProcessAssemblies => EmptyArray<Assembly>.Instance;

        private Task PrepareDebuggerServer(IUnitTestRun run)
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

            return tcs.Task;
        }

        private class GodotPatcher : IProcessStartInfoPatcher
        {
            private readonly ISolution mySolution;
            private readonly FrontendBackendGodotModel myModel;

            public GodotPatcher(ISolution solution)
            {
                mySolution = solution;
                myModel = mySolution.GetProtocolSolution().GetFrontendBackendGodotModel();
            }

            public ProcessStartInfoPatchResult Patch(JetProcessStartInfo startInfo, JetProcessRuntimeRequest request)
            {
                var fileName = startInfo.FileName;
                var args = startInfo.Arguments;

                var solutionDir = mySolution.SolutionDirectory.QuoteIfNeeded();

                if (myModel == null)
                    throw new InvalidOperationException("Missing connection to frontend.");
                if (!myModel.GodotPath.HasValue())
                    throw new InvalidOperationException("GodotPath is unknown.");
                var godotPath = myModel.GodotPath.Value.QuoteIfNeeded();

                var patchedInfo = startInfo.Patch(godotPath,
                    $"--path {solutionDir} \"res://{pluginDirectory}/Runner.tscn\" --unit_test_assembly \"{fileName}\" --unit_test_args \"{args}\"",
                    EnvironmentVariableMutator.Empty);

                return ProcessStartInfoPatchResult.CreateSuccess(startInfo, request, patchedInfo);
            }
        }
    }
}