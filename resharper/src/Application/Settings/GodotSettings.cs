using JetBrains.Application.Settings;
using JetBrains.ReSharper.Plugins.Godot.Resources;
using JetBrains.ReSharper.Resources.Settings;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Application.Settings
{
    [SettingsKey(typeof(CodeEditingSettings), nameof(Strings.GodotPluginSettings_Text))]
    public class GodotSettings
    {
        // LSP 
        [SettingsEntry(LanguageServerConnectionMode.ConnectRunningEditor,  nameof(Strings.LanguageServerConnectionMode_Description))]
        public LanguageServerConnectionMode LanguageServerConnectionMode;

        [SettingsEntry("127.0.0.1", nameof(Strings.GDScript_LSP_Remote_Host_Text))]
        public string RemoteHost;
        
        [SettingsEntry(6005, nameof(Strings.GDScript_LSP_Port_Text))]
        public int RemoteHostPort;

        [SettingsEntry(false, nameof(Strings.GDScript_UseARandomFreePortSupportedInGodot4_Text))]
        public bool UseDynamicPort;
        
        // Debugging
        [SettingsEntry(true, DescriptionResourceType: typeof(Strings), DescriptionResourceName: nameof(Strings.GodotSettings_t_Enable_debugger_extensions))]
        public bool EnableDebuggerExtensions;
    }
}
