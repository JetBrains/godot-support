using System;
using JetBrains.Annotations;
using JetBrains.Application.Threading;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol
{
    [SolutionComponent]
    public class FrontendBackendHost
    {
        private readonly bool myIsInTests;

        // This will only ever be null when running tests. The value does not change for the lifetime of the solution.
        // Prefer using this field over calling GetFrontendBackendModel(), as that method will throw in tests
        [CanBeNull] public readonly GodotFrontendBackendModel Model;

        public FrontendBackendHost(Lifetime lifetime, ISolution solution, IShellLocks shellLocks,
            bool isInTests = false)
        {
            myIsInTests = isInTests;
            if (myIsInTests)
                return;

            // This will throw in tests, as GetProtocolSolution will return null
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            Model = model;
        }

        public bool IsAvailable => !myIsInTests && Model != null;

        // Convenience method to fire and forget an action on the model (e.g. set a value, fire a signal, etc). Fire and
        // forget means it's safe to use during testing, when there won't be a frontend model available, and Model will
        // be null.
        // There is not a Do that takes in a Func to return a value, as that cannot be called reliably in tests. Use
        // Model directly in this case, check for null and do whatever is appropriate for the callsite.
        public void Do(Action<GodotFrontendBackendModel> action)
        {
            if (myIsInTests)
                return;

            action(Model);
        }
    }
}