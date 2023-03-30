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
    int indent = 0;
    Stack<Integer> indentSizes = new Stack<>();
    int yycolumn;
    boolean eofFinished = false; // TODO remove?

    boolean newLineProcessed = false;
    // For signals and such, where Indents/NewLines do not matter
    boolean ignoreIndent = false;
    int ignored = 0;

    public IElementType dedentRoot(IElementType type) {
        newLineProcessed = false;
        lineEnded = false;
        if (ignored > 0 || yycolumn > 0 || indent <= 0 || indentSizes.empty()) {
            return type;
        }

        dedent();
        yypushback(yylength());

        return GdTypes.DEDENT;
    }

    public boolean dedentSpaces() {
        newLineProcessed = false;
        if (ignored > 0 || indent <= 0 || indentSizes.empty()) { // For EOF rule
            return false;
        }

        dedent();

//        if (indent > yylength()) {
            yypushback(yylength());
//        }

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
EMPTY_INDENT = [ \t]+{NEW_LINE}
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NUMBER = [0-9][0-9_]*(\.[0-9_]+)?
HEX_NUMBER = 0x[0-9_a-fA-F]+
BIN_NUMBER = 0b[01_]+
REAL_NUMBER = {NUMBER}e-[0-9]+

STRING = "&"?\"([^\\\"\r\n]|\\.)*\"
STRING_CHAR = \'([^\\\'\r\n]|\\.)*\'
STRING_MULTILINE = \"\"\"([^\\\"]|\\.)*\"\"\"

// TODO remove z obojích flexů
STRING_MARKER = \"\"\"|\"|\'
STRING_MARKER_REV = [^\"\'\n\r]*

COMMENT = "#"[^\r\n]*
INDENTED_COMMENT = {INDENT}"#"
ANNOTATOR = "@"[a-zA-Z_]*
NODE_PATH = "^"\"([^\\\"\r\n]|\\.)*\"
NODE_PATH_LEX = ("$"|"%")[\"\%a-zA-Z0-9_/:\.]*

ASSIGN = "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
//OPERATOR = "+" | "-" | "*" | "/" | "%" | "^" | "&" | "|"
//    | "<<" | ">>" | "!" | "&&" | "||"
ANY = .+

%state CREATE_INDENT

%%

<CREATE_INDENT> {
    {ANY} {
        yybegin(lastState);
        yypushback(yylength());

        return GdTypes.INDENT;
    }
}

    "extends"      { return dedentRoot(GdTypes.EXTENDS); }
    "class_name"   { return dedentRoot(GdTypes.CLASS_NAME); }
    "var"          { return dedentRoot(GdTypes.VAR); }
    "const"        { return dedentRoot(GdTypes.CONST); }
    "get"          { return dedentRoot(GdTypes.GET); }
    "set"          { return dedentRoot(GdTypes.SET); }

    "enum"         { return dedentRoot(GdTypes.ENUM); }
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
    "signal"       { return dedentRoot(GdTypes.SIGNAL); }
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
    "master"       { return dedentRoot(GdTypes.MASTER); }
    "pupper"       { return dedentRoot(GdTypes.PUPPET); }
    "remote"       { return dedentRoot(GdTypes.REMOTE); }
    "remotesync"   { return dedentRoot(GdTypes.REMOTESYNC); }

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
    "("            { /*if (ignoreIndent) { ignored++; }*/ ignored++; return dedentRoot(GdTypes.LRBR); }
    ")"            { /*if (ignoreIndent) { if (--ignored == 0) { ignoreIndent = false; } }*/ ignored--; return GdTypes.RRBR; }
    "["            { ignored++; return dedentRoot(GdTypes.LSBR); }
    "]"            { ignored--; return GdTypes.RSBR; }
      // TODO resetovat ignored, když bude chybět uzavírací závorka? např. nedopsaný call fn
    "{"            { /*if (ignoreIndent) { ignored++; }*/ignored++; return dedentRoot(GdTypes.LCBR); }
    "}"            { /*if (ignoreIndent) { if (--ignored == 0) { ignoreIndent = false; } } */ignored--; return GdTypes.RCBR; }
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

    {NODE_PATH}     { return dedentRoot(GdTypes.NODE_PATH_LIT); }
    {NODE_PATH_LEX} { return dedentRoot(GdTypes.NODE_PATH_LEX); }
    {STRING}        { if (yytext().charAt(0) == '&') { return dedentRoot(GdTypes.STRING_NAME); } return dedentRoot(GdTypes.STRING); }
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
    {COMMENT}       {
          lineEnded = true;
          boolean alreadyDone = newLineProcessed;
          IElementType ret = dedentRoot(GdTypes.COMMENT);
          newLineProcessed = alreadyDone || yycolumn == 0;
          return ret;
      }

    {INDENTED_COMMENT} {
        yypushback(1);
        if (yycolumn == 0) {
            newLineProcessed = true;
        }

        return TokenType.WHITE_SPACE;
    }
    {EMPTY_INDENT} { return TokenType.WHITE_SPACE; }
    {NEW_LINE}     {
        if (yycolumn == 0) {
            return TokenType.WHITE_SPACE;
        } else if (ignored > 0) {
            if (ignored == 0) {
                ignoreIndent = false;
                return GdTypes.NEW_LINE;
            } else {
                return TokenType.WHITE_SPACE;
            }
        }

        if (newLineProcessed) {
            return TokenType.WHITE_SPACE;
        }

        newLineProcessed = true;
        return GdTypes.NEW_LINE;
    }
    {INDENT}  {
        if (yycolumn == 0) {
            int spaces = yytext().length();
            if (spaces > indent) {
                if (ignored > 0) {
                    return TokenType.WHITE_SPACE;
                }

                indentSizes.push(spaces - indent);
                indent = spaces;

                return GdTypes.INDENT;
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

[^] { return TokenType.BAD_CHARACTER; }
