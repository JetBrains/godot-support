using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.Parts;
using JetBrains.Application.Threading;
using JetBrains.Application.Threading.Tasks;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Tasks;
using JetBrains.ProjectModel.Tasks.Listeners;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.ReSharper.Features.Running;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Threading;
using JetBrains.Util;
using JetBrains.Util.Threading.Tasks;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent(Instantiation.DemandAnyThreadSafe)]
    public class GodotTracker(
        ISolution solution,
        ILogger logger,
        ISolutionLoadTasksScheduler loadTasksScheduler,
        IShellLocks locks,
        Lifetime lifetime)
        : ISolutionLoadTasksInitialSynchronizeSolutionListener2
    {
        public VirtualFileSystemPath? MainProjectBasePath { get; private set; }
        public VirtualFileSystemPath? ProjectGodotPath { get; private set; }
        
        public IProject? MainProject { get; private set; }
        public GodotDescriptor? GodotDescriptor { get; private set; }

        IEnumerable<SolutionLoadTasksListenerExecutionStep> ISolutionLoadTasksInitialSynchronizeSolutionListener2.OnSolutionLoadInitialSynchronizeSolution(OuterLifetime loadLifetime)
        {
            var barrierCookie = loadTasksScheduler.SetTasksBarrier(GetType(), SolutionLoadTaskKinds.Done,
                "Waiting for Godot solution load tasks");
            locks.Tasks.StartNew(lifetime, Scheduling.FreeThreaded, TaskPriority.AboveNormal,  () =>
            {
                var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
                try
                {
                    if (!solution.SolutionFilePath.IsEmpty)
                    {
                        var project = GetMainGodotProject();
                        if (project == null) return;
                        MainProjectBasePath = project.Location;
                        MainProject = project;
                        logger.Verbose($"Godot MainProjectBasePath: {MainProjectBasePath}");
                        GodotDescriptor = new GodotDescriptor(false, MainProjectBasePath.FullPath,
                            MainProject.TargetFrameworkIds.SingleItem().ToRdTargetFrameworkInfo());

                    }
                    else
                    {
                        var files = solution.SolutionDirectory.GetChildFiles(3, "project.godot",
                            PathSearchFlags.RecurseIntoSubdirectories);
                        var bestMatch = files.OrderBy(it => it.Components.Count()).FirstOrDefault();
                        if (bestMatch == null) return;
                        GodotDescriptor = new GodotDescriptor(true, bestMatch.Directory.FullPath, null);
                        ProjectGodotPath = bestMatch;
                    }
                }
                finally
                {
                    barrierCookie.Dispose();
                    locks.ExecuteOrQueue("Sync Godot model", () =>
                    {
                        if (GodotDescriptor != null) 
                            model.GodotDescriptor.SetValue(GodotDescriptor);
                        model.IsGodotProject.SetValue(GodotDescriptor != null);
                    });
                }
            }).NoAwait();
            return EmptyList<SolutionLoadTasksListenerExecutionStep>.Enumerable;
        }

        private IProject? GetMainGodotProject()
        {
            using (ReadLockCookie.Create())
            {
                foreach (var project in solution.GetAllProjects())
                {
                    var file = project.Location.Combine("project.godot");
                    if (file.ExistsFile)
                    {
                        ProjectGodotPath = file;
                        return project;
                    }
                }
            }
            return null;
        }
    }
}