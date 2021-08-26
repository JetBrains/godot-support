using JetBrains.Application.Settings;
using JetBrains.ReSharper.Resources.Settings;

namespace JetBrains.ReSharper.Plugins.Godot.Settings
{
    [SettingsKey(typeof(CodeEditingSettings), "Godot plugin settings")]
    public class GodotSettings
    {
        // Debugging
        [SettingsEntry(true, "Enable debugger extensions")]
        public bool EnableDebuggerExtensions;
    }
}
