package tscn;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import tscn.psi.TscnTokenType;
import tscn.psi.TscnTypes;
import java.util.List;
import java.util.HashMap;
import java.util.Stack;

%%

%class TscnLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

%{
%}

%init{
%init}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]

NUMBER = ( [0-9][0-9_]*(\.[0-9_]+)? ) | ( \.[0-9][0-9_]* ) | ( [0-9][0-9_]*\. )
REAL_NUMBER = {NUMBER}e-[0-9]+

IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
IDENTIFIER_REF = {LETTER}({LETTER}|{DIGIT})*"("

ONE_NL = \R
WHITE_SPACE = " " | \t | \f | \\ {ONE_NL}
COMMENT = ";"[^\r\n]*(\n|\r|\r\n)?

STRING_ESC = \\ [^] | \\ ({WHITE_SPACE})+ (\n|\r)
DOUBLE_QUOTED_CONTENT = {STRING_ESC} | [^\"]
DOUBLE_QUOTED_LITERAL = [\$\^]?\" {DOUBLE_QUOTED_CONTENT}* \"
STRING_REFERENCE = "&"{DOUBLE_QUOTED_LITERAL}

%%

// Node names
"gd_scene"        { return TscnTypes.GD_SCENE; }
"ext_resource"    { return TscnTypes.EXT_RESOURCE; }
"node"            { return TscnTypes.NODE; }
"connection"      { return TscnTypes.CONNECTION; }

"/"               { return TscnTypes.SLASH; }
"="               { return TscnTypes.EQ; }
":"               { return TscnTypes.COLON; }
","               { return TscnTypes.COMMA; }

"("               { return TscnTypes.LRBR; }
"["               { return TscnTypes.LSBR; }
"{"               { return TscnTypes.LCBR; }
")"               { return TscnTypes.RRBR; }
"]"               { return TscnTypes.RSBR; }
"}"               { return TscnTypes.RCBR; }

"true"            { return TscnTypes.TRUE; }
"false"           { return TscnTypes.FALSE; }
"null"            { return TscnTypes.NULL; }
{NUMBER}          { return TscnTypes.NUMBER; }
{REAL_NUMBER}     { return TscnTypes.NUMBER; }
"+"               { return TscnTypes.PLUS; }
"-"               { return TscnTypes.MINUS; }

{DOUBLE_QUOTED_LITERAL} { return TscnTypes.STRING; }
{STRING_REFERENCE}      { return TscnTypes.STRING_REF; }

{IDENTIFIER}      { return TscnTypes.IDENTIFIER; }
{IDENTIFIER_REF}  { yypushback(1); return TscnTypes.IDENTIFIER_REF; }
{COMMENT}         { return TscnTypes.COMMENT; }
{WHITE_SPACE}     { return TokenType.WHITE_SPACE; }
{ONE_NL}          { return TokenType.WHITE_SPACE; }

<<EOF>> {
    return null;
}

[^] { return TscnTypes.BAD_CHARACTER; }
