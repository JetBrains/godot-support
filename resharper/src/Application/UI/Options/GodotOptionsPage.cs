#nullable enable

using System;
using System.Linq.Expressions;
using JetBrains.Application.Settings;
using JetBrains.Application.UI.Options;
using JetBrains.Application.UI.Options.OptionsDialog;
using JetBrains.Application.UI.Options.OptionsDialog.SimpleOptions;
using JetBrains.Application.UI.Options.OptionsDialog.SimpleOptions.ViewModel;
using JetBrains.DataFlow;
using JetBrains.IDE.UI.Options;
using JetBrains.Lifetimes;
using JetBrains.ReSharper.Feature.Services.OptionPages.CodeEditing;
using JetBrains.ReSharper.Plugins.Godot.Application.Settings;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Rider.Model.UIAutomation;
using JetBrains.UI.ThemedIcons;

namespace JetBrains.ReSharper.Plugins.Godot.Application.UI.Options
{
    [OptionsPage(PID, Name, typeof(QydowydThemedIconsThemedIcons.Godot), ParentId = CodeEditingPage.PID,
        HelpKeyword = "Settings_Godot_Engine")]
    public class GodotOptionsPage : BeSimpleOptionsPage
    {
        public const string PID = "GodotPluginSettings";
        public const string Name = "Godot Engine";

        private static readonly Expression<Func<GodotSettings, LanguageServerConnectionMode>>
            ourLanguageServerConnectionMode =
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
            
            AddHeader("Debugger");
            using (Indent())
            {
                AddBoolOption((GodotSettings s) => s.EnableDebuggerExtensions,
                    "Enable debugger extensions");
            }
        }

        private void AddNetworkSection()
        {
            AddHeader("Network");
            using (Indent())
            {
                AddComboOption((GodotSettings s) => s.LanguageServerConnectionMode,
                    "Connecting LSP server:", string.Empty, string.Empty,
                    new RadioOptionPoint(LanguageServerConnectionMode.StartEditorHeadless,
                        "Automatically start headless LSP server"),
                    // new RadioOptionPoint(LanguageServerConnectionMode.ConnectRunningEditor, "Attempt to connect the running Godot Editor"), // todo: commented because need some tricky waiting and probing the port
                    new RadioOptionPoint(LanguageServerConnectionMode.Never, "Never use LSP")
                );
                AddKeyword("Language server");

                // AddTextBox(ourHostNameAccessor, "Host"); // host is always localhot, lets not allow changing it.

                // Godot 4.3 and later
                var useDynamic = AddBoolOption(ourUseDynamicPort, "Use a random free port (supported in Godot 4.3+)",
                    toolTipText: "Only supported by the Godot 4.3+");

                var portOption = AddIntOption(ourHostPortAccessor, "Port");

                // AddBinding(portOption, BindingStyle.IsEnabledProperty, ourUseDynamicPort, enable => !enable);

                var sourceProperty = OptionsSettingsSmartContext.GetValueProperty(Lifetime, ourUseDynamicPort);
                sourceProperty
                    .Change.Advise(Lifetime, () =>
                    {
                        // RIDER-104651 Visibility of a BeControl based on other settings works inconsistently
                        portOption.Enabled.Value = !sourceProperty.Value; // this always works
                        portOption.Visible.Value =
                            sourceProperty.Value
                                ? ControlVisibility.Collapsed
                                : ControlVisibility
                                    .Visible; // this doesn't work initially, but starts working when you change sourceProperty back and forth
                    });

                // AddBinding(portOption, BindingStyle.IsEnabledProperty, ourLanguageServerConnectionMode,
                //     mode => mode is not LanguageServerConnectionMode.Never);
                AddBinding(useDynamic, BindingStyle.IsEnabledProperty, ourLanguageServerConnectionMode,
                    mode => mode is LanguageServerConnectionMode.StartEditorHeadless);
            }
        }
    }
}