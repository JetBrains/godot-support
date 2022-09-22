using JetBrains.Application.Processes;
using JetBrains.Collections.Viewable;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;
using JetBrains.RdBackend.Common.Features.Processes;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.UnitTesting
{
    [SolutionInstanceComponent]
    public class GodotRiderProcessStartInfoPatcher : RiderProcessStartInfoPatcher
    {
        private IViewableProperty<bool> MyIsNet6Property;
        private IViewableProperty<string> myGodotPathProperty;

        public GodotRiderProcessStartInfoPatcher(ILogger logger, ISolutionToolset solutionToolset, RiderProcessStartInfoEnvironment environment, ISolution solution) : base(logger, solutionToolset, environment)
        {
            MyIsNet6Property = solution.GetProtocolSolution().GetGodotFrontendBackendModel().IsNet6Plus;
            myGodotPathProperty = solution.GetProtocolSolution().GetGodotFrontendBackendModel().GodotPath;
        }

        public override ProcessStartInfoPatchResult Patch(JetProcessStartInfo info, JetProcessRuntimeRequest request)
        {
            if (MyIsNet6Property.HasTrueValue() && myGodotPathProperty.Value == info.FileName)
                return base.Patch(new JetProcessStartInfo(info.FileName, info.Arguments, info.WorkingDirectory, info.ToProcessStartInfo().Environment), 
                    JetProcessRuntimeRequest.CreateDirect(request.EnvironmentVariableMutator));
            return base.Patch(info, request);
        }
        
    }
}