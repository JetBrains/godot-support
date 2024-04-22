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

[ShellComponent(Instantiation.DemandAnyThread)]
public class GodotChatContextPartProvider : IChatContextPartProvider
{
  public int Priority => ChatContextPartPriorities.SOLUTION + 1;

  public bool IsApplicable(IDataContext dataContext)
  {
    return true;
  }

  public void ContributeTo(IDataContext dataContext, ChatContextPartSet parts)
  {
    var solution = dataContext.GetData(ProjectModelDataConstants.SOLUTION);
    if (solution == null)
      return;

    var tracker = solution.TryGetComponent<GodotTracker>();
    if (tracker == null) return;

    if (tracker.GodotDescriptor == null) return;

    if (parts.Contains<TechnologyChatContextPartProvider.Part>())
      parts.Remove<TechnologyChatContextPartProvider.Part>();
    
    var text = new StringBuilder();
    text.Append("You are working with Godot project.\n");
    if (tracker.GodotDescriptor.IsPureGdScriptProject)
        text.Append("You are mostly using GDScript language.\n");
    else
    {
        var sdk = tracker.MainProject.ProjectProperties.DotNetCorePlatform?.Sdk;
        if (sdk != null)
        {
            text.Append($"You are using Project Sdk: \"{sdk}\".\n");
            text.Append($"You are mostly using C# language.\n");
        }
    }
    
    parts.Add(new Part(text.ToString()));
  }
  
  public sealed class Part : SimpleChatContextPart<Part>
  {
    public override int Priority => ChatContextPartPriorities.SOLUTION + 1;

    public Part(string text) : base(text)
    {
    }
  }
}