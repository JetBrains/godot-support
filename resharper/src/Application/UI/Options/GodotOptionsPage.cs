#nullable enable

using System;
using System.Linq.Expressions;
using JetBrains.Application.Help;
using JetBrains.Application.Settings;
using JetBrains.Application.UI.Options;
using JetBrains.Application.UI.Options.OptionsDialog;
using JetBrains.Application.UI.Options.OptionsDialog.SimpleOptions;
using JetBrains.Application.UI.Options.OptionsDialog.SimpleOptions.ViewModel;
using JetBrains.DataFlow;
using JetBrains.IDE.UI.Extensions;
using JetBrains.IDE.UI.Options;
using JetBrains.Lifetimes;
using JetBrains.ReSharper.Feature.Services.OptionPages.CodeEditing;
using JetBrains.ReSharper.Plugins.Godot.Application.Settings;
using JetBrains.ReSharper.Plugins.Godot.Resources;
using JetBrains.Rider.Model.Godot.FrontendBackend;
using JetBrains.Rider.Model.UIAutomation;
using JetBrains.UI.ThemedIcons;

namespace JetBrains.ReSharper.Plugins.Godot.Application.UI.Options
{
    [OptionsPage(PID, Name, typeof(GodotLogoIconsThemedIcons.Godot), ParentId = CodeEditingPage.PID,
        HelpKeyword = HelpId.Settings_Godot_Engine)]
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
            AddGdScriptSection();
            
            AddHeader(Strings.CSharp_Text);
            using (Indent())
            {
                AddBoolOption((GodotSettings s) => s.EnableDebuggerExtensions,
                    Strings.ExtendValueRendering_Text);
                AddBetterCommentText(Strings.GodotOptionsPage_AddDebuggingSection_Extend_value_rendering_Comment);
            }
        }
        
        private void AddBetterCommentText(string text)
        {
            // AddCommentText doesn't match the UI guidelines for inline help. It doesn't indent, uses the wrong theme
            // colour, should wrap at about 70 characters and have a slightly smaller font size.
            // https://youtrack.jetbrains.com/issue/RIDER-47090
            using (Indent())
            {
                var comment = CreateCommentText(text).WithCustomTextSize(BeFontSize.SMALLER);
                AddControl(comment);
            }

            AddKeyword(text);
        }

        private void AddGdScriptSection()
        {
            AddHeaderWithoutCapitalization(Strings.GDScriptSupport_Text);
            using (Indent())
            {
                AddComboOption((GodotSettings s) => s.LanguageServerConnectionMode,
                    Strings.GDScript_ConnectingLSPServer_Text, string.Empty, string.Empty,
                    new RadioOptionPoint(LanguageServerConnectionMode.StartEditorHeadless,
                        Strings.GDScript_AutomaticallyStartHeadlessLSPServer_Text),
                    new RadioOptionPoint(LanguageServerConnectionMode.ConnectRunningEditor, 
                        Strings.GDScript_AttemptToConnectTheRunningGodotEditor_Text),
                    new RadioOptionPoint(LanguageServerConnectionMode.Never, Strings.GDScript_NeverUseLSP_Text)
                );
                AddKeyword(Strings.GDScript_LanguageServer_Text);

                // AddTextBox(ourHostNameAccessor, "Host"); // host is always localhot, lets not allow changing it.

                // Godot 4.3 and later
                var useDynamic = AddBoolOption(ourUseDynamicPort, Strings.GDScript_UseARandomFreePortSupportedInGodot4_Text,
                    toolTipText: Strings.GDScript_OnlySupportedByTheGodot43_Text);

                var portOption = AddIntOption(ourHostPortAccessor, Strings.GDScript_LSP_Port_Text);

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