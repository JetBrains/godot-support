using System;
using System.Linq.Expressions;
using JetBrains.Application.Parts;
using JetBrains.Application.Settings;
using JetBrains.Application.Threading;
using JetBrains.DataFlow;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Reflection;
using JetBrains.ReSharper.Plugins.Godot.Protocol;
using JetBrains.ReSharper.Psi.Util;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Application.Settings
{
    [SolutionComponent(Instantiation.LaterAsyncAnyThreadSafe)]
    public class SettingsSynchronizer
    {
        public SettingsSynchronizer(Lifetime lifetime, ISolution solution, FrontendBackendHost host,
                                         IApplicationWideContextBoundSettingStore settingsStore)
        {
            var boundStore = settingsStore.BoundSettingsStore;
            
            BindSettingToProperty(lifetime, solution, host, boundStore,
                (GodotSettings s) => s.EnableDebuggerExtensions,
                (model, args) => model.BackendSettings.EnableDebuggerExtensions.Value = args.New);
            
            BindSettingToProperty(lifetime, solution, host, boundStore,
                (GodotSettings s) => s.LanguageServerConnectionMode,
                (model, args) => model.BackendSettings.LspConnectionMode.Value = args.New);
            BindSettingToProperty(lifetime, solution, host, boundStore,
                (GodotSettings s) => s.RemoteHostPort,
                (model, args) => model.BackendSettings.RemoteHostPort.Value = args.New);
            BindSettingToProperty(lifetime, solution, host, boundStore,
                (GodotSettings s) => s.UseDynamicPort,
                (model, args) => model.BackendSettings.UseDynamicPort.Value = args.New);
        }

        private static void BindSettingToProperty<TKeyClass, TEntryMemberType>(
            Lifetime lifetime, ISolution solution, FrontendBackendHost frontendBackendHost,
            IContextBoundSettingsStoreLive boundStore,
            Expression<Func<TKeyClass, TEntryMemberType>> entry,
            Action<GodotFrontendBackendModel, PropertyChangedEventArgs<TEntryMemberType>> action)
        {
            var name = entry.GetInstanceMemberName();
            var setting = boundStore.Schema.GetScalarEntry(entry);
            var apartmentForNotifications = ApartmentForNotifications.Primary(solution.Locks);
            boundStore.GetValueProperty2<TEntryMemberType>(lifetime, setting, null, apartmentForNotifications).Change.Advise_HasNew(lifetime,
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