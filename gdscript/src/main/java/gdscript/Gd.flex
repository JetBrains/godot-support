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
%column
%eof{  return;
%eof}

%{
    int indent = 0;
    Stack<Integer> indentSizes = new Stack<>();
    int yycolumn;

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
%}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]
//STRING_CH = [^\"\r\n\\]
//CHAR_CH = [^'\r\n\\]
//PRINTABLE = [\ -~]
//HEX = [0-9a-f]
FLIT1 = [0-9]+ \. [0-9]*
FLIT2 = \. [0-9]+
FLIT3 = [0-9]+

NEW_LINE = [\r\n]
INDENT = [ \t]+
//WHITE_SPACE = {NEW_LINE} | {INDENT}
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NUMBER = {FLIT1}|{FLIT2}|{FLIT3}
STRING = \"(.)*\"
COMMENT = "#"[^\r\n]*(\n|\r|\r\n)?
ANNOTATOR = "@"[a-z|A-Z]*

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
    "const"        { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.CONST); }
    "var"          { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.VAR); }
    "setget"       { return GdTypes.SETGET; }

    "pass"         { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.PASS); }
    "true"         { return dedentRoot(GdTypes.TRUE); }
    "false"        { return dedentRoot(GdTypes.FALSE); }
    "null"         { return dedentRoot(GdTypes.NULL); }
    "int"          { return dedentRoot(GdTypes.INT); }
    "String"       { return dedentRoot(GdTypes.STR); }
    "self"         { return dedentRoot(GdTypes.SELF); }
    "continue"     { return dedentRoot(GdTypes.CONTINUE); }
    "break"        { return dedentRoot(GdTypes.BREAK); }
    "return"       { return dedentRoot(GdTypes.RETURN); }
    "void"         { return GdTypes.VOID; }
    "PI"           { return dedentRoot(GdTypes.PI); }
    "TAU"          { return dedentRoot(GdTypes.TAU); }
    "NAN"          { return dedentRoot(GdTypes.NAN); }
    "INF"          { return dedentRoot(GdTypes.INF); }

    "."            { return GdTypes.DOT; }
    ","            { return dedentRoot(GdTypes.COMMA); }
    ":"            { return dedentRoot(GdTypes.COLON); }
    ";"            { yybegin(YYINITIAL); return dedentRoot(GdTypes.SEMICON); }
    "="            { return dedentRoot(GdTypes.EQ); }
    "->"           { return GdTypes.RET; }
    "("            { return dedentRoot(GdTypes.LRBR); }
    ")"            { return dedentRoot(GdTypes.RRBR); }

    {ANNOTATOR}    { return GdTypes.ANNOTATOR; }
    {IDENTIFIER}   { return dedentRoot(GdTypes.IDENTIFIER); }
    {NUMBER}       { return dedentRoot(GdTypes.NUMBER); }
    {STRING}       { return dedentRoot(GdTypes.STRING); }
    {COMMENT}      { return GdTypes.COMMENT; }

    {INDENT}  {
        int spaces = yytext().length();
        if (yycolumn <= spaces) {
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
    } else {
        return null;
    }
}

//<AWAIT_NEW_LINE, AWAIT_STMT_END> {
//<YYINITIAL, AWAIT_NEW_LINE, AWAIT_STMT_END> {
//
//    "class" { return GdTypes.CLASS; }
//    "enum" { return GdTypes.ENUM; }
//    "if" { return GdTypes.IF; }
//    "else" { return GdTypes.ELSE; }
//    "elif" { return GdTypes.ELIF; }
//
//    "signal" { return GdTypes.SIGNAL; }
//    "static" { return GdTypes.STATIC; }
//    "breakpoint" { return GdTypes.BREAKPOINT; }
//    "while" { return GdTypes.WHILE; }
//    "for" { return GdTypes.FOR; }
//    "in" { return GdTypes.IN; }
//    "match" { return GdTypes.MATCH; }
//    "_" { return GdTypes.UNDER; }
//    "assert" { return GdTypes.ASSERT; }
//    "yield" { return GdTypes.YIELD; }
//    "preload" { return GdTypes.PRELOAD; }
//    "as" { return GdTypes.AS; }
//    "and" { return GdTypes.AND; }
//    "or" { return GdTypes.OR; }
//
//    /* Syntax */
//    "{" { return GdTypes.LCBR; }
//    "}" { return GdTypes.RCBR; }
//    "[" { return GdTypes.LSBR; }
//    "]" { return GdTypes.RSBR; }
//    "?" { return GdTypes.TERNARY; }
//    ".." { return GdTypes.DOTDOT; }
//
//
//    {OPERATOR} { return GdTypes.OPERATOR; }
//    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
//    {ASSIGN} { return GdTypes.ASSIGN; }
//}

[^] { return GdTypes.BAD_CHARACTER; }