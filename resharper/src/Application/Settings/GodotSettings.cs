using JetBrains.Application.Settings;
using JetBrains.ReSharper.Resources.Settings;

namespace JetBrains.ReSharper.Plugins.Godot.Application.Settings
{
    [SettingsKey(typeof(CodeEditingSettings), "Godot plugin settings")]
    public class GodotSettings
    {
        [SettingsEntry(LanguageServerConnectionMode.StartEditorHeadless, "Different ways to connect LSP")]
        public LanguageServerConnectionMode LanguageServerConnectionMode;

        [SettingsEntry("127.0.0.1", "RemoteHost")]
        public string RemoteHost;
        
        [SettingsEntry(6005, "Remote host port")]
        public int RemoteHostPort;

        [SettingsEntry(true, "Use a random free port, supported only with future Godot builds")]
        public bool UseDynamicPort;
        
        // Debugging
        [SettingsEntry(true, "Enable debugger extensions")]
        public bool EnableDebuggerExtensions;
    }
}
