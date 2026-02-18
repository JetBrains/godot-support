using JetBrains.Application.Settings;
using JetBrains.ReSharper.Plugins.Godot.Resources;
using JetBrains.ReSharper.Resources.Settings;

namespace JetBrains.ReSharper.Plugins.Godot.Application.Settings
{
    [SettingsKey(typeof(CodeEditingSettings), nameof(Strings.GodotPluginSettings_Text))]
    public class GodotSettings
    {
        // Debugging
        [SettingsEntry(true, DescriptionResourceType: typeof(Strings),
            DescriptionResourceName: nameof(Strings.GodotSettings_t_Enable_debugger_extensions))]
        public bool EnableDebuggerExtensions;

        // Build
        [SettingsEntry(true, DescriptionResourceType: typeof(Strings),
            DescriptionResourceName: nameof(Strings.GodotSettings_BuildAutomatically_Description))]
        public bool BuildAutomatically;
    }
}