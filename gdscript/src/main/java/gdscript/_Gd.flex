package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTypes;

%%

%class GdLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]
STRING_CH = [^\"\r\n\\]
CHAR_CH = [^'\r\n\\]
PRINTABLE = [\ -~]
HEX = [0-9a-f]
FLIT1 = [0-9]+ \. [0-9]*
FLIT2 = \. [0-9]+
FLIT3 = [0-9]+

NEW_LINE = [\r\n]+
WHITE_SPACE = {NEW_LINE} | [ \t]+
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NUMBER = {FLIT1}|{FLIT2}|{FLIT3}
STRING = \" (.)* \"
char = \' ( {CHAR_CH} | \\ {PRINTABLE} {HEX}* ) \'
COMMENT = "#"[^\r\n]*(\n|\r|\r\n)?

ASSIGN = "=" | ":=" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
OPERATOR = "+" | "-" | "*" | "/" | "%" | "^" | "&" | "|"
    | "<<" | ">>" | "!" | "&&" | "||"

%state WAITING_VALUE
%state AWAIT_NEW_LINE
%state AWAIT_STMT_END

%%

<AWAIT_NEW_LINE, AWAIT_STMT_END> {
    {NEW_LINE} { yybegin(YYINITIAL); return GdTypes.NEW_LINE; }
    <<EOF>>    { yybegin(YYINITIAL); return GdTypes.NEW_LINE; }

        <AWAIT_STMT_END> {
          ";"  { yybegin(YYINITIAL); return GdTypes.SEMICON; }
        }
}

<YYINITIAL, AWAIT_NEW_LINE, AWAIT_STMT_END> {
    "class_name"  { yybegin(AWAIT_NEW_LINE); return GdTypes.CLASS_NAME; }
    "extends"     { yybegin(AWAIT_NEW_LINE); return GdTypes.EXTENDS; }
    "tool"        { yybegin(AWAIT_NEW_LINE); return GdTypes.TOOL; }
    "var"         { yybegin(AWAIT_STMT_END); return GdTypes.VAR; }

    "class" { return GdTypes.CLASS; }
    "enum" { return GdTypes.ENUM; }
    "self" { return GdTypes.SELF; }
    "func" { return GdTypes.FUNC; }
    "const" { return GdTypes.CONST; }
    "if" { return GdTypes.IF; }
    "else" { return GdTypes.ELSE; }
    "elif" { return GdTypes.ELIF; }
    "int" { return GdTypes.INT; }
    "true" { return GdTypes.TRUE; }
    "false" { return GdTypes.FALSE; }
    "null" { return GdTypes.NULL; }

    "pass" { return GdTypes.PASS; }
    "setget" { return GdTypes.SETGET; }
    "signal" { return GdTypes.SIGNAL; }
    "static" { return GdTypes.STATIC; }
    "->" { return GdTypes.RET; }
    "_init" { return GdTypes.INIT; }
    "breakpoint" { return GdTypes.BREAKPOINT; }
    "while" { return GdTypes.WHILE; }
    "for" { return GdTypes.FOR; }
    "in" { return GdTypes.IN; }
    "match" { return GdTypes.MATCH; }
    "_" { return GdTypes.UNDER; }
    "continue" { return GdTypes.CONTINUE; }
    "break" { return GdTypes.BREAK; }
    "return" { return GdTypes.RETURN; }
    "assert" { return GdTypes.ASSERT; }
    "yield" { return GdTypes.YIELD; }
    "preload" { return GdTypes.PRELOAD; }
    "as" { return GdTypes.AS; }
    "and" { return GdTypes.AND; }
    "or" { return GdTypes.OR; }
    "PI" { return GdTypes.PI; }
    "TAU" { return GdTypes.TAU; }
    "NAN" { return GdTypes.NAN; }
    "INF" { return GdTypes.INF; }

    /* Annotations */
    "@onready" { return GdTypes.ONREADY; }
    "@export" { return GdTypes.EXPORT; }

    /* Syntax */
    "{" { return GdTypes.LCBR; }
    "}" { return GdTypes.RCBR; }
    "(" { return GdTypes.LRBR; }
    ")" { return GdTypes.RRBR; }
    "[" { return GdTypes.LSBR; }
    "]" { return GdTypes.RSBR; }
    ";" { return GdTypes.SEMICON; }
    "," { return GdTypes.COMMA; }
    ":" { return GdTypes.COLON; }
    "?" { return GdTypes.TERNARY; }
    ".." { return GdTypes.DOTDOT; }
    "." { return GdTypes.DOT; }
    "=" { return GdTypes.EQ; }

    {OPERATOR} { return GdTypes.OPERATOR; }
    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
    {ASSIGN} { return GdTypes.ASSIGN; }
    {STRING} { return GdTypes.STRING; }
    {NUMBER} { return GdTypes.NUMBER; }
    {IDENTIFIER} { return GdTypes.IDENTIFIER; }
    {WHITE_SPACE} { return TokenType.WHITE_SPACE; }
    {COMMENT} { return GdTypes.COMMENT; }
}

[^] { return GdTypes.BAD_CHARACTER; }