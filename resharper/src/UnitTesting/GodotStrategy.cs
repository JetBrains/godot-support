using System;
using System.Threading.Tasks;
using JetBrains.Metadata.Access;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Plugins.Godot.Protocol.BackendGodot;
using JetBrains.ReSharper.UnitTestFramework;
using JetBrains.ReSharper.UnitTestFramework.Launch;
using JetBrains.ReSharper.UnitTestFramework.Strategy;
using JetBrains.Util.Dotnet.TargetFrameworkIds;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionComponent]
    public class GodotStrategy : IUnitTestRunStrategy
    {
        private readonly BackendGodotHost myBackendGodotHost;
        public bool RequiresProjectBuild(IProject project) { return true; }
        public bool RequiresProjectExplorationAfterBuild(IProject project) { return true; }
        public bool RequiresProjectPropertiesRefreshBeforeLaunch() { return true; }

        public GodotStrategy(BackendGodotHost backendGodotHost)
        {
            myBackendGodotHost = backendGodotHost;
        }
        
        public IRuntimeEnvironment GetRuntimeEnvironment(IUnitTestLaunch launch, IProject project, TargetFrameworkId targetFrameworkId, IUnitTestElement element)
        {
            var targetPlatform = TargetPlatformCalculator.GetTargetPlatform(launch, project, targetFrameworkId);
            return new GodotRuntimeEnvironment(targetPlatform, project);
        }

        public Task Run(IUnitTestRun run)
        {
            // https://docs.microsoft.com/en-us/dotnet/api/system.appdomain.executeassembly?view=netframework-4.7.2
            // /home/ivan-shakhov/Work/dotnet-products/Bin.ReSharperHost/ReSharperTestRunner64c.exe
            // myBackendGodotHost.BackendGodotModel.Value.
            return Task.FromResult(true);
        }

        public void Cancel(IUnitTestRun run)
        {
           // throw new NotImplementedException();
        }

        public void Abort(IUnitTestRun run)
        { 
            // throw new NotImplementedException();
        }
        
        private class GodotRuntimeEnvironment : IRuntimeEnvironment
        {
            public GodotRuntimeEnvironment(TargetPlatform targetPlatform, IProject project)
            {
                TargetPlatform = targetPlatform;
                Project = project;
            }

            public TargetPlatform TargetPlatform { get; }
            public IProject Project { get; }

            private bool Equals(GodotRuntimeEnvironment other)
            {
                return TargetPlatform == other.TargetPlatform && Equals(Project, other.Project);
            }

            public override bool Equals(object obj)
            {
                if (ReferenceEquals(null, obj)) return false;
                if (ReferenceEquals(this, obj)) return true;
                if (obj.GetType() != GetType()) return false;
                return Equals((GodotRuntimeEnvironment) obj);
            }

            public override int GetHashCode()
            {
                return (int) TargetPlatform;
            }

            public static bool operator ==(GodotRuntimeEnvironment left, GodotRuntimeEnvironment right)
            {
                return Equals(left, right);
            }

            public static bool operator !=(GodotRuntimeEnvironment left, GodotRuntimeEnvironment right)
            {
                return !Equals(left, right);
            }
        }
    }

}