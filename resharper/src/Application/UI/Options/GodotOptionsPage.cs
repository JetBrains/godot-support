#nullable enable

using JetBrains.Application.Help;
using JetBrains.Application.UI.Options;
using JetBrains.Application.UI.Options.OptionsDialog;
using JetBrains.IDE.UI.Extensions;
using JetBrains.IDE.UI.Options;
using JetBrains.Lifetimes;
using JetBrains.ReSharper.Feature.Services.OptionPages.CodeEditing;
using JetBrains.ReSharper.Plugins.Godot.Application.Settings;
using JetBrains.ReSharper.Plugins.Godot.Resources;
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

        public GodotOptionsPage(Lifetime lifetime,
            OptionsPageContext pageContext,
            OptionsSettingsSmartContext settingsStore)
            : base(lifetime, pageContext, settingsStore)
        {
            AddHeader(Strings.CSharp_Text);
            using (Indent())
            {
                AddBoolOption((GodotSettings s) => s.EnableDebuggerExtensions,
                    Strings.ExtendValueRendering_Text);
                AddBetterCommentText(Strings.GodotOptionsPage_AddDebuggingSection_Extend_value_rendering_Comment);

                AddBoolOption((GodotSettings s) => s.BuildAutomatically,
                    Strings.BuildAutomatically_Text);
                AddBetterCommentText(Strings.GodotSettings_BuildAutomatically_Description);
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

    }
}