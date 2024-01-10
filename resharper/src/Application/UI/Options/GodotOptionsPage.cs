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
using JetBrains.Rider.Model.Godot.FrontendBackend;
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

        private static readonly Expression<Func<GodotSettings, LanguageServerConnectionMode>> ourLanguageServerConnectionMode =
            s => s.LanguageServerConnectionMode;
        
        private static readonly Expression<Func<GodotSettings, bool>> ourUseDynamicPort = s => s.UseDynamicPort;
        
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
                AddComboOption((GodotSettings s) => s.LanguageServerConnectionMode,
                    "Connecting LSP server:", string.Empty, string.Empty,
                    new RadioOptionPoint(LanguageServerConnectionMode.StartEditorHeadless, "Automatically start headless LSP server"),
                    // new RadioOptionPoint(LanguageServerConnectionMode.ConnectRunningEditor, "Attempt to connect the running Godot Editor"), // todo: commented because need some tricky waiting and probing the port
                    new RadioOptionPoint(LanguageServerConnectionMode.Never, "Never use LSP")
                );
                AddKeyword("Language server");
                
                // AddTextBox(ourHostNameAccessor, "Host"); // host is always localhot, lets not allow changing it.
                
                // todo: implement later 
                // var useDynamic = AddBoolOption(ourUseDynamicPort, "Use a random free port",
                //     toolTipText: "Only supported by the future Godot builds");

                var portOption = AddIntOption(ourHostPortAccessor, "Port");
                
                AddBinding(portOption, BindingStyle.IsVisibleProperty, ourUseDynamicPort, enable => !enable);
                AddBinding(portOption, BindingStyle.IsEnabledProperty, ourLanguageServerConnectionMode,
                    mode => mode is not LanguageServerConnectionMode.Never);
                // AddBinding(useDynamic, BindingStyle.IsEnabledProperty, ourLanguageServerConnectionMode,
                //     mode => mode is LanguageServerConnectionMode.StartEditorHeadless);
            }
        }
    }
}
