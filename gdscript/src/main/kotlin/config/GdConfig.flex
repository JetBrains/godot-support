package config;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import config.psi.GdConfigTypes;
import java.util.Stack;

%%

%class GdConfigLexer
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
NUMBER = [0-9]+
ONE_NL = \R
WHITE_SPACE = " " | \t | \f | \R

VARIADIC = "_variadic"

OPERAND = "+" | "-" | "*" | "/" | "<" | ">" | "!=" | "==" | "^"
  | "<=" | ">=" | "[]" | "%" | "<<" | ">>" | "**" | "&"  | "|"

IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*

%%
"AN"          { return GdConfigTypes.AN; }
"OP"          { return GdConfigTypes.OP; }
":"           { return GdConfigTypes.COLON; }

{VARIADIC}    { return GdConfigTypes.VARIADIC; }
{NUMBER}      { return GdConfigTypes.NUMBER; }

{OPERAND}     { return GdConfigTypes.OPERAND; }
{IDENTIFIER}  { return GdConfigTypes.IDENTIFIER; }

{WHITE_SPACE} { return TokenType.WHITE_SPACE; }

<<EOF>> {
    return null;
}

[^] { return TokenType.BAD_CHARACTER; }
