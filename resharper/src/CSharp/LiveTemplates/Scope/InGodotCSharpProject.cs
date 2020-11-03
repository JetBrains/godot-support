using System;
using JetBrains.ProjectModel.Properties;
using JetBrains.ReSharper.Feature.Services.LiveTemplates.Scope;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.CSharp;

namespace JetBrains.ReSharper.Plugins.Godot.CSharp.LiveTemplates.Scope
{
    // Defines a scope point, but has no inherent behaviour, other than to compare against
    // other scope points. A template can declare that it requires this scope point, and
    // the template will only be made available if a ScopeProvider "publishes" this scope
    // point based on the current context
    public class InGodotCSharpProject : InLanguageSpecificProject
    {
        private static readonly Guid DefaultUID = new Guid("6B18575A-F13A-4950-98F8-99931D95EBA1");
        private static readonly Guid QuickUID = new Guid("FB6511D0-C4F8-40A3-B8E0-9776C6452AA5");

        public InGodotCSharpProject() : base(ProjectLanguage.CSHARP)
        {
            AdditionalSuperTypes.Add(typeof(InLanguageSpecificProject));
        }

        public override Guid GetDefaultUID() => DefaultUID;
        public override string PresentableShortName => "Godot projects";

        public override PsiLanguageType RelatedLanguage => CSharpLanguage.Instance;

        // Define the name and UID of the QuickList we'll use for Godot projects. Any
        // scope points that are subsets will appear in this QuickList (I think)
        public override string QuickListTitle => "Godot projects";
        public override Guid QuickListUID => QuickUID;
    }
}