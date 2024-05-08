using System.Text;
using JetBrains.Application;
using JetBrains.Application.DataContext;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.DataContext;
using JetBrains.ReSharper.Feature.Services.AI.Context.Common;
using JetBrains.ReSharper.Feature.Services.ChatContexts;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;

namespace JetBrains.ReSharper.Plugins.Godot.AI;

public abstract class GodotChatContextPartProviderBase : IChatContextPartProvider
{
    public int Priority => ChatContextPartPriorities.SOLUTION + 1;

    public bool IsApplicable(IDataContext dataContext)
    {
        return true;
    }

    public void ContributeTo(IDataContext dataContext, ChatContextPartSet parts)
    {
        var solution = dataContext.GetData(ProjectModelDataConstants.SOLUTION);
        var tracker = solution?.TryGetComponent<GodotTracker>();
        if (tracker?.GodotDescriptor == null) return;

        ContributeTo(tracker, parts);
    }

    protected abstract void ContributeTo(GodotTracker tracker, ChatContextPartSet parts);
}

[ShellComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotPreferableLanguageChatContextPartProvider : GodotChatContextPartProviderBase
{
    protected override void ContributeTo(GodotTracker tracker, ChatContextPartSet parts)
    {
        var text = new StringBuilder();
        if (tracker.GodotDescriptor.IsPureGdScriptProject)
            text.Append("Default language in the current project is GDScript.\n");
        else
        {
            var sdk = tracker.MainProject.ProjectProperties.DotNetCorePlatform?.Sdk;
            if (sdk != null)
            {
                text.Append($"Default language in the current project is C#.\n");
                text.Append($"Project SDK is \"{sdk}\".\n");
            }
        }

        parts.Add(new Part(text.ToString()));
    }

    public sealed class Part : SimpleChatContextPart<Part>, ILanguageDefinitionContextPart
    {
        public override int Priority => ChatContextPartPriorities.SOLUTION + 1;

        public Part(string text) : base(text)
        {
        }
    }
}

[ShellComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotTechnologyChatContextPartProvider : GodotChatContextPartProviderBase
{
    protected override void ContributeTo(GodotTracker tracker, ChatContextPartSet parts)
    {
        if (parts.Contains<TechnologyChatContextPartProvider.Part>())
            parts.Remove<TechnologyChatContextPartProvider.Part>();

        const string text = "Current project is using the following technology: Godot.";
        parts.Add(new Part(text));
    }

    public sealed class Part : SimpleChatContextPart<Part>
    {
        public override int Priority => ChatContextPartPriorities.SOLUTION + 1;

        public Part(string text) : base(text)
        {
        }
    }
}