using JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing.TokenNodes;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;
using JetBrains.Util;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing.TokenNodeTypes;

public class ProjectGodotTokenType : TokenNodeType
{
  public ProjectGodotTokenType(string s, int index) : base(s, index) { }



  public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
  {
    return new ProjectGodotToken(buffer.GetText(new TextRange(startOffset.Offset, endOffset.Offset)), this);
  }

  public override bool IsWhitespace => this == GodotInitTokenNodeTypes.WHITESPACE || this == GodotInitTokenNodeTypes.NEWLINE;
  public override bool IsComment => this == GodotInitTokenNodeTypes.COMMENT;
  public override bool IsStringLiteral => this == GodotInitTokenNodeTypes.STRING_LITERAL;
  public override bool IsConstantLiteral => false;
  public override bool IsIdentifier => false;
  public override bool IsKeyword => false;
  public override string TokenRepresentation { get; }
}