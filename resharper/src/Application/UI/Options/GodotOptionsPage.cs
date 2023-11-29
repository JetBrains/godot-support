#nullable enable

using System;
using System.Linq.Expressions;
using JetBrains.Application.UI.Options;
using JetBrains.Application.UI.Options.OptionsDialog;
using JetBrains.Application.UI.Options.OptionsDialog.SimpleOptions;
using JetBrains.Application.UI.Options.OptionsDialog.SimpleOptions.ViewModel;
using JetBrains.IDE.UI.Options;
using JetBrains.Lifetimes;
using JetBrains.ReSharper.Feature.Services.OptionPages.CodeEditing;
using JetBrains.ReSharper.Plugins.Godot.Application.Settings;
using JetBrains.UI.ThemedIcons;
using JetBrains.Rider.Model.UIAutomation;

namespace JetBrains.ReSharper.Plugins.Godot.Application.UI.Options
{
    [OptionsPage(PID, Name, typeof(QydowydThemedIconsThemedIcons.Godot), ParentId = CodeEditingPage.PID, 
        HelpKeyword = "Settings_Godot_Engine")]
    public class GodotOptionsPage : BeSimpleOptionsPage
    {
        public const string PID = "GodotPluginSettings";
        public const string Name = "Godot Engine";

        // private static readonly Expression<Func<GodotSettings, bool>> ourEnabledProperty =
        //     s => s.LanguageServerConnectionModeEntry != LanguageServerConnectionMode.Never;
        
        private static readonly Expression<Func<GodotSettings, string>> ourHostNameAccessor =
            s => s.RemoteHost;
        
        private static readonly Expression<Func<GodotSettings, int>> ourHostPortAccessor =
            s => s.RemoteHostPort;

        public GodotOptionsPage(Lifetime lifetime,
                                OptionsPageContext pageContext,
                                OptionsSettingsSmartContext settingsStore)
            : base(lifetime, pageContext, settingsStore)
        {
            AddNetworkSection();
        }

        private void AddNetworkSection()
        {
            AddHeader("Network");
            using (Indent())
            {
                AddComboOption((GodotSettings s) => s.LanguageServerConnectionModeEntry,
                    "Utilizing LSP server:", string.Empty, string.Empty,
                    new RadioOptionPoint(LanguageServerConnectionMode.StartEditorHeadless, "Automatically start headless LSP server"),
                    new RadioOptionPoint(LanguageServerConnectionMode.StartEditor, "Automatically start Godot Editor, if it is not running"), 
                    new RadioOptionPoint(LanguageServerConnectionMode.ConnectRunningEditor, "Wait for the Godot Editor"),
                    new RadioOptionPoint(LanguageServerConnectionMode.Never, "Never use LSP")
                );
                AddKeyword("Language server");
                
                AddTextBox(ourHostNameAccessor, "Host");
                AddIntOption(ourHostPortAccessor, "Port");
                // BindToEnabledProperty(AddTextBox(ourHostNameAccessor, "Host"), ourEnabledProperty);
                // BindToEnabledProperty(AddIntOption(ourHostPortAccessor, "Port"), ourEnabledProperty);
            }
        }
        
        private void BindToEnabledProperty(BeControl option, Expression<Func<GodotSettings, bool>> setting)
        {
            AddBinding(option, BindingStyle.IsEnabledProperty, setting, enable => enable);
        }
    }
}
