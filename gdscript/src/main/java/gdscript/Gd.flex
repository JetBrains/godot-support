package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTypes;
import java.util.Stack;

%%

%class GdLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%line
%column
%eof{  return;
%eof}

%{
    int indent = 0;
    Stack<Integer> indentSizes = new Stack<>();

    public IElementType dedentRoot(IElementType type) {
        if (yycolumn > 0 || indent <= 0 || indentSizes.empty()) {
            return type;
        }

        dedent();
        yypushback(yylength());

        return GdTypes.DEDENT;
    }

    public boolean dedentSpaces() {
        if (indent <= 0 || indentSizes.empty()) { // For EOF rule
            return false;
        }

        dedent();

        if (indent > yylength()) {
            yypushback(yylength());
        }

        return true;
    }

    private void dedent() {
        indent = Math.max(0, indent - indentSizes.pop());
    }

    int yyline;
    int yycolumn;
%}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]
//STRING_CH = [^\"\r\n\\]
//CHAR_CH = [^'\r\n\\]
//PRINTABLE = [\ -~]
//HEX = [0-9a-f]
//FLIT1 = [0-9]+ \. [0-9]*
//FLIT2 = \. [0-9]+
//FLIT3 = [0-9]+
//
NEW_LINE = [\r\n]+
INDENT = [ \t]+
//WHITE_SPACE = {NEW_LINE} | {INDENT}
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
//NUMBER = {FLIT1}|{FLIT2}|{FLIT3}
STRING = \"(.)*\"
COMMENT = "#"[^\r\n]*(\n|\r|\r\n)?

//ASSIGN = "=" | ":=" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|="
//TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
//OPERATOR = "+" | "-" | "*" | "/" | "%" | "^" | "&" | "|"
//    | "<<" | ">>" | "!" | "&&" | "||"

%state AWAIT_NEW_LINE

%%

<AWAIT_NEW_LINE> {
    {NEW_LINE}     { yybegin(YYINITIAL); return GdTypes.NEW_LINE; }
}

    "extends"      { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.EXTENDS); }
    "class_name"   { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.CLASS_NAME); }
    "func"         { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.FUNC); }
    "tool"         { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.TOOL); }
    "pass"         { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.PASS); }

    ","            { return dedentRoot(GdTypes.COMMA); }
    ":"            { return dedentRoot(GdTypes.COLON); }
    ";"            { yybegin(YYINITIAL); return dedentRoot(GdTypes.SEMICON); }

    {IDENTIFIER}   { return dedentRoot(GdTypes.IDENTIFIER); }
    {STRING}       { return dedentRoot(GdTypes.STRING); }
    {COMMENT}      { return GdTypes.COMMENT; }

    {INDENT}  {
        if (zzAtBOL) {
            int spaces = yytext().length();
            if (spaces == 1) spaces = 0;

            if (spaces > indent) {
                indentSizes.push(spaces - indent);
                indent += spaces;
                return GdTypes.INDENT;
            } else if (indent > spaces) {
                dedentSpaces();
                return GdTypes.DEDENT;
            }
        }

        return TokenType.WHITE_SPACE;
    }

    {NEW_LINE}     { return TokenType.WHITE_SPACE; }

<<EOF>> {
    if (dedentSpaces()) {
        return GdTypes.DEDENT;
    } else return null;
}

//<AWAIT_NEW_LINE, AWAIT_STMT_END> {
//    {NEW_LINE} { yybegin(YYINITIAL); return GdTypes.NEW_LINE; }
//    <<EOF>>    { yybegin(YYINITIAL); return GdTypes.NEW_LINE; }
//
//        <AWAIT_STMT_END> {
//          ";"  { yybegin(YYINITIAL); return GdTypes.SEMICON; }
//        }
//}
//
//<YYINITIAL, AWAIT_NEW_LINE, AWAIT_STMT_END> {
//    "class_name"  { yybegin(AWAIT_NEW_LINE); return GdTypes.CLASS_NAME; }
//    "extends"     { yybegin(AWAIT_NEW_LINE); return GdTypes.EXTENDS; }
//    "tool"        { yybegin(AWAIT_NEW_LINE); return GdTypes.TOOL; }
//    "var"         { yybegin(AWAIT_STMT_END); return GdTypes.VAR; }
//
//    "class" { return GdTypes.CLASS; }
//    "enum" { return GdTypes.ENUM; }
//    "self" { return GdTypes.SELF; }
//    "const" { return GdTypes.CONST; }
//    "if" { return GdTypes.IF; }
//    "else" { return GdTypes.ELSE; }
//    "elif" { return GdTypes.ELIF; }
//    "int" { return GdTypes.INT; }
//    "true" { return GdTypes.TRUE; }
//    "false" { return GdTypes.FALSE; }
//    "null" { return GdTypes.NULL; }
//
//    "setget" { return GdTypes.SETGET; }
//    "signal" { return GdTypes.SIGNAL; }
//    "static" { return GdTypes.STATIC; }
//    "->" { return GdTypes.RET; }
//    "_init" { return GdTypes.INIT; }
//    "breakpoint" { return GdTypes.BREAKPOINT; }
//    "while" { return GdTypes.WHILE; }
//    "for" { return GdTypes.FOR; }
//    "in" { return GdTypes.IN; }
//    "match" { return GdTypes.MATCH; }
//    "_" { return GdTypes.UNDER; }
//    "continue" { return GdTypes.CONTINUE; }
//    "break" { return GdTypes.BREAK; }
//    "return" { return GdTypes.RETURN; }
//    "assert" { return GdTypes.ASSERT; }
//    "yield" { return GdTypes.YIELD; }
//    "preload" { return GdTypes.PRELOAD; }
//    "as" { return GdTypes.AS; }
//    "and" { return GdTypes.AND; }
//    "or" { return GdTypes.OR; }
//    "PI" { return GdTypes.PI; }
//    "TAU" { return GdTypes.TAU; }
//    "NAN" { return GdTypes.NAN; }
//    "INF" { return GdTypes.INF; }
//
//    /* Annotations */
//    "@onready" { return GdTypes.ONREADY; }
//    "@export" { return GdTypes.EXPORT; }
//
//    /* Syntax */
//    "{" { return GdTypes.LCBR; }
//    "}" { return GdTypes.RCBR; }
//    "(" { return GdTypes.LRBR; }
//    ")" { return GdTypes.RRBR; }
//    "[" { return GdTypes.LSBR; }
//    "]" { return GdTypes.RSBR; }
//    "?" { return GdTypes.TERNARY; }
//    ".." { return GdTypes.DOTDOT; }
//    "." { return GdTypes.DOT; }
//    "=" { return GdTypes.EQ; }
//
//    {OPERATOR} { return GdTypes.OPERATOR; }
//    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
//    {ASSIGN} { return GdTypes.ASSIGN; }
//    {STRING} { return GdTypes.STRING; }
//    {NUMBER} { return GdTypes.NUMBER; }
//    {IDENTIFIER} { return GdTypes.IDENTIFIER; }
//    {WHITE_SPACE} { return TokenType.WHITE_SPACE; }
//    {COMMENT} { return GdTypes.COMMENT; }
//}

[^] { return GdTypes.BAD_CHARACTER; }