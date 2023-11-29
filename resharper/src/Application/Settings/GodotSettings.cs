using JetBrains.Application.Settings;
using JetBrains.ReSharper.Resources.Settings;

namespace JetBrains.ReSharper.Plugins.Godot.Application.Settings
{
    [SettingsKey(typeof(CodeEditingSettings), "Godot plugin settings")]
    public class GodotSettings
    {
        [SettingsEntry(LanguageServerConnectionMode.StartEditorHeadless, "Different ways of utilizing LSP")]
        public LanguageServerConnectionMode LanguageServerConnectionModeEntry;

        [SettingsEntry("127.0.0.1", "RemoteHost")]
        public string RemoteHost;
        
        [SettingsEntry(6005, "Remote host port")]
        public int RemoteHostPort;
        
        // Debugging
        [SettingsEntry(true, "Enable debugger extensions")]
        public bool EnableDebuggerExtensions;
    }
}
