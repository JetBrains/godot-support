using System.Collections.Generic;
using System.Diagnostics;
using System.Reflection;
using JetBrains.Annotations;
using JetBrains.Collections.Viewable;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.ReSharper.UnitTestFramework.Execution.TestRunner;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    public class GodotGdUnitTestRunnerHost : DefaultTestRunnerHost
    {
        [NotNull] public new static readonly ITestRunnerHost Instance = new GodotGdUnitTestRunnerHost();
        private const string godotPathEnvVarName = "GODOT_BIN";

        public override IPreparedProcess StartProcess(ProcessStartInfo startInfo, ITestRunnerContext context)
        {
            var solution = context.RuntimeEnvironment.Project.GetSolution();
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            if (model.GodotPath.HasValue()) 
                startInfo.EnvironmentVariables.Add(godotPathEnvVarName, model.GodotPath.Value);

            return base.StartProcess(startInfo, context);
        }
    }
}