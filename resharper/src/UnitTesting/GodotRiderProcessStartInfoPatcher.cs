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
        private readonly ISolution mySolution;

        public GodotRiderProcessStartInfoPatcher(ILogger logger, ISolutionToolset solutionToolset, RiderProcessStartInfoEnvironment environment, ISolution solution) : base(logger, solutionToolset, environment)
        {
            mySolution = solution;
        }

        public override ProcessStartInfoPatchResult Patch(JetProcessStartInfo info, JetProcessRuntimeRequest request)
        {
            if (mySolution.GetProtocolSolution().GetGodotFrontendBackendModel().IsNet6Plus.HasTrueValue()
                && mySolution.GetProtocolSolution().GetGodotFrontendBackendModel().GodotPath.Value == info.FileName)
                return base.Patch(new JetProcessStartInfo(info.FileName, info.Arguments, info.WorkingDirectory, info.ToProcessStartInfo().Environment), 
                    JetProcessRuntimeRequest.CreateDirect(request.EnvironmentVariableMutator));
            return base.Patch(info, request);
        }
        
    }
}