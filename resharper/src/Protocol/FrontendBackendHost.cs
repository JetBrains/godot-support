using JetBrains.Annotations;
using JetBrains.Application.Threading;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ReSharper.Host.Features;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol
{
    [SolutionComponent]
    public class FrontendBackendHost
    {
        // This will only ever be null when running tests. The value does not change for the lifetime of the solution.
        // Prefer using this field over calling GetFrontendBackendModel(), as that method will throw in tests
        [CanBeNull] public readonly FrontendBackendModel Model;

        public FrontendBackendHost(Lifetime lifetime, ISolution solution, IShellLocks shellLocks)
        {
            // This will throw in tests, as GetProtocolSolution will return null
            var model = solution.GetProtocolSolution().GetFrontendBackendModel();
            Model = model;
        }
    }
}