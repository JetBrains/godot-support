using JetBrains.Application;
using JetBrains.Application.DataContext;
using JetBrains.Application.Parts;
using JetBrains.ProjectModel;
using JetBrains.ProjectModel.DataContext;
using JetBrains.ReSharper.Feature.Services.ChatContexts.Common;
using JetBrains.ReSharper.Feature.Services.ChatContexts.DataProviders;
using JetBrains.ReSharper.Plugins.Godot.ProjectModel;
using JetBrains.ReSharper.Psi.CSharp;

namespace JetBrains.ReSharper.Plugins.Godot.AI;

[ShellComponent(Instantiation.DemandAnyThreadSafe)]
public class GodotPreferableLanguagesChatContextProvider : ISolutionLanguagesChatContextDataProvider
{
    public bool IsApplicable(IDataContext dataContext) => true;

    public void ContributeTo(IDataContext dataContext, ChatContextDataSet<LanguageDetails> data)
    {
        var solution = dataContext.GetData(ProjectModelDataConstants.SOLUTION);
        var tracker = solution?.TryGetComponent<GodotTracker>();
        if (tracker?.GodotDescriptor == null) return;
        if (tracker.GodotDescriptor?.IsPureGdScriptProject == true)
            data.Add(new LanguageDetails("GDScript"));
        else
        {
            var sdk = tracker.MainProject?.ProjectProperties.DotNetCorePlatform?.Sdk;
            if (sdk != null)
                data.Add(new LanguageDetails(CSharpLanguage.Name));
        }
    }
}