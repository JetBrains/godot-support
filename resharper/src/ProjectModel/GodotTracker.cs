using System.Collections.Generic;
using System.Linq;
using JetBrains.Application.Parts;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Tasks.Listeners;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.ReSharper.Features.Running;
using JetBrains.ReSharper.Resources.Shell;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent(Instantiation.DemandAnyThreadSafe)]
    public class GodotTracker(
        ISolution solution,
        ILogger logger)
        : ISolutionLoadTasksSolutionStructureReadyListener2
    {
        public VirtualFileSystemPath? MainProjectBasePath { get; private set; }
        public VirtualFileSystemPath? ProjectGodotPath { get; private set; }

        public IProject? MainProject { get; private set; }
        public GodotDescriptor? GodotDescriptor { get; private set; }

        IEnumerable<SolutionLoadTasksListenerExecutionStep> ISolutionLoadTasksSolutionStructureReadyListener2.OnSolutionLoadSolutionStructureReady(OuterLifetime loadLifetime)
        {
            yield return SolutionLoadTasksListenerExecutionStep.YieldToBackgroundThread;
            if (!solution.SolutionFilePath.IsEmpty)
            {
                var project = GetMainGodotProject();
                if (project != null)
                {
                    MainProject = project;
                    logger.Verbose($"Godot MainProjectBasePath: {MainProjectBasePath}");
                    GodotDescriptor = new GodotDescriptor(false, MainProjectBasePath!.FullPath, MainProject.ProjectFile?.Location?.FullPath,
                        MainProject.TargetFrameworkIds.SingleItem()?.ToRdTargetFrameworkInfo());
                }
            }
            else
            {
                var files = solution.SolutionDirectory.GetChildFiles(3, "project.godot", PathSearchFlags.RecurseIntoSubdirectories);
                var bestMatch = files.OrderBy(it => it.Components.Count()).FirstOrDefault();
                if (bestMatch != null)
                {
                    GodotDescriptor = new GodotDescriptor(true, bestMatch.Directory.FullPath, null, null);
                    ProjectGodotPath = bestMatch;
                }
            }

            yield return SolutionLoadTasksListenerExecutionStep.YieldToMainThreadGuarded;
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            if (GodotDescriptor != null)
                model.GodotDescriptor.SetValue(GodotDescriptor);
            model.IsGodotProject.SetValue(GodotDescriptor != null);
        }

        private IProject? GetMainGodotProject()
        {
            using (ReadLockCookie.Create())
            {
                foreach (var project in solution.GetAllProjects())
                {
                    if (project.GetProjectKind() != ProjectKind.REGULAR_PROJECT) continue; // MiscProject and SolutionProject
                    // not a regular generated project, however can happen with gdextensions template
                    var subItem = project.GetSubItems("project.godot").FirstOrDefault();
                    if (subItem != null)
                    {
                        ProjectGodotPath = subItem.Location;
                        MainProjectBasePath = ProjectGodotPath.Parent;
                        return project;
                    }

                    // regular case
                    var file = project.Location.Combine("project.godot");
                    if (file.ExistsFile)
                    {
                        ProjectGodotPath = file;
                        MainProjectBasePath = project.Location;
                        return project;
                    }
                }
            }

            return null;
        }
    }
}
