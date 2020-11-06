using JetBrains.Annotations;
using JetBrains.Application.Threading;
using JetBrains.Application.UI.Components;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.Protocol.BackendGodot
{
    [SolutionComponent]
    public class BackendGodotHost
    {
        private readonly JetBrains.Application.ActivityTrackingNew.UsageStatistics myUsageStatistics;

        // The property value will be null when the backend/Godot protocol is not available
        [NotNull]
        public readonly ViewableProperty<BackendGodotModel> BackendGodotModel = new ViewableProperty<BackendGodotModel>(null);

        public BackendGodotHost(Lifetime lifetime, ILogger logger,
                                FrontendBackendHost frontendBackendHost,
                                IThreading threading, IIsApplicationActiveState isApplicationActiveState,
                                JetBrains.Application.ActivityTrackingNew.UsageStatistics usageStatistics)
        {
            myUsageStatistics = usageStatistics;
        }
    }
}