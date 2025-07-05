using System;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Impl;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.UnitTestFramework.Execution.TestRunner;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util.Dotnet.TargetFrameworkIds;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionComponent(Instantiation.DemandAnyThreadSafe)]
    public class GodotTestRunnerHostProvider : ITestRunnerHostProvider
    {
        public GodotTestRunnerHostProvider(ISolution solution)
        {
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel(); // do not remove - would brake calling the same in GodotPatcher
        }

        public ITestRunnerHost TryGetHost(IProject project, TargetFrameworkId targetFrameworkId)
        {
            if (!project.IsGodotProject2()) return null;
            if (project.GetModuleReference("gdUnit4Api") != null) return GodotGdUnitTestRunnerHost.Instance;
            var assemblyNameVersion = (project.GetModuleReference("GodotSharp") as ProjectToAssemblyReference)?.ReferenceTarget.AssemblyName.Version;
            if (assemblyNameVersion != null && project.IsGodotProject() && assemblyNameVersion.Major >= 4) return GodotCoreTestRunnerHost.Instance;
            return project.IsGodotProject() ? GodotTestRunnerHost.Instance : null;
        }
    }
}