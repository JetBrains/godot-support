package tscn;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import tscn.psi.TscnTypes;
import java.util.Stack;

%%

%class TscnLexerV2
%implements FlexLexer
%unicode
%function advance
%type IElementType

%{
    Stack<Integer> nestedStates = new Stack<>();

    private IElementType determineStringType(CharSequence text) {
        if (text.length() >= 8 && text.subSequence(0, 7).equals("\"res://")) {
            return TscnTypes.RES_STRING_VALUE;
        }
        return TscnTypes.STRING_VALUE;
    }
%}

%eof{  return;
%eof}

LETTER = [a-z|A-Z_\-0-9]
DIGIT = [0-9]

NEW_LINE = [\r\n]
IDENTIFIER = {LETTER}({LETTER}|{DIGIT}|\/)*
DATA_IDENTIFIER = {LETTER}[^\=\r\n ]*

STRING = \"[^\r\n\"]*\"
GODOT_OBJECT_START = {LETTER}+\(
VALUE = {LETTER}({LETTER}|\.)*

WHITE_SPACE = [ \t]+
WHITE_SPACE_ANY = {WHITE_SPACE}|{NEW_LINE}
COMMENT = ";"[^\r\n]*(\n|\r|\r\n)?

%xstate DATA_LINE
%xstate HEADER
%xstate HEADER_ATTRIBUTES
%xstate HEADER_ATTR
%xstate HEADER_ATTR_VALUE
%xstate GODOT_OBJECT
%xstate GODOT_MEMBER_REF
%xstate DATA_VALUE
%xstate JSON_OBJECT
%xstate JSON_OBJECT_ELEM
%xstate JSON_ARRAY
%xstate JSON_VALUE

%%

<YYINITIAL> {
    "["               { yybegin(HEADER); nestedStates.clear(); return TscnTypes.LSBR; }
    {DATA_IDENTIFIER} { yybegin(DATA_LINE); nestedStates.clear(); return TscnTypes.DATA_LINE_NM; }
    {COMMENT}         { return TscnTypes.COMMENT; }
    {WHITE_SPACE_ANY} { return TokenType.WHITE_SPACE; }
}

<HEADER> {
    "gd_scene"     { yybegin(HEADER_ATTRIBUTES); return TscnTypes.GD_SCENE; }
    "ext_resource" { yybegin(HEADER_ATTRIBUTES); return TscnTypes.EXT_RESOURCE; }
    "node"         { yybegin(HEADER_ATTRIBUTES); return TscnTypes.NODE; }
    "connection"   { yybegin(HEADER_ATTRIBUTES); return TscnTypes.CONNECTION; }
    {IDENTIFIER}   { yybegin(HEADER_ATTRIBUTES); return TscnTypes.UNKNOWN; }
    [^]            { return TscnTypes.BAD_CHARACTER; }
}

<HEADER_ATTRIBUTES> {
    {WHITE_SPACE}  { return TokenType.WHITE_SPACE; }
    {IDENTIFIER}   { yybegin(HEADER_ATTR); return TscnTypes.HEADER_VALUE_NM; }
    \]             { yybegin(YYINITIAL); return TscnTypes.RSBR; }
    [^]            { return TscnTypes.BAD_CHARACTER; }
}

<HEADER_ATTR> {
    {WHITE_SPACE}  { yybegin(HEADER_ATTRIBUTES); return TokenType.WHITE_SPACE; }
    \=             { yybegin(HEADER_ATTR_VALUE); return TscnTypes.EQ; }
    [^]            { return TscnTypes.BAD_CHARACTER; }
}

<HEADER_ATTR_VALUE> {
    {STRING}              { yybegin(HEADER_ATTRIBUTES); return determineStringType(yytext()); }
    {GODOT_OBJECT_START}  { nestedStates.push(HEADER_ATTRIBUTES); yypushback(1); yybegin(GODOT_OBJECT); return TscnTypes.GODOT_CLASS_REF; }
    {VALUE}               { yybegin(HEADER_ATTRIBUTES); return TscnTypes.VALUE; }
    {WHITE_SPACE}         { yybegin(HEADER_ATTRIBUTES); return TokenType.WHITE_SPACE; }
    [^]                   { return TscnTypes.BAD_CHARACTER; }
}

<DATA_LINE> {
    {WHITE_SPACE}        { return TokenType.WHITE_SPACE; }
    \=                   { return TscnTypes.EQ; }
    \{                   { yybegin(JSON_OBJECT); nestedStates.push(DATA_LINE); return TscnTypes.LCBR; }
    \[                   { yybegin(JSON_ARRAY); nestedStates.push(DATA_LINE); return TscnTypes.LSBR; }
    \&                   { yybegin(GODOT_MEMBER_REF); nestedStates.push(DATA_LINE); return TscnTypes.AMPERSAND; }
    {STRING}             { return determineStringType(yytext()); }
    {GODOT_OBJECT_START} { nestedStates.push(DATA_LINE); yypushback(1); yybegin(GODOT_OBJECT); return TscnTypes.GODOT_CLASS_REF; }
    {VALUE}              { return TscnTypes.VALUE; }
    {NEW_LINE}           { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
    [^]                  { return TscnTypes.BAD_CHARACTER; }
}

<GODOT_OBJECT> {
    \(             { return TscnTypes.LP; }
    \&             { yybegin(GODOT_MEMBER_REF); nestedStates.push(GODOT_OBJECT); return TscnTypes.AMPERSAND; }
    {STRING}       { return determineStringType(yytext()); }
    {VALUE}        { return TscnTypes.VALUE; }
    \,             { return TscnTypes.COMMA; }
    {WHITE_SPACE}  { return TokenType.WHITE_SPACE; }
    \)             { yybegin(nestedStates.pop()); return TscnTypes.RP; }
    [^]            { return TscnTypes.BAD_CHARACTER; }
}

<GODOT_MEMBER_REF> {
    {STRING}    { yybegin(nestedStates.pop()); return TscnTypes.GODOT_MEMBER_REF; }
    [^]         { return TscnTypes.BAD_CHARACTER; }
}

<JSON_OBJECT> {
    {WHITE_SPACE_ANY}  { return TokenType.WHITE_SPACE; }
    {STRING}           { nestedStates.push(JSON_OBJECT); yybegin(JSON_OBJECT_ELEM); return TscnTypes.STRING_VALUE; }
    \,                 { return TscnTypes.COMMA; }
    \}                 { yybegin(nestedStates.pop()); return TscnTypes.RCBR; }
    [^]                { return TscnTypes.BAD_CHARACTER; }
}

<JSON_OBJECT_ELEM> {
    {WHITE_SPACE_ANY}  { return TokenType.WHITE_SPACE; }
    \:                 { nestedStates.push(JSON_OBJECT_ELEM); yybegin(JSON_VALUE); return TscnTypes.COLON; }
    \,                 { yybegin(nestedStates.pop()); return TscnTypes.COMMA; }
    \}                 { nestedStates.pop(); yybegin(nestedStates.pop()); return TscnTypes.RCBR; }
    [^]                { return TscnTypes.BAD_CHARACTER; }
}

<JSON_ARRAY> {
    {WHITE_SPACE_ANY}    { return TokenType.WHITE_SPACE; }
    \,                   { return TscnTypes.COMMA; }
    \{                   { nestedStates.push(JSON_ARRAY); yybegin(JSON_OBJECT); return TscnTypes.LCBR; }
    \&                   { yybegin(GODOT_MEMBER_REF); nestedStates.push(JSON_ARRAY); return TscnTypes.AMPERSAND; }
    {STRING}             { return determineStringType(yytext()); }
    {GODOT_OBJECT_START} { nestedStates.push(JSON_ARRAY); yypushback(1); yybegin(GODOT_OBJECT); return TscnTypes.GODOT_CLASS_REF; }
    {VALUE}              { return TscnTypes.VALUE; }
    \]                   { yybegin(nestedStates.pop()); return TscnTypes.RSBR; }
    [^]                  { return TscnTypes.BAD_CHARACTER; }
}

<JSON_VALUE> {
    {WHITE_SPACE_ANY}    { return TokenType.WHITE_SPACE; }
    \{                   { yybegin(JSON_OBJECT); return TscnTypes.LCBR; }
    \[                   { yybegin(JSON_ARRAY); return TscnTypes.LSBR; }
    \&                   { yybegin(GODOT_MEMBER_REF); return TscnTypes.AMPERSAND; }
    {STRING}             { yybegin(nestedStates.pop()); return determineStringType(yytext()); }
    {GODOT_OBJECT_START} { yypushback(1); yybegin(GODOT_OBJECT); return TscnTypes.GODOT_CLASS_REF; }
    {VALUE}              { yybegin(nestedStates.pop()); return TscnTypes.VALUE; }
    [^]                  { return TscnTypes.BAD_CHARACTER; }
}

[^] { return TscnTypes.BAD_CHARACTER; }
