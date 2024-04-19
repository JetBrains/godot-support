using System.Linq;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Tasks;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent]
    public class GodotTracker
    {
        public VirtualFileSystemPath MainProjectBasePath { get; private set; }
        
        public IProject MainProject { get; private set; }
        public GodotDescriptor GodotDescriptor { get; private set; }
        public GodotTracker(ISolution solution, ILogger logger, ISolutionLoadTasksScheduler tasksScheduler)
        {
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            if (solution.SolutionFilePath.IsEmpty && solution.SolutionDirectory.Combine("project.godot").ExistsFile)
            {
                GodotDescriptor = new GodotDescriptor(true, solution.SolutionDirectory.FullPath);
                model.GodotDescriptor.SetValue(GodotDescriptor);
                return;
            }
            
            tasksScheduler.EnqueueTask(new SolutionLoadTask(GetType(),
                SolutionLoadTaskKinds.Done,
                () =>
                {
                    foreach (var project in solution.GetAllProjects().Where(project => project.IsGodotProject()))
                    {
                        var file = project.Location.Combine("project.godot");
                        if (!file.ExistsFile) continue;
                        MainProjectBasePath = file.Directory;
                        MainProject = project;
                        logger.Verbose($"Godot MainProjectBasePath: {file.Directory}");
                        GodotDescriptor = new GodotDescriptor(false, file.Directory.FullPath);
                        model.GodotDescriptor.SetValue(GodotDescriptor);
                        break;
                    }
                }));
        }
    }
}