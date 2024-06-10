using System;
using System.Collections.Generic;
using JetBrains.Application;
using JetBrains.Application.Parts;
using JetBrains.Application.Settings;
using JetBrains.Application.Settings.Implementation;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Scope;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Settings;
using JetBrains.ReSharper.Plugins.Godot.CSharp.FileTemplates.Scope;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.FileTemplates
{
    // Defines settings for the QuickList, or we don't get a QuickList at all
    // Note that the QuickList can be empty, but it's still required
    [ShellComponent(Instantiation.DemandAnyThreadSafe)]
    public class GodotQuickListDefaultSettings : HaveDefaultSettings
    {
        private readonly ILogger myLogger;
        private readonly ISettingsSchema mySettingsSchema;
        private readonly IMainScopePoint myProjectMainPoint;
        // private readonly IMainScopePoint myFilesMainPoint;

        public GodotQuickListDefaultSettings(ILogger logger, ISettingsSchema settingsSchema,
            GodotProjectScopeCategoryUIProvider projectScopeProvider
            )
            : base(settingsSchema, logger)
        {
            myLogger = logger;
            mySettingsSchema = settingsSchema;
            myProjectMainPoint = projectScopeProvider.MainPoint;
        }

        public override void InitDefaultSettings(ISettingsStorageMountPoint mountPoint)
        {
            InitialiseQuickList(mountPoint, myProjectMainPoint);

            // TODO: Not sure if this would be better handled in a .dotSettings file
            var pos = 0;
            AddToQuickList(mountPoint, myProjectMainPoint, "Node", ++pos, "67A1E56D-859B-4BEF-A4EE-1D7A06F1A2B9");
        }

        private void InitialiseQuickList(ISettingsStorageMountPoint mountPoint, IMainScopePoint quickList)
        {
            var settings = new QuickListSettings {Name = quickList.QuickListTitle};
            SetIndexedKey(mountPoint, settings, new GuidIndex(quickList.QuickListUID));
        }

        private void AddToQuickList(ISettingsStorageMountPoint mountPoint, IMainScopePoint quickList, string name, int position, string guid)
        {
            var quickListKey = mySettingsSchema.GetIndexedKey<QuickListSettings>();
            var entryKey = mySettingsSchema.GetIndexedKey<EntrySettings>();
            var dictionary = new Dictionary<SettingsKey, object>
            {
                {quickListKey, new GuidIndex(quickList.QuickListUID)},
                {entryKey, new GuidIndex(new Guid(guid))}
            };

            if (!ScalarSettingsStoreAccess.IsIndexedKeyDefined(mountPoint, entryKey, dictionary, null, myLogger))
                ScalarSettingsStoreAccess.CreateIndexedKey(mountPoint, entryKey, dictionary, null, myLogger);
            SetValue(mountPoint, (EntrySettings e) => e.EntryName, name, dictionary);
            SetValue(mountPoint, (EntrySettings e) => e.Position, position, dictionary);
        }

        public override string Name => "Godot QuickList settings";
    }
}