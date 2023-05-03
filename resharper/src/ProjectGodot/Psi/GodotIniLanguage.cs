using JetBrains.Annotations;
using JetBrains.ReSharper.Psi;

namespace JetBrains.ReSharper.Plugins.Godot.GodotIni.Psi;

[LanguageDefinition(Name)]
public class ProjectGodotLanguage : KnownLanguage
{
  public new const string Name = "ProjectGodot";

  public static ProjectGodotLanguage Instance { get; private set; }
    
  private ProjectGodotLanguage() : base(Name, Name) { }
    
  protected ProjectGodotLanguage([NotNull] string name) : base(name) { }

  protected ProjectGodotLanguage([NotNull] string name, [NotNull] string presentableName) : base(name, presentableName) { }
}