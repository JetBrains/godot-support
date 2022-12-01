package project;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import project.psi.ProjectTypes;
import java.util.Stack;

%%

%class ProjectLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
%}

KEY_CHAR = [^=\r\n]

COMMENT = ";"[^\r\n]*
SECTION_KEY = "["{KEY_CHAR}+"]"

NEW_LINE = [\r\n]

KEY = {KEY_CHAR}+
KEYED = {KEY}"="

//VALUE = (.*(\?:\n(?!.+\=).*)*)
VALUE = [^\r\n=]*
VALUE_LINE = !(!{VALUE}|{SECTION_KEY})
EQ = "="

%xstate KEYED_STATE
%xstate VALUED_STATE

%%

<KEYED_STATE> {
    {KEY}          { return ProjectTypes.KEY; }
    {EQ}           { yybegin(VALUED_STATE); return ProjectTypes.EQ; }
}

<VALUED_STATE> {
    {KEYED}        { yybegin(YYINITIAL); yypushback(yylength()); continue; }
    {SECTION_KEY}  { yybegin(YYINITIAL); return ProjectTypes.SECTION_KEY; }
    {VALUE_LINE}   { return ProjectTypes.VALUE; }

    {COMMENT}      { return ProjectTypes.COMMENT; }
    {NEW_LINE}     { return TokenType.WHITE_SPACE; }

    <<EOF>> {
        return null;
    }
}

{SECTION_KEY}      { return ProjectTypes.SECTION_KEY; }
{KEYED}            { yybegin(KEYED_STATE); yypushback(yylength()); continue; }

{COMMENT}          { return ProjectTypes.COMMENT; }
{NEW_LINE}         { return TokenType.WHITE_SPACE; }

<<EOF>> {
    return null;
}

[^] { return ProjectTypes.BAD_CHARACTER; }