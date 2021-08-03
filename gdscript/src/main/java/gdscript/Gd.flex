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
    boolean lineEnded = false;
    int indent = 0;
    Stack<Integer> indentSizes = new Stack<>();
    int yycolumn;

    public IElementType dedentRoot(IElementType type) {
        lineEnded = false;
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

ASSIGN = "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
//OPERATOR = "+" | "-" | "*" | "/" | "%" | "^" | "&" | "|"
//    | "<<" | ">>" | "!" | "&&" | "||"

%state AWAIT_NEW_LINE
%state AWAIT_NEW_LINE_ONCE

%%

<AWAIT_NEW_LINE_ONCE> {
    {NEW_LINE}     {
          yybegin(YYINITIAL);
          if (lineEnded) { // For signal, etc.
              return TokenType.WHITE_SPACE;
          }
          return GdTypes.NEW_LINE;
      }
}

<AWAIT_NEW_LINE> {
    {NEW_LINE}     {
          if (!lineEnded) {
              lineEnded = true;
              return GdTypes.NEW_LINE;
          }
          return TokenType.WHITE_SPACE;
      }
}

    "extends"      { yybegin(AWAIT_NEW_LINE_ONCE); return dedentRoot(GdTypes.EXTENDS); }
    "class_name"   { yybegin(AWAIT_NEW_LINE_ONCE); return dedentRoot(GdTypes.CLASS_NAME); }
    "tool"         { yybegin(AWAIT_NEW_LINE_ONCE); return dedentRoot(GdTypes.TOOL); }
    "var"          { yybegin(AWAIT_NEW_LINE_ONCE); return dedentRoot(GdTypes.VAR); }
    "const"        { yybegin(AWAIT_NEW_LINE_ONCE); return dedentRoot(GdTypes.CONST); }
    "setget"       { return GdTypes.SETGET; }

    "func"         { yybegin(AWAIT_NEW_LINE); return dedentRoot(GdTypes.FUNC); }
    "pass"         { return dedentRoot(GdTypes.PASS); }
    "true"         { return dedentRoot(GdTypes.TRUE); }
    "false"        { return dedentRoot(GdTypes.FALSE); }
    "null"         { return dedentRoot(GdTypes.NULL); }
    "int"          { return dedentRoot(GdTypes.INT); }
    "String"       { return dedentRoot(GdTypes.STR); }
    "self"         { return dedentRoot(GdTypes.SELF); }
    "continue"     { return dedentRoot(GdTypes.CONTINUE); }
    "breakpoint"   { return dedentRoot(GdTypes.BREAKPOINT); }
    "break"        { return dedentRoot(GdTypes.BREAK); }
    "return"       { return dedentRoot(GdTypes.RETURN); }
    "void"         { return dedentRoot(GdTypes.VOID); }
    "PI"           { return dedentRoot(GdTypes.PI); }
    "TAU"          { return dedentRoot(GdTypes.TAU); }
    "NAN"          { return dedentRoot(GdTypes.NAN); }
    "INF"          { return dedentRoot(GdTypes.INF); }
    "signal"       { yybegin(AWAIT_NEW_LINE_ONCE); return GdTypes.SIGNAL; }
    "in"           { return GdTypes.IN; }
    "if"           { return GdTypes.IF; }
    "else"         { return GdTypes.ELSE; }
    "elif"         { return GdTypes.ELIF; }
    "as"           { return GdTypes.AS; }
    "is"           { return GdTypes.IS; }

    "*"            { return GdTypes.MUL; }
    "/"            { return GdTypes.DIV; }
    "%"            { return GdTypes.MOD; }
    "+"            { return GdTypes.PLUS; }
    "-"            { return GdTypes.MINUS; }
    "++"           { return dedentRoot(GdTypes.PPLUS); }
    "--"           { return dedentRoot(GdTypes.MMINUS); }
    "."            { return GdTypes.DOT; }
    ","            { return GdTypes.COMMA; }
    ":"            { return GdTypes.COLON; }
    ";"            { lineEnded = true; return GdTypes.SEMICON; }
    "!"            { return GdTypes.NEGATE; }
    "not"          { return GdTypes.NEGATE; }
    "="            { return GdTypes.EQ; }
    "->"           { return GdTypes.RET; }
    ">>"           { return GdTypes.RBSHIFT; }
    "<<"           { return GdTypes.LBSHIFT; }
    "("            { return dedentRoot(GdTypes.LRBR); }
    ")"            { return dedentRoot(GdTypes.RRBR); }
    "&"            { return GdTypes.AND; }
    "&&"           { return GdTypes.ANDAND; }
    "and"          { return GdTypes.ANDAND; }
    "|"            { return GdTypes.OR; }
    "||"           { return GdTypes.OROR; }
    "or"           { return GdTypes.OROR; }
    "^"            { return GdTypes.XOR; }
    "~"            { return GdTypes.NOT; }

    {ASSIGN}        { return GdTypes.ASSIGN; }
    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
    {ANNOTATOR}     { return GdTypes.ANNOTATOR; }
    {IDENTIFIER}    { return dedentRoot(GdTypes.IDENTIFIER); }
    {NUMBER}        { return dedentRoot(GdTypes.NUMBER); }
    {STRING}        { return dedentRoot(GdTypes.STRING); }
    {COMMENT}       { return GdTypes.COMMENT; }

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
//
//    "static" { return GdTypes.STATIC; }
//    "while" { return GdTypes.WHILE; }
//    "for" { return GdTypes.FOR; }
//    "in" { return GdTypes.IN; }
//    "match" { return GdTypes.MATCH; }
//    "_" { return GdTypes.UNDER; }
//    "assert" { return GdTypes.ASSERT; }
//    "yield" { return GdTypes.YIELD; }
//    "preload" { return GdTypes.PRELOAD; }
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
//}

[^] { return GdTypes.BAD_CHARACTER; }