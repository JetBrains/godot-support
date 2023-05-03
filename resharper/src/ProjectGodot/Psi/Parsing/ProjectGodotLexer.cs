using JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing.TokenNodeTypes;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;

namespace JetBrains.ReSharper.Plugins.Godot.ProjectGodot.Psi.Parsing;

public partial class ProjectGodotLexer
{
  public void Start()
  {
    Start(0, yy_buffer.Length, YYINITIAL);
  }

  public void Advance()
  {
    currentTokenType = null;
    LocateToken();
  }

  private void LocateToken()
  {
    if (currentTokenType == null)
    {
      currentTokenType = _locateToken();
    }
  }

  private class TokenPosition
  {
    public TokenNodeType CurrTokenType;
    public int YyBufferIndex;
    public int YyBufferStart;
    public int YyBufferEnd;
    public int YyLexicalState;
  }

  public object CurrentPosition
  {
    get =>
      new TokenPosition
      {
        CurrTokenType = currentTokenType,
        YyBufferIndex = yy_buffer_index,
        YyBufferStart = yy_buffer_start,
        YyBufferEnd = yy_buffer_end,
        YyLexicalState = yy_lexical_state
      };
    set
    {
      var tokenPosition = (TokenPosition) value;
      currentTokenType = tokenPosition.CurrTokenType;
      yy_buffer_index = tokenPosition.YyBufferIndex;
      yy_buffer_start = tokenPosition.YyBufferStart;
      yy_buffer_end = tokenPosition.YyBufferEnd;
      yy_lexical_state = tokenPosition.YyLexicalState;
    }
  }

  public TokenNodeType TokenType
  {
    get
    {
      LocateToken();
      return currentTokenType;
    }
  }

  public int TokenStart
  {
    get
    {
      LocateToken();
      return yy_buffer_start;
    }
  }

  public int TokenEnd
  {
    get
    {
      LocateToken();
      return yy_buffer_end;
    }
  }

  public IBuffer Buffer => yy_buffer;
  public uint LexerStateEx => (uint) yy_lexical_state;

  public int EOFPos => yy_eof_pos;
  public int LexemIndent => 0;

  public void Start(int startOffset, int endOffset, uint state)
  {
    yy_buffer_index = startOffset;
    yy_buffer_start = startOffset;
    yy_buffer_end = startOffset;

    yy_eof_pos = endOffset;

    yy_lexical_state = (int) state;

    currentTokenType = null;
  }

  private ProjectGodotTokenType GetToken(ProjectGodotTokenType token)
  {
    currentTokenType = token;
    if (token == GodotInitTokenNodeTypes.WHITESPACE)
    {
      return token;
    }

    if (token == GodotInitTokenNodeTypes.NEWLINE)
    {
      yybegin(YYINITIAL);
    }
    else
    {
      yybegin(YY_IN_LINE);
    }

    return token;
  }
}