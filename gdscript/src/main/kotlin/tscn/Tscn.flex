package tscn;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import tscn.psi.TscnTokenType;
import tscn.psi.TscnTypes;
import java.util.Stack;

%%

%class TscnLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
%}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]

NEW_LINE = [\r\n]
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*

VALUE = [^\r\n\s\]]+
DATA_LINE = [^\r\n]+

WHITE_SPACE = [ \t]+
COMMENT = ";"[^\r\n]*(\n|\r|\r\n)?

%xstate HEADER
%xstate VALUE

%%

<HEADER> {
    "gd_scene"     { return TscnTypes.GD_SCENE; }
    "ext_resource" { return TscnTypes.EXT_RESOURCE; }
    "sub_resource" { return TscnTypes.SUB_RESOURCE; }
    "node"         { return TscnTypes.NODE; }
    "connection"   { return TscnTypes.CONNECTION; }
    {IDENTIFIER}   { return TscnTypes.IDENTIFIER; }
    {WHITE_SPACE}  { return TokenType.WHITE_SPACE; }
    "]"            { yybegin(YYINITIAL); return TscnTypes.RSBR; }
    "="            { yybegin(VALUE); return TscnTypes.EQ; }
}

<VALUE> {
    {VALUE}        { yybegin(HEADER); return TscnTypes.VALUE; }
}

"["                { yybegin(HEADER); return TscnTypes.LSBR; }
{DATA_LINE}        {
                      if (yytext().charAt(0) == '[') {
                          yypushback(yylength() - 1);
                          yybegin(HEADER);

                          return TscnTypes.LSBR;
                      } else {
                          return TscnTypes.DATA_LINE;
                      }
                   }
{COMMENT}          { return TscnTypes.COMMENT; }
{NEW_LINE}         { return TokenType.WHITE_SPACE; }

<<EOF>> {
    return null;
}

[^] { return TscnTypes.BAD_CHARACTER; }