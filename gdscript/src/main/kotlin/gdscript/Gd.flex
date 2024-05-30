package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTokenType;
import gdscript.psi.GdTypes;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    boolean eofFinished = false;

    boolean newLineProcessed = false;
    // For signals and such, where Indents/NewLines do not matter
    boolean ignoreIndent = false;
    int ignored = 0;
    Stack<Integer> ignoreLambda = new Stack<>();
    Pattern nextNonCommentIndentPattern = Pattern.compile("\\n([ |\\t]*)[^#\\s]");

    public IElementType dedentRoot(IElementType type) {
        newLineProcessed = false;
        lineEnded = false;
        if (isIgnored() || yycolumn > 0 || indent <= 0 || indentSizes.empty()) {
            return type;
        }

        dedent();
        yypushback(yylength());

        return GdTypes.DEDENT;
    }

    public boolean dedentSpaces() {
        newLineProcessed = false;
        if (isIgnored() || indent <= 0 || indentSizes.empty()) { // For EOF rule
            return false;
        }

        dedent();
        yypushback(yylength());

        return true;
    }

    private int nextNonCommentIndent() {
        Matcher matcher = nextNonCommentIndentPattern.matcher(zzBuffer.subSequence(Math.min(zzBuffer.length(), zzCurrentPos+yylength()), zzBuffer.length()));
        if (matcher.find()) {
            return matcher.group(1).length();
        }

        return -1;
    }

    private IElementType dedentComment(IElementType type) {
        int nextIndent = nextNonCommentIndent();
        if (nextIndent < 0 || isIgnored() || indent <= 0 || indentSizes.empty() || indent <= nextIndent || !newLineProcessed) {
            return type;
        }

        dedent();
        yypushback(yylength());

        return GdTypes.DEDENT;
    }

    private void dedent() {
        indent = Math.max(0, indent - indentSizes.pop());
    }

    private boolean isIgnored() {
        if (ignored <= 0) return false;

        if (ignoreLambda.isEmpty()) return true;

        return ignoreLambda.peek() != ignored;
    }

    private void ignoredMinus() {
        if (!ignoreLambda.isEmpty() && ignoreLambda.peek() == ignored) {
            ignoreLambda.pop();
        }
        ignored--;
    }

    private void markLambda() {
        if (ignored > 0) {
            ignoreLambda.push(ignored);
        }
    }
%}

LETTER = [a-z|A-Z|_]
DIGIT = [0-9]

NEW_LINE = [\r\n]
IGNORE_NEW_LINE = \\[\r\n]
INDENT = [ \t]+
EMPTY_INDENT = [ \t]+{NEW_LINE}
IDENTIFIER = {LETTER}({LETTER}|{DIGIT})*
NUMBER = ( [0-9][0-9_]*(\.[0-9_]+)? ) | ( \.[0-9][0-9_]* ) | ( [0-9][0-9_]*\. )
HEX_NUMBER = 0x[0-9_a-fA-F]+
BIN_NUMBER = 0b[01_]+
REAL_NUMBER = {NUMBER}e-[0-9]+

COMMENT = "#"[^\r\n]*
INDENTED_COMMENT = {INDENT}"#"
ANNOTATOR = "@"[a-zA-Z_]*
NODE_PATH = "^"\"([^\\\"\r\n ]|\\.)*\"
STRING_NAME = "&"\"([^\\\"\r\n]|\\.)*\"
NODE_PATH_LEX = ( ("$"|"%")[\%a-zA-Z0-9_/]* ) | ( ("$"|"%")\"[\%a-zA-Z0-9:_/\. ]*\" )

ASSIGN = "+=" | "-=" | "*=" | "/=" | "**=" | "%=" | "&=" | "|=" | "^=" | "<<=" | ">>="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
ANY = .+

ONE_NL = \R
WHITE_SPACE = " " | \t | \f | \\ {ONE_NL}

STRING_NL = {ONE_NL}
STRING_ESC = \\ [^] | \\ ({WHITE_SPACE})+ (\n|\r)

SINGLE_QUOTED_CONTENT = {STRING_ESC} | [^'\\\r\n]
SINGLE_QUOTED_LITERAL = \' {SINGLE_QUOTED_CONTENT}* \'?

DOUBLE_QUOTED_CONTENT = {STRING_ESC} | [^\"\n\r]
DOUBLE_QUOTED_LITERAL = [\$\^]?\" {DOUBLE_QUOTED_CONTENT}* \"

TRIPLE_DOUBLE_QUOTED_CONTENT = {DOUBLE_QUOTED_CONTENT} | {STRING_NL} | \"(\")?[^\"\\$]
TRIPLE_DOUBLE_QUOTED_LITERAL = \"\"\" {TRIPLE_DOUBLE_QUOTED_CONTENT}* \"\"\"

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
    "func"         { markLambda(); return dedentRoot(GdTypes.FUNC); }
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
    "mastersync"   { return dedentRoot(GdTypes.MASTERSYNC); }
    "puppetsync"   { return dedentRoot(GdTypes.PUPPETSYNC); }

    "*"            { return dedentRoot(GdTypes.MUL); }
    "**"           { return dedentRoot(GdTypes.POWER); }
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
    ";"            { newLineProcessed = true; return GdTypes.SEMICON; }
    "!"            { return dedentRoot(GdTypes.NEGATE); }
    "not"          { return dedentRoot(GdTypes.NEGATE); }
    "="            { return dedentRoot(GdTypes.EQ); }
    "->"           { return dedentRoot(GdTypes.RET); }
    ">>"           { return dedentRoot(GdTypes.RBSHIFT); }
    "<<"           { return dedentRoot(GdTypes.LBSHIFT); }
    "<<"           { return dedentRoot(GdTypes.LBSHIFT); }
    "("            { ignored++; return dedentRoot(GdTypes.LRBR); }
    ")"            { ignoredMinus(); return dedentRoot(GdTypes.RRBR); }
    "["            { ignored++; return dedentRoot(GdTypes.LSBR); }
    "]"            { ignoredMinus(); return dedentRoot(GdTypes.RSBR); }
    "{"            { ignored++; return dedentRoot(GdTypes.LCBR); }
    "}"            { ignoredMinus(); return dedentRoot(GdTypes.RCBR); }
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
    "\\"           { newLineProcessed = true; ignoreIndent = true; return GdTypes.BACKSLASH; }

    {NODE_PATH}     { return dedentRoot(GdTypes.NODE_PATH_LIT); }
    {STRING_NAME}   { return dedentRoot(GdTypes.STRING_NAME); }
    {NODE_PATH_LEX} {
          if (yytext().toString().startsWith("%\"")) {
              String preceeding = zzBufferL.toString().substring(Math.max(0, zzCurrentPos - 100), zzCurrentPos).trim();
              if (preceeding.length() > 1 && preceeding.charAt(preceeding.length() - 1) == '"') {
                  yypushback(yylength() - 1);
                  return dedentRoot(GdTypes.MOD);
              }
          }

          return dedentRoot(GdTypes.NODE_PATH_LEX);
    }

    {SINGLE_QUOTED_LITERAL}        { return dedentRoot(GdTypes.STRING); }
    {DOUBLE_QUOTED_LITERAL}        { return dedentRoot(GdTypes.STRING); }
    {TRIPLE_DOUBLE_QUOTED_LITERAL} { return dedentRoot(GdTypes.STRING); }

    {ASSIGN}        { return GdTypes.ASSIGN; }
    {TEST_OPERATOR} { return GdTypes.TEST_OPERATOR; }
    {ANNOTATOR}     { return dedentRoot(GdTypes.ANNOTATOR); }
    {IDENTIFIER}    { return dedentRoot(GdTypes.IDENTIFIER); }
    {REAL_NUMBER}   { return dedentRoot(GdTypes.NUMBER); }
    {NUMBER}        { return dedentRoot(GdTypes.NUMBER); }
    {HEX_NUMBER}    { return dedentRoot(GdTypes.NUMBER); }
    {BIN_NUMBER}    { return dedentRoot(GdTypes.NUMBER); }
    {COMMENT}       { return dedentComment(GdTypes.COMMENT); }

    {INDENTED_COMMENT} {
        yypushback(1);
        if (yycolumn == 0) {
            newLineProcessed = true;
        }

        return TokenType.WHITE_SPACE;
    }
    {EMPTY_INDENT} { if (yycolumn > 0) { yypushback(1); } return TokenType.WHITE_SPACE; }
    {NEW_LINE}     {
        if (yycolumn == 0) {
            return TokenType.WHITE_SPACE;
        } else if (isIgnored()) {
            return TokenType.WHITE_SPACE;
        }

        if (newLineProcessed) {
            return TokenType.WHITE_SPACE;
        }

        newLineProcessed = true;
        return GdTypes.NEW_LINE;
    }
    {INDENT}  {
        if (yycolumn == 0 && !ignoreIndent) {
            int spaces = yytext().length();
            if (spaces > indent) {
                if (isIgnored()) {
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
        ignoreIndent = false;

        return TokenType.WHITE_SPACE;
    }

<<EOF>> {
    if (yycolumn > 0 && !eofFinished && !lineEnded) {
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
