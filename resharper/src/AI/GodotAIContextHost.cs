using System.Threading.Tasks;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.DataContext;
using JetBrains.Rd.Tasks;
using JetBrains.ReSharper.Feature.Services.AI.Context;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.Rider.Backend.Features;
using JetBrains.Rider.Model;

namespace JetBrains.ReSharper.Plugins.Godot.AI;

[SolutionComponent(InstantiationEx.LegacyDefault)]
public class GodotAIContextHost
{
    public GodotAIContextHost(SolutionModel solutionModel,
        IChatDataContextProvider chatDataContextProvider)
    {
        var chatModel = solutionModel.GetCurrentSolution().GetAIChatModel();
        chatModel.GodotContext.SetAsync((lt, _) =>
        {
            using (ReadLockCookie.Create())
            {
                var dataContext = chatDataContextProvider.GetDataContext(lt);
                var solution = dataContext.GetData(ProjectModelDataConstants.SOLUTION);
                var tracker = solution?.TryGetComponent<GodotTracker>();
                if (tracker?.GodotDescriptor == null)
                    return Task.FromResult(new GodotContextResult(false, false, null));

                var isPureGdScriptProject = tracker.GodotDescriptor.IsPureGdScriptProject;
                if (tracker.MainProject == null)
                    return Task.FromResult(new GodotContextResult(true, isPureGdScriptProject, null));
                
                var sdk = tracker.MainProject.ProjectProperties.DotNetCorePlatform?.Sdk;
                return Task.FromResult(new GodotContextResult(true, isPureGdScriptProject, sdk));
            }
        });
    }
}