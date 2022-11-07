using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.RdBackend.Common.Features;
using JetBrains.ReSharper.UnitTestFramework.Execution.TestRunner;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    public class GodotCoreTestRunnerHost : DefaultTestRunnerHost
    {
        [NotNull] public new static readonly ITestRunnerHost Instance = new GodotCoreTestRunnerHost();
        private const string pluginDirectory = "RiderTestRunner";
        private const string runnerScene = "NetCoreRunner.tscn";

        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, ITestRunnerContext context)
        {
            var solution = context.RuntimeEnvironment.Project.GetSolution();
            var solutionDirectory = solution.SolutionDirectory;
            var scenePaths = solutionDirectory.GetChildDirectories(pluginDirectory,
                PathSearchFlags.ExcludeFiles | PathSearchFlags.RecurseIntoSubdirectories).Select(a=>a.Combine(runnerScene)).Where(a => a.ExistsFile).ToArray();
            if (!scenePaths.Any())
                throw new Exception("Please manually put folder with files from https://github.com/van800/godot-demo-projects/tree/net6/mono/dodge_the_creeps/RiderTestRunner to your project.");
            if (scenePaths.Length > 1)
                throw new Exception($"Make sure you have only 1 {pluginDirectory}/{runnerScene} in your project.");
            
            var args = CommandLineUtil.ToArray(startInfo.Arguments);
            
            var testRunnerItem = args.Select((item, i) => new { Item = item, Index = i })
                .First(x =>
                {
                    var possiblePathToTestRunner = FileSystemPath.TryParse(x.Item);
                    return possiblePathToTestRunner.IsAbsolute &&
                           possiblePathToTestRunner.NameWithoutExtension.StartsWith("ReSharperTestRunner") &&
                           possiblePathToTestRunner.ExtensionNoDot == "dll";
                });

            var fileName = testRunnerItem.Item;

            var usefulArgs = CommandLineUtil.ToString(args.Skip(testRunnerItem.Index + 1));

            var solutionDir = solution.SolutionDirectory;
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();

            if (model == null)
                throw new InvalidOperationException("Missing connection to frontend.");
            if (!model.GodotPath.HasValue())
                throw new InvalidOperationException("GodotPath is unknown.");
            var godotPath = model.GodotPath.Value;

            var sceneRelPath = scenePaths.Single().MakeRelativeTo(solutionDirectory);
            startInfo.FileName = godotPath;
            startInfo.Arguments =
                $"--path \"{solutionDir}\" \"res://{sceneRelPath}\" --unit_test_assembly \"{fileName}\" --unit_test_args \"{usefulArgs}\"";


            if (context is ITestRunnerExecutionContext executionContext)
            {
                return executionContext.Run.HostController.StartProcess(startInfo, executionContext.Run, context.Logger);
            }

            return base.StartProcess(startInfo, context);
        }

        public override IEnumerable<Assembly> InProcessAssemblies => EmptyArray<Assembly>.Instance;

    }
}