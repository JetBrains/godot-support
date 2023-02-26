package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTokenType;import gdscript.psi.GdTypes;
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
    String oppening = "";
    int lastState = YYINITIAL;
    boolean lineEnded = false;
    boolean enumValEnded = false;
    int indent = 0;
    Stack<Integer> indentSizes = new Stack<>();
    int yycolumn;
    boolean eofFinished = false;

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

NEW_LINE = [\r\n]
IGNORE_NEW_LINE = \\[\r\n]
INDENT = [ \t]+
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NUMBER = [0-9][0-9_]*(\.[0-9_]+)?
HEX_NUMBER = 0x[0-9_a-fA-F]+
BIN_NUMBER = 0b[01_]+
REAL_NUMBER = {NUMBER}e-[0-9]+

STRING = \"([^\\\"\r\n]|\\.)*\"
STRING_CHAR = \'([^\\\'\r\n]|\\.)*\'
STRING_MULTILINE = \"\"\"([^\\\"]|\\.)*\"\"\"

// TODO remove z obojích flexů
STRING_MARKER = \"\"\"|\"|\'
STRING_MARKER_REV = [^\"\'\n\r]*

COMMENT = [ \t]*"#"[^\r\n]*(\n|\r|\r\n)?
ANNOTATOR = "@"[a-zA-Z_]*
NODE_PATH_LEX = ("$"[a-zA-Z0-9_/]*) | ("$"\"[a-zA-Z0-9_/\.]*\") | (\%[a-zA-Z0-9_\./]*)

ASSIGN = "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
//OPERATOR = "+" | "-" | "*" | "/" | "%" | "^" | "&" | "|"
//    | "<<" | ">>" | "!" | "&&" | "||"
ANY = .+

%state AWAIT_ENUM_SEPARATOR
%state CREATE_INDENT
%xstate STRING

%%

<CREATE_INDENT> {
    {ANY} {
        yybegin(lastState);
        yypushback(yylength());

        return GdTypes.INDENT;
    }
}

<STRING> {
    // Stringers
    {STRING_MARKER} {
        if (oppening.equals(yytext().toString())) {
            yybegin(lastState);
            return GdTypes.STRING;
        }
    }

    {IGNORE_NEW_LINE} { return TokenType.WHITE_SPACE; }
    {NEW_LINE} {
        if (!oppening.equals("\"\"\"")) {
            yybegin(lastState);
            return TokenType.BAD_CHARACTER;
        }
    }

    {STRING_MARKER_REV} {
        continue;
    }

    <<EOF>> {
        yybegin(YYINITIAL);
        return GdTypes.STRING;
    }
}

<AWAIT_ENUM_SEPARATOR> {
    {IDENTIFIER} {
          enumValEnded = false;
          return GdTypes.IDENTIFIER;
    }
    "," {
          enumValEnded = true;
          return GdTypes.COMMA;
    }
    "{" {
          enumValEnded = true;
          return GdTypes.LCBR;
    }
    "}" {
          enumValEnded = false;
          lineEnded = false;
          return GdTypes.RCBR;
    }
}

    "extends"      { return dedentRoot(GdTypes.EXTENDS); }
    "class_name"   { return dedentRoot(GdTypes.CLASS_NAME); }
    "var"          { return dedentRoot(GdTypes.VAR); }
    "const"        { return dedentRoot(GdTypes.CONST); }
    "get"          { return dedentRoot(GdTypes.GET); }
    "set"          { return dedentRoot(GdTypes.SET); }

    "enum"         { yybegin(AWAIT_ENUM_SEPARATOR); enumValEnded = true; return GdTypes.ENUM; }
    "func"         { return dedentRoot(GdTypes.FUNC); }
    "pass"         { return dedentRoot(GdTypes.PASS); }
    "true"         { return dedentRoot(GdTypes.TRUE); }
    "false"        { return dedentRoot(GdTypes.FALSE); }
    "null"         { return dedentRoot(GdTypes.NULL); }
    "self"         { return dedentRoot(GdTypes.SELF); }
    "continue"     { return dedentRoot(GdTypes.CONTINUE); }
    "breakpoint"   { return dedentRoot(GdTypes.BREAKPOINT); }
    "break"        { return dedentRoot(GdTypes.BREAK); }
    "return"       { return dedentRoot(GdTypes.RETURN); }
    "void"         { return dedentRoot(GdTypes.VOID); }
    "inf"          { return dedentRoot(GdTypes.INF); }
    "nan"          { return dedentRoot(GdTypes.NAN); }
    "signal"       { return GdTypes.SIGNAL; }
    "in"           { return dedentRoot(GdTypes.IN); }
    "if"           { return dedentRoot(GdTypes.IF); }
    "else"         { return dedentRoot(GdTypes.ELSE); }
    "elif"         { return dedentRoot(GdTypes.ELIF); }
    "as"           { return dedentRoot(GdTypes.AS); }
    "is"           { return dedentRoot(GdTypes.IS); }
    "while"        { return dedentRoot(GdTypes.WHILE); }
    "for"          { return dedentRoot(GdTypes.FOR); }
    "in"           { return dedentRoot(GdTypes.IN); }
    "match"        { return dedentRoot(GdTypes.MATCH); }
    "await"        { return dedentRoot(GdTypes.AWAIT); }
    "static"       { return dedentRoot(GdTypes.STATIC); }
    "vararg"       { return dedentRoot(GdTypes.VARARG); }
    "class"        { return dedentRoot(GdTypes.CLASS); }
    "super"        { return dedentRoot(GdTypes.SUPER); }

    "*"            { return dedentRoot(GdTypes.MUL); }
    "/"            { return dedentRoot(GdTypes.DIV); }
    "%"            { return dedentRoot(GdTypes.MOD); }
    "+"            { return dedentRoot(GdTypes.PLUS); }
    "-"            { return dedentRoot(GdTypes.MINUS); }
    "++"           { return dedentRoot(GdTypes.PPLUS); }
    "--"           { return dedentRoot(GdTypes.MMINUS); }
    "."            { return dedentRoot(GdTypes.DOT); }
    ","            { return dedentRoot(GdTypes.COMMA); }
    ":="           { return dedentRoot(GdTypes.CEQ); }
    ":"            { return dedentRoot(GdTypes.COLON); }
    ";"            { lineEnded = true; return GdTypes.SEMICON; }
    "!"            { return dedentRoot(GdTypes.NEGATE); }
    "not"          { return dedentRoot(GdTypes.NEGATE); }
    "="            { return dedentRoot(GdTypes.EQ); }
    "->"           { return dedentRoot(GdTypes.RET); }
    ">>"           { return dedentRoot(GdTypes.RBSHIFT); }
    "<<"           { return dedentRoot(GdTypes.LBSHIFT); }
    "<<"           { return dedentRoot(GdTypes.LBSHIFT); }
    "("            { return dedentRoot(GdTypes.LRBR); }
    ")"            { return dedentRoot(GdTypes.RRBR); }
    "["            { return dedentRoot(GdTypes.LSBR); }
    "]"            { return dedentRoot(GdTypes.RSBR); }
    "{"            { return dedentRoot(GdTypes.LCBR); }
    "}"            { return dedentRoot(GdTypes.RCBR); }
    "&"            { return dedentRoot(GdTypes.AND); }
    "&&"           { return dedentRoot(GdTypes.ANDAND); }
    "and"          { return dedentRoot(GdTypes.ANDAND); }
    "|"            { return dedentRoot(GdTypes.OR); }
    "||"           { return dedentRoot(GdTypes.OROR); }
    "or"           { return dedentRoot(GdTypes.OROR); }
    "^"            { return dedentRoot(GdTypes.XOR); }
    "~"            { return dedentRoot(GdTypes.NOT); }
    "_"            { return dedentRoot(GdTypes.UNDER); }
    ".."           { return dedentRoot(GdTypes.DOTDOT); }

    {NODE_PATH_LEX} { return dedentRoot(GdTypes.NODE_PATH_LEX); }
    {STRING}        { return dedentRoot(GdTypes.STRING); }
    {STRING_CHAR}   { return dedentRoot(GdTypes.STRING); }
    {STRING_MULTILINE} { return GdTypes.STRING; }
    {ASSIGN}        { return GdTypes.ASSIGN; }
    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
    {ANNOTATOR}     { return dedentRoot(GdTypes.ANNOTATOR); }
    {IDENTIFIER}    { return dedentRoot(GdTypes.IDENTIFIER); }
    {REAL_NUMBER}   { return dedentRoot(GdTypes.NUMBER); }
    {NUMBER}        { return dedentRoot(GdTypes.NUMBER); }
    {HEX_NUMBER}    { return dedentRoot(GdTypes.NUMBER); }
    {BIN_NUMBER}    { return dedentRoot(GdTypes.NUMBER); }
    {COMMENT}       { return GdTypes.COMMENT; }
    {NEW_LINE}      {
        if (yycolumn == 0) {
            //return dedentRoot(TokenType.WHITE_SPACE);
            return TokenType.WHITE_SPACE;
        }

        return GdTypes.NEW_LINE;
    }
    {INDENT}  {
        if (yycolumn == 0) {
            int spaces = yytext().length();
            if (spaces > indent) {
                indentSizes.push(spaces - indent);
                indent = spaces;

                return GdTypes.INDENT;

                //lastState = yystate();
                //yybegin(CREATE_INDENT);

                //return TokenType.WHITE_SPACE;
            } else if (indent > spaces) {
                dedentSpaces();
                return GdTypes.DEDENT;
            }
        }

        return TokenType.WHITE_SPACE;
    }

    {IGNORE_NEW_LINE} { return TokenType.WHITE_SPACE; }

<<EOF>> {
    if (yycolumn > 0 && !eofFinished) {
        eofFinished = true;
        return GdTypes.NEW_LINE;
    }

    if (indentSizes.empty()) {
        return null;
    }

    indentSizes.pop();
    yypushback(yylength());
    return GdTypes.DEDENT;
}

[^] { return GdTypes.BAD_CHARACTER; }
