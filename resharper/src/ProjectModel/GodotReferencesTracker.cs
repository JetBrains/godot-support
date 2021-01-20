using System.Collections.Generic;
using System.Linq;
using JetBrains.Annotations;
using JetBrains.Application.changes;
using JetBrains.Collections;
using JetBrains.Collections.Viewable;
using JetBrains.Diagnostics;
using JetBrains.Lifetimes;
using JetBrains.Metadata.Utils;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.Assemblies.Impl;
using JetBrains.ProjectModel.Tasks;
using JetBrains.Rd.Base;
using JetBrains.Util;
using JetBrains.Util.Reflection;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectModel
{
    public interface IGodotReferenceChangeHandler
    {
        // This is guaranteed to be called on all handlers before any handler receives OnGodotProjectAdded
        void OnHasGodotReference();
        void OnGodotProjectAdded(Lifetime projectLifetime, IProject project);
    }

    [SolutionComponent]
    public class GodotReferencesTracker : IChangeProvider
    {
        // Godot 2017.3 split GodotEngine into modules. The copy in the Managed folder is the original monolithic build.
        // The Managed/GodotEngine/ folder contains the version split into modules, and generated projects reference the
        // GodotEngine.dll in this folder, as well as the modules. Managed/GodotEngine/GodotEngine.dll has a load of
        // type forwards to the new modules. Non-generated/manually maintained projects can still reference the original
        // Managed/GodotEngine.dll, and the type forwards will fix things up at runtime.
        // We check for references to GodotEngine.dll, GodotEngine.CoreModule.dll and (just in case)
        // GodotEngine.ShaderInternalsModule.dll
        // Godot 2020.2 similarly splits GodotEditor.dll, primarily to allow packages to override implementations, such
        // as UIElements.
        private static readonly JetHashSet<string> ourGodotReferenceNames = new JetHashSet<string>
        {
            "GodotSharp",
            "GodotSharpEditor",
            "GodotTools"
        };

        private static readonly ICollection<AssemblyNameInfo> ourGodotReferenceNameInfos;

        private readonly Lifetime myLifetime;
        private readonly ILogger myLogger;
        private readonly ISolution mySolution;
        private readonly ModuleReferenceResolveSync myModuleReferenceResolveSync;
        private readonly ChangeManager myChangeManager;
        private readonly IViewableProjectsCollection myProjects;
        private readonly ICollection<IGodotReferenceChangeHandler> myHandlers;
        private readonly Dictionary<IProject, Lifetime> myAllProjectLifetimes;
        private readonly HashSet<IProject> myGodotProjects;

        // If you only want to be notified that we're a Godot solution, advise this.
        // If all you're interested in is being notified that we're a Godot solution, advise this. If you need to know
        // we're a Godot solution *and*/or know about Godot projects (and get a per-project lifetime), implement
        // IGodotReferenceChangeHandler
        public readonly ViewableProperty<bool> HasGodotReference = new ViewableProperty<bool>(false);

        static GodotReferencesTracker()
        {
            ourGodotReferenceNameInfos = new List<AssemblyNameInfo>();
            foreach (var name in ourGodotReferenceNames)
                ourGodotReferenceNameInfos.Add(AssemblyNameInfoFactory.Create2(name, null));
        }

        public GodotReferencesTracker(
            Lifetime lifetime,
            IEnumerable<IGodotReferenceChangeHandler> handlers,
            ISolution solution,
            ISolutionLoadTasksScheduler scheduler,
            ModuleReferenceResolveSync moduleReferenceResolveSync,
            ChangeManager changeManager,
            IViewableProjectsCollection projects,
            ILogger logger)
        {
            myAllProjectLifetimes = new Dictionary<IProject, Lifetime>();
            myGodotProjects = new HashSet<IProject>();

            myHandlers = handlers.ToList();
            myLifetime = lifetime;
            myLogger = logger;
            mySolution = solution;
            myModuleReferenceResolveSync = moduleReferenceResolveSync;
            myChangeManager = changeManager;
            myProjects = projects;

            // At PreparePsiModules, we know what references we have, so we know if we're a Godot project. This is where
            // we'll initialise our custom PSI module. We have to initialise our PSI module before Done, or the
            // PersistentIndexManager will clean out the "orphaned" external (YAML) files and we'll have to reparse all
            // files on every startup
            scheduler.EnqueueTask(new SolutionLoadTask("Preparing Godot project", SolutionLoadTaskKinds.PreparePsiModules,
                OnSolutionPreparePsiModules));
        }

        private void OnSolutionPreparePsiModules()
        {
            myChangeManager.RegisterChangeProvider(myLifetime, this);
            myChangeManager.AddDependency(myLifetime, this, myModuleReferenceResolveSync);

            // Track the lifetime of all projects, so we can pass it to the handler later
            myProjects.Projects.View(myLifetime,
                (projectLifetime, project) =>
                {
                    myAllProjectLifetimes.Add(projectLifetime, project, projectLifetime);
                    if (IsGodotProjectOrHasGodotReference(project))
                        myGodotProjects.Add(projectLifetime, project);
                });

            var godotProjectLifetimes = myAllProjectLifetimes.Where(pair => IsGodotProjectOrHasGodotReference(pair.Key)).ToList();
            if (godotProjectLifetimes.Count == 0)
                return;

            NotifyHasGodotReference();
            NotifyOnGodotProjectAdded(godotProjectLifetimes);
        }

        private void NotifyHasGodotReference()
        {
            if (!HasGodotReference.Value)
            {
                HasGodotReference.SetValue(true);
                foreach (var handler in myHandlers) handler.OnHasGodotReference();
            }
        }

        private void NotifyOnGodotProjectAdded(List<KeyValuePair<IProject, Lifetime>> godotProjectLifetimes)
        {
            foreach (var handler in myHandlers)
            {
                foreach (var (project, lifetime) in godotProjectLifetimes)
                {
                   handler.OnGodotProjectAdded(lifetime, project);
                }
            }
        }

        object IChangeProvider.Execute(IChangeMap changeMap)
        {
            var projectModelChange = changeMap.GetChange<ProjectModelChange>(mySolution);
            if (projectModelChange == null)
                return null;

            var changes = ReferencedAssembliesService.TryGetAssemblyReferenceChanges(projectModelChange,
                ourGodotReferenceNameInfos, myLogger.Trace());

            var newGodotProjects = new List<KeyValuePair<IProject, Lifetime>>();
            foreach (var change in changes)
            {
                if (change.IsAdded)
                {
                    var project = change.GetNewProject();
                    if (IsGodotProjectOrHasGodotReference(project))
                    {
                        Assertion.Assert(myAllProjectLifetimes.ContainsKey(project), "project is not added");
                        if (myAllProjectLifetimes.TryGetValue(project, out var projectLifetime))
                        {
                            newGodotProjects.Add(JetKeyValuePair.Of(project, projectLifetime));
                            if (!myGodotProjects.Contains(project))
                                myGodotProjects.Add(projectLifetime, project);
                        }
                    }
                }
            }

            if (newGodotProjects.Count > 0)
            {
                myChangeManager.ExecuteAfterChange(() =>
                {
                    NotifyHasGodotReference();
                    NotifyOnGodotProjectAdded(newGodotProjects);
                });
            }

            return null;
        }

        public bool IsGodotProject(IProject project)
        {
            return myGodotProjects.Contains(project);
        }

        private static bool IsGodotProjectOrHasGodotReference([NotNull] IProject project)
        {
            return project.IsGodotProject() || ReferencesGodot(project);
        }

        public static bool ReferencesGodot(IProject project)
        {
            var targetFrameworkId = project.GetCurrentTargetFrameworkId();
            foreach (var reference in project.GetModuleReferences(targetFrameworkId))
            {
                if (ourGodotReferenceNames.Contains(reference.Name))
                    return true;
            }
            return false;
        }
    }
}