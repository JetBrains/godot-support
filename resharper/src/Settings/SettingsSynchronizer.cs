using System;
using System.Linq.Expressions;
using JetBrains.Application.Settings;
using JetBrains.Application.Threading;
using JetBrains.DataFlow;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Reflection;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
using JetBrains.ReSharper.Psi.Util;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Settings
{
    [SolutionComponent]
    public class SettingsSynchronizer
    {
        public SettingsSynchronizer(Lifetime lifetime, ISolution solution, FrontendBackendHost host,
                                         IApplicationWideContextBoundSettingStore settingsStore)
        {
            var boundStore = settingsStore.BoundSettingsStore;
            
            BindSettingToProperty(lifetime, solution, host, boundStore,
                (GodotSettings s) => s.EnableDebuggerExtensions,
                (model, args) => model.BackendSettings.EnableDebuggerExtensions.Value = args.New);
        }

        private static void BindSettingToProperty<TKeyClass, TEntryMemberType>(
            Lifetime lifetime, ISolution solution, FrontendBackendHost frontendBackendHost,
            IContextBoundSettingsStoreLive boundStore,
            Expression<Func<TKeyClass, TEntryMemberType>> entry,
            Action<GodotFrontendBackendModel, PropertyChangedEventArgs<TEntryMemberType>> action)
        {
            var name = entry.GetInstanceMemberName();
            var setting = boundStore.Schema.GetScalarEntry(entry);
            boundStore.GetValueProperty<TEntryMemberType>(lifetime, setting, null).Change.Advise_HasNew(lifetime,
                args =>
                {
                    solution.Locks.ExecuteOrQueueEx(lifetime, name, () =>
                    {
                        frontendBackendHost.Do(m => action(m, args));
                    });
                });
        }
    }
}