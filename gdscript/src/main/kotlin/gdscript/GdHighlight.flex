package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTokenType;
import gdscript.psi.GdTypes;
import java.util.Stack;

%%

%class GdLexerHighlighter
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
    String oppening = "";
    int lastState = YYINITIAL;
%}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]

NEW_LINE = [\r\n]
INDENT = [ \t]+
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NUMBER = [0-9][0-9_]*(\.[0-9_]+)?
HEX_NUMBER = 0x[0-9_a-fA-F]+
BIN_NUMBER = 0b[01_]+
REAL_NUMBER = {NUMBER}e-[0-9]+

STRING = "&"?\"([^\\\"\r\n]|\\.)*\"
STRING_CHAR = \'([^\\\'\r\n]|\\.)*\'
STRING_MULTILINE = \"\"\"(\R|.)*\"\"\"

COMMENT = "#"[^\r\n]*
ANNOTATOR = "@"[a-zA-Z_]*
NODE_PATH = "^"\"([^\\\"\r\n]|\\.)*\"
NODE_PATH_LEX = ( ("$"|"%")[\%a-zA-Z0-9_/]* ) | ( ("$"|"%")\"[\%a-zA-Z0-9:_/\.]*\" )

ASSIGN = "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
//OPERATOR = "+" | "-" | "*" | "/" | "%" | "^" | "&" | "|"
//    | "<<" | ">>" | "!" | "&&" | "||"

%%

    "extends"      { return GdTypes.EXTENDS; }
    "class_name"   { return GdTypes.CLASS_NAME; }
    "var"          { return GdTypes.VAR; }
    "const"        { return GdTypes.CONST; }
    "get"          { return GdTypes.GET; }
    "set"          { return GdTypes.SET; }

    "enum"         { return GdTypes.ENUM; }
    "func"         { return GdTypes.FUNC; }
    "pass"         { return GdTypes.PASS; }
    "true"         { return GdTypes.TRUE; }
    "false"        { return GdTypes.FALSE; }
    "null"         { return GdTypes.NULL; }
    "self"         { return GdTypes.SELF; }
    "continue"     { return GdTypes.CONTINUE; }
    "breakpoint"   { return GdTypes.BREAKPOINT; }
    "break"        { return GdTypes.BREAK; }
    "return"       { return GdTypes.RETURN; }
    "void"         { return GdTypes.VOID; }
    "nan"          { return GdTypes.NAN; }
    "inf"          { return GdTypes.INF; }
    "signal"       { return GdTypes.SIGNAL; }
    "in"           { return GdTypes.IN; }
    "if"           { return GdTypes.IF; }
    "else"         { return GdTypes.ELSE; }
    "elif"         { return GdTypes.ELIF; }
    "as"           { return GdTypes.AS; }
    "is"           { return GdTypes.IS; }
    "while"        { return GdTypes.WHILE; }
    "for"          { return GdTypes.FOR; }
    "in"           { return GdTypes.IN; }
    "match"        { return GdTypes.MATCH; }
    "await"        { return GdTypes.AWAIT; }
    "static"       { return GdTypes.STATIC; }
    "vararg"       { return GdTypes.VARARG; }
    "class"        { return GdTypes.CLASS; }
    "super"        { return GdTypes.SUPER; }
    "master"       { return GdTypes.MASTER; }
    "pupper"       { return GdTypes.PUPPET; }
    "remote"       { return GdTypes.REMOTE; }
    "remotesync"   { return GdTypes.REMOTESYNC; }

    "*"            { return GdTypes.MUL; }
    "**"           { return GdTypes.POWER; }
    "/"            { return GdTypes.DIV; }
    "%"            { return GdTypes.MOD; }
    "+"            { return GdTypes.PLUS; }
    "-"            { return GdTypes.MINUS; }
    "++"           { return GdTypes.PPLUS; }
    "--"           { return GdTypes.MMINUS; }
    "."            { return GdTypes.DOT; }
    ","            { return GdTypes.COMMA; }
    ":="           { return GdTypes.CEQ; }
    ":"            { return GdTypes.COLON; }
    ";"            { return GdTypes.SEMICON; }
    "!"            { return GdTypes.NEGATE; }
    "not"          { return GdTypes.NEGATE; }
    "="            { return GdTypes.EQ; }
    "->"           { return GdTypes.RET; }
    ">>"           { return GdTypes.RBSHIFT; }
    "<<"           { return GdTypes.LBSHIFT; }
    "("            { return GdTypes.LRBR; }
    ")"            { return GdTypes.RRBR; }
    "["            { return GdTypes.LSBR; }
    "]"            { return GdTypes.RSBR; }
    "{"            { return GdTypes.LCBR; }
    "}"            { return GdTypes.RCBR; }
    "&"            { return GdTypes.AND; }
    "&&"           { return GdTypes.ANDAND; }
    "and"          { return GdTypes.ANDAND; }
    "|"            { return GdTypes.OR; }
    "||"           { return GdTypes.OROR; }
    "or"           { return GdTypes.OROR; }
    "^"            { return GdTypes.XOR; }
    "~"            { return GdTypes.NOT; }
    "_"            { return GdTypes.UNDER; }
    ".."           { return GdTypes.DOTDOT; }
    "\\"           { return GdTypes.BACKSLASH; }

    {NODE_PATH}        { return GdTypes.NODE_PATH_LIT; }
    {NODE_PATH_LEX}    { return GdTypes.NODE_PATH_LEX; }
    {STRING}           { if (yytext().charAt(0) == '&') { return GdTypes.STRING_NAME; } return GdTypes.STRING; }
    {STRING_CHAR}      { return GdTypes.STRING; }
    {STRING_MULTILINE} { return GdTypes.STRING; }
    {ASSIGN}        { return GdTypes.ASSIGN; }
    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
    {ANNOTATOR}     { return GdTypes.ANNOTATOR; }
    {IDENTIFIER}    { return GdTypes.IDENTIFIER; }
    {REAL_NUMBER}   { return GdTypes.NUMBER; }
    {NUMBER}        { return GdTypes.NUMBER; }
    {HEX_NUMBER}    { return GdTypes.NUMBER; }
    {BIN_NUMBER}    { return GdTypes.NUMBER; }
    {COMMENT}       { return GdTypes.COMMENT; }
    {INDENT}        { return TokenType.WHITE_SPACE; }
    {NEW_LINE}      { return TokenType.WHITE_SPACE; }

<<EOF>> {
    return null;
}

[^] { return TokenType.BAD_CHARACTER; }
