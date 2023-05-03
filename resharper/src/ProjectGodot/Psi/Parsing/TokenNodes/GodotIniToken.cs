using System.Text;
using JetBrains.ReSharper.Plugins.Godot.GodotIni.Psi;
using JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing.TokenNodeTypes;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing.TokenNodes;

public class ProjectGodotToken : LeafElementBase, ITokenNode
{
  private readonly string myText;
  private readonly ProjectGodotTokenType myType;

  public ProjectGodotToken(string text, ProjectGodotTokenType type)
  {
    myText = text;
    myType = type;
  }

  public override int GetTextLength()
  {
    return myText.Length;
  }

  public override StringBuilder GetText(StringBuilder to)
  {
    to.Append(GetText());
    return to;
  }

  public override IBuffer GetTextAsBuffer()
  {
    return new StringBuffer(GetText());
  }

  public override string GetText()
  {
    return myText;
  }

  public override string ToString()
  {
    return myType.ToString();
  }

  public override NodeType NodeType => myType;
  public override PsiLanguageType Language => ProjectGodotLanguage.Instance;

  public TokenNodeType GetTokenType()
  {
    return myType;
  }
}