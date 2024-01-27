using System;
using System.Linq.Expressions;
using JetBrains.Application.Settings;
using JetBrains.ReSharper.Resources.Settings;
using JetBrains.Rider.Model.Godot.FrontendBackend;

namespace JetBrains.ReSharper.Plugins.Godot.Application.Settings
{
    [SettingsKey(typeof(CodeEditingSettings), "Godot plugin settings")]
    public class GodotSettings
    {
        // LSP 
        [SettingsEntry(LanguageServerConnectionMode.Never, "Different ways to connect LSP")]
        public LanguageServerConnectionMode LanguageServerConnectionMode;

        [SettingsEntry("127.0.0.1", "RemoteHost")]
        public string RemoteHost;
        
        [SettingsEntry(6005, "Remote host port")]
        public int RemoteHostPort;

        [SettingsEntry(false, "Use a random free port, supported with Godot 4.3+")]
        public bool UseDynamicPort;
        
        // Debugging
        [SettingsEntry(true, "Enable debugger extensions")]
        public bool EnableDebuggerExtensions;
    }
}
