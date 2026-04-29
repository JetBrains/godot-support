package tscn;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static tscn.psi.TscnTypes.*;

%%

%{
  public _TscnLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _TscnLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

COMMENT=;.*
BAD_CHARACTER=[\^]

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return WHITE_SPACE; }

  "LSBR"                { return LSBR; }
  "GD_SCENE"            { return GD_SCENE; }
  "RSBR"                { return RSBR; }
  "NODE"                { return NODE; }
  "EXT_RESOURCE"        { return EXT_RESOURCE; }
  "CONNECTION"          { return CONNECTION; }
  "IDENTIFIER"          { return IDENTIFIER; }
  "EQ"                  { return EQ; }
  "SLASH"               { return SLASH; }
  "NUMBER"              { return NUMBER; }
  "TRUE"                { return TRUE; }
  "FALSE"               { return FALSE; }
  "NULL"                { return NULL; }
  "STRING"              { return STRING; }
  "STRING_REF"          { return STRING_REF; }
  "PLUS"                { return PLUS; }
  "MINUS"               { return MINUS; }
  "LRBR"                { return LRBR; }
  "RRBR"                { return RRBR; }
  "COMMA"               { return COMMA; }
  "OBJECT_REF"          { return OBJECT_REF; }
  "IDENTIFIER_REF"      { return IDENTIFIER_REF; }
  "COLON"               { return COLON; }
  "LCBR"                { return LCBR; }
  "RCBR"                { return RCBR; }

  {COMMENT}             { return COMMENT; }
  {BAD_CHARACTER}       { return BAD_CHARACTER; }

}

[^] { return BAD_CHARACTER; }
