using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Reflection;
using JetBrains.Application.Processes;
using JetBrains.Collections.Viewable;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Host.Features;
using JetBrains.ReSharper.TestRunner.Abstractions;
using JetBrains.ReSharper.UnitTestFramework.Extensions;
using JetBrains.ReSharper.UnitTestFramework.Processes;
using JetBrains.ReSharper.UnitTestFramework.TestRunner;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    public class GodotTestRunnerHost : DefaultTestRunnerHost
    {
        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, ITestRunnerContext context)
        {
            context.Settings.TestRunner.NoIsolationNetFramework.SetValue(true);
            
            var rawStartInfo = new JetProcessStartInfo(startInfo);
            var patcher = new GodotPatcher(context.RuntimeEnvironment.Project.GetSolution());
            var request = context.RuntimeEnvironment.ToJetProcessRuntimeRequest();
            var patch = new JetProcessStartInfoPatch(patcher, request);
            return new PreparedProcess(rawStartInfo, patch);
        }

        public override IEnumerable<Assembly> InProcessAssemblies => EmptyArray<Assembly>.Instance;
    }

    public class GodotPatcher : IProcessStartInfoPatcher
    {
        private readonly ISolution mySolution;

        public GodotPatcher(ISolution solution)
        {
            mySolution = solution;
        }
        public ProcessStartInfoPatchResult Patch(JetProcessStartInfo startInfo, JetProcessRuntimeRequest request)
        {
            var fileName = startInfo.FileName;
            var args = startInfo.Arguments;
            
            var solutionDir = mySolution.SolutionDirectory.QuoteIfNeeded();
            var model = mySolution.GetProtocolSolution().GetFrontendBackendGodotModel();
            if (model == null)
                throw new InvalidOperationException("Missing connection to frontend.");
            if (!model.GodotPath.HasValue())
                throw new InvalidOperationException("GodotPath is unknown.");
            var godotPath = model.GodotPath.Value.QuoteIfNeeded();

            var patchedInfo = startInfo.Patch(godotPath,
                $"--path {solutionDir} \"res://test_runner/runner.tscn\" --unit_test_assembly \"{fileName}\" --unit_test_args \"{args}\"",
                EnvironmentVariableMutator.Empty);

            return ProcessStartInfoPatchResult.CreateSuccess(startInfo, request, patchedInfo);
        }
    }
}