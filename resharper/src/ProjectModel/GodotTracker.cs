using System.Linq;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Tasks;
using JetBrains.Rd.Base;
using JetBrains.ReSharper.Feature.Services.Protocol;
using JetBrains.ReSharper.Features.Running;
using JetBrains.Rider.Model;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    [SolutionComponent(InstantiationEx.LegacyDefault)]
    public class GodotTracker
    {
        private readonly ILogger myLogger;
        public VirtualFileSystemPath MainProjectBasePath { get; private set; }
        
        public IProject MainProject { get; private set; }
        public GodotDescriptor GodotDescriptor { get; private set; }
        
        /// <summary>
        /// List of config/feature from the project.godot
        /// </summary>
        // public string[] Features { get; private set; }
        public GodotTracker(ISolution solution, ILogger logger, ISolutionLoadTasksScheduler tasksScheduler)
        {
            myLogger = logger;
            var model = solution.GetProtocolSolution().GetGodotFrontendBackendModel();
            tasksScheduler.EnqueueTask(new SolutionLoadTask(GetType(), SolutionLoadTaskKinds.Done, () =>
            {
                try
                {
                    if (solution.SolutionFilePath.IsEmpty &&
                        solution.SolutionDirectory.Combine("project.godot").ExistsFile)
                    {
                        GodotDescriptor = new GodotDescriptor(true, solution.SolutionDirectory.FullPath, null);
                        model.GodotDescriptor.SetValue(GodotDescriptor);
                        // Features = ReadFeatures(solution.SolutionDirectory.Combine("project.godot"));
                        // logger.Verbose($"Godot project features: {string.Join(",", Features)}");
                        return;
                    }

                    foreach (var project in solution.GetAllProjects().Where(project => project.IsGodotProject()))
                    {
                        var file = project.Location.Combine("project.godot");
                        if (!file.ExistsFile) continue;
                        MainProjectBasePath = file.Directory;
                        MainProject = project;
                        logger.Verbose($"Godot MainProjectBasePath: {file.Directory}");
                        GodotDescriptor = new GodotDescriptor(false, file.Directory.FullPath, MainProject.TargetFrameworkIds.SingleItem().ToRdTargetFrameworkInfo());
                        model.GodotDescriptor.SetValue(GodotDescriptor);
                        // Features = ReadFeatures(file);
                        // logger.Verbose($"Godot project features: {string.Join(",", Features)}");
                        break;
                    }
                }
                finally
                {
                    model.GodotInitialized.SetValue(true);
                }
            }));
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