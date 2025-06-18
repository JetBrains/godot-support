using System.Linq;
using System.Threading.Tasks;
using JetBrains.Annotations;
using JetBrains.Application.Parts;
using JetBrains.Application.Threading;
using JetBrains.Application.Threading.Tasks;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Tasks;
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
    public class GodotTracker : ISolutionLoadTasksInitialSynchronizeSolutionListener
    {
        private readonly ISolution mySolution;
        private readonly ILogger myLogger;
        private readonly ISolutionLoadTasksScheduler myLoadTasksScheduler;
        private readonly IShellLocks myLocks;
        private readonly Lifetime myLifetime;
        public VirtualFileSystemPath MainProjectBasePath { get; private set; }
        
        public IProject MainProject { get; private set; }
        public GodotDescriptor GodotDescriptor { get; private set; }

        /// <summary>
        /// List of config/feature from the project.godot
        /// </summary>
        // public string[] Features { get; private set; }
        public GodotTracker(ISolution solution, ILogger logger, ISolutionLoadTasksScheduler loadTasksScheduler,
            IShellLocks locks, Lifetime lifetime)
        {
            mySolution = solution;
            myLogger = logger;
            myLoadTasksScheduler = loadTasksScheduler;
            myLocks = locks;
            myLifetime = lifetime;
        }
        
        public Task OnSolutionLoadInitialSynchronizeSolutionAsync(OuterLifetime loadLifetime,
            ISolutionLoadTasksSchedulerThreading _)
        {
            var barrierCookie = myLoadTasksScheduler.SetTasksBarrier(GetType(), SolutionLoadTaskKinds.Done,
                "Waiting for Godot solution load tasks");
            myLocks.Tasks.StartNew(myLifetime, Scheduling.FreeThreaded, TaskPriority.AboveNormal,  () =>
            {
                var model = mySolution.GetProtocolSolution().GetGodotFrontendBackendModel();
                try
                {
                    if (!mySolution.SolutionFilePath.IsEmpty)
                    {
                        var project = GetMainGodotProject();
                        if (project == null) return;
                        MainProjectBasePath = project.Location;
                        MainProject = project;
                        myLogger.Verbose($"Godot MainProjectBasePath: {MainProjectBasePath}");
                        GodotDescriptor = new GodotDescriptor(false, MainProjectBasePath.FullPath,
                            MainProject.TargetFrameworkIds.SingleItem().ToRdTargetFrameworkInfo());

                    }
                    else
                    {
                        var files = mySolution.SolutionDirectory.GetChildFiles(3, "project.godot",
                            PathSearchFlags.RecurseIntoSubdirectories);
                        var bestMatch = files.OrderBy(it => it.Components.Count()).FirstOrDefault();
                        if (bestMatch == null) return;
                        GodotDescriptor = new GodotDescriptor(true, bestMatch.Directory.FullPath, null);
                    }
                    // todo: Features = ReadFeatures(solution.SolutionDirectory.Combine("project.godot")); 
                }
                finally
                {
                    barrierCookie.Dispose();
                    myLocks.ExecuteOrQueue("Sync Godot model", () =>
                    {
                        if (GodotDescriptor != null) 
                            model.GodotDescriptor.SetValue(GodotDescriptor);
                        model.IsGodotProject.SetValue(GodotDescriptor != null);
                    });
                }
            }).NoAwait();
            return Task.CompletedTask;
        }

        [CanBeNull]
        private IProject GetMainGodotProject()
        {
            using (ReadLockCookie.Create())
            {
                foreach (var project in mySolution.GetAllProjects().Where(project => project.IsGodotProject()))
                {
                    var file = project.Location.Combine("project.godot");
                    if (file.ExistsFile) return project;
                }
            }
            return null;
        }

        // RIDER-111425 Design a cache for "project.godot" data
        // maybe we need an external files module on backend, or track it on the frontend
        
        // private string[] ReadFeatures(VirtualFileSystemPath configPath)
        // {
        //     try
        //     {
        //         return configPath.ReadTextStream(reader =>
        //         {
        //             while (reader.ReadLine() is { } line)
        //             {
        //                 if (!line.StartsWith("config/features=PackedStringArray")) continue;
        //                 // define a regex to capture contents inside double quotes
        //                 var regex = new Regex("\"([^\"]*)\"");
        //
        //                 // find all the matches and extract the contents inside quotes
        //                 var matches = regex.Matches(line);
        //                 var features = new string[matches.Count];
        //
        //                 for (int i = 0; i < matches.Count; i++)
        //                 {
        //                     features[i] =
        //                         matches[i].Groups[1].Value; // the first capturing group is the content inside quotes
        //                 }
        //
        //                 return features;
        //             }
        //
        //             return null;
        //         });
        //     }
        //     catch (Exception ex)
        //     {
        //         myLogger.Error(ex);
        //     }
        //
        //     return null;
        // }
    }
}