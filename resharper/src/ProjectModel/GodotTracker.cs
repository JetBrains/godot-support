﻿using System.Linq;
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
        public GodotTracker(ISolution solution, ILogger logger, ISolutionLoadTasksScheduler tasksScheduler)
        {
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            if (solution.SolutionFilePath.IsEmpty && solution.SolutionDirectory.Combine("project.godot").ExistsFile)
            {
                model.GodotDescriptor.SetValue(new GodotDescriptor(true, solution.SolutionDirectory.FullPath));
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
                        logger.Verbose($"Godot MainProjectBasePath: {file.Directory}");
                        model.GodotDescriptor.SetValue(new GodotDescriptor(false, file.Directory.FullPath));
                        break;
                    }
                }));
        }
    }
}