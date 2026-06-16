/*
------------------------------------------------------------------------------
  GDSCRIPT INDENTATION & NESTING RULES
  ------------------------------------

  GDScript is indentation-sensitive like Python, but indentation is ignored
  inside (), [], {} -- EXCEPT inside an inline lambda body, where it becomes
  significant again. This lexer reconciles the two cases with a little state.

  State (declared in the %{ ... %} block below):
    ParenTracker parens                  // nesting depth of (), [], {}
    int indent                           // current significant indent, in columns
    Stack<Integer> indentSizes           // one entry per open INDENT: the column
                                         //   delta it added (dedent() pops one)
    ArrayDeque<LambdaFrame> lambdaFrames // one frame per inline lambda; see below

------------------------------------------------------------------------------
  WHEN IS INDENTATION SIGNIFICANT?  (isIgnored())
  -----------------------------------------------
  - At paren depth 0 it is always significant.
  - Inside (), [], {} it is ignored -- so ordinary multi-line arguments are
    free-form -- UNLESS the top lambda frame is active at the current depth,
    which re-enables it for that lambda's body.

------------------------------------------------------------------------------
  NEWLINE / INDENT / DEDENT  (the {NEW_LINE} and {INDENT} rules)
  -------------------------------------------------------------
  When indentation is significant, a line's leading whitespace is compared to
  `indent`:
    - deeper    -> push the delta onto indentSizes, emit INDENT
    - shallower -> pop one level, emit DEDENT (but see lambda close, below)
    - equal     -> emit one NEW_LINE (repeats collapse to WHITE_SPACE)
  When it is ignored, leading whitespace and newlines are insignificant
  WHITE_SPACE.

------------------------------------------------------------------------------
  PARENTHESES & COLON
  -------------------
  '(' '[' '{' -> parens.open();   ')' ']' '}' -> onCloseParen().
  A ':' immediately followed by a newline opens a block suite. Its only special
  lexer job is to ACTIVATE a pending lambda frame (activateLambdaAfterColon);
  an inline ':' (code on the same line) opens no block and activates nothing.

------------------------------------------------------------------------------
  LAMBDAS INSIDE PARENS  (the LambdaFrame mechanism)
  --------------------------------------------------
  A `func` lexed while inside parens (markLambda) pushes a LambdaFrame:
      parenDepth      = paren depth of that `func`
      startIndentSize = -1 while pending; set to indentSizes.size() once the
                        body activates at ':'+newline (the indent level the
                        body starts from)
  While a frame is active at the current depth, isIgnored() is false, so the
  body emits real INDENT/DEDENT/NEW_LINE -- it is a genuine indented suite even
  though it lives inside ().

  A lambda body ends by STRUCTURE, never by `return` (an earlier version closed
  it on `return`, which mis-handled nested if/for/match bodies). Three closes:

    1. Dedent back to the start level -- in the {INDENT} dedent branch, a pop
       that would reach startIndentSize deactivates the frame and consumes the
       whitespace WITHOUT emitting DEDENT: the following ')'/',' bounds the
       argument and the parser's lambda suite absorbs the trailing NEW_LINE.
       Inner-block dedents (still deeper than the start) emit DEDENT normally.

    2. A ',' at the lambda's own depth (drainLambdaFrameAtComma) -- ends the
       argument before the body ever dedented; silently unwind the body's
       indents and pop the frame, no DEDENT emitted.

    3. A closing ')'/']'/'}' at the lambda's depth (drainLambdaFramesAtCurrentDepth)
       -- emit a DEDENT for each still-open body level (pushing the bracket back
       each time) so the bracket lands at the frame's start level, then close.

  onCloseParen() additionally drops any frames belonging to the closed paren.

------------------------------------------------------------------------------
  EXAMPLES
  --------
  (1) closed by its own dedent -- the lower-indented ')' line ends the body;
      no DEDENT token, just WHITE_SPACE before ')':
          var sum = arr.reduce(
              func(acc, n):
                  return acc + n
          )
  (2) closed by an inline ',' -- body never dedents; it ends at the comma and
      the next argument follows:
          arr.reduce(func(acc, n): return acc + n, 0)
  (3) closed by the bracket on the same line -- DEDENT(s) are emitted before
      ')', so ')' sits at the lambda's start level:
          print(func():
              if x:
                  pass)
------------------------------------------------------------------------------
*/

package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTypes;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import gdscript.lexer.ParenTracker;

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
    ParenTracker parens = new ParenTracker();
    int indent = 0;
    Stack<Integer> indentSizes = new Stack<>();
    boolean eofFinished = false;

    boolean newLineProcessed = false;
    boolean gotBackslash = false;

    // One frame per inline lambda (see header). Pushed in `func`-lexed order, popped in `)`-closed order (LIFO).
    static final class LambdaFrame {
        final int parenDepth;
        int startIndentSize = -1;
        LambdaFrame(int parenDepth) { this.parenDepth = parenDepth; }
    }
    java.util.ArrayDeque<LambdaFrame> lambdaFrames = new java.util.ArrayDeque<>();
    Pattern nextNonCommentIndentPattern = Pattern.compile("\\n([ |\\t]*)[^#\\s]");

    public IElementType dedentRoot(IElementType type) {
        newLineProcessed = false;
        if (isIgnored() || yycolumn > 0 || indent <= 0 || indentSizes.empty()) {
            return type;
        }

        dedent();
        yypushback(yylength());

        return GdTypes.DEDENT;
    }

    public boolean dedentSpaces() {
        newLineProcessed = false;
        if (indent <= 0 || indentSizes.empty()) { // For EOF rule
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
        if (parens.isTopLevel()) return false;
        LambdaFrame top = lambdaFrames.peek();
        return top == null
            || top.startIndentSize < 0
            || top.parenDepth != parens.getDepth();
    }

    private void onCloseParen() {
        parens.close();
        int d = parens.getDepth();
        while (!lambdaFrames.isEmpty() && lambdaFrames.peek().parenDepth > d) {
            lambdaFrames.pop();
        }
    }

    private void markLambda() {
        if (parens.isNested()) {
            lambdaFrames.push(new LambdaFrame(parens.getDepth()));
        }
    }

    private boolean nextCharIsNewline() {
        int start = Math.min(zzBuffer.length(), zzCurrentPos + yylength());
        if (start >= zzBuffer.length()) return false;
        char c = zzBuffer.charAt(start);
        if (c == '\r') return true; // handles \r and \r\n
        return c == '\n';
    }

    private void activateLambdaAfterColon() {
        LambdaFrame top = lambdaFrames.peek();
        if (top != null
            && top.startIndentSize < 0
            && top.parenDepth == parens.getDepth()
            && nextCharIsNewline()) {
            top.startIndentSize = indentSizes.size();
        }
    }

    private void drainLambdaFrameAtComma() {
        LambdaFrame top = lambdaFrames.peek();
        if (top != null
            && top.startIndentSize >= 0
            && top.parenDepth == parens.getDepth()
            && parens.getDepth() > 0) {
            while (indentSizes.size() > top.startIndentSize) {
                dedent();
            }
            lambdaFrames.pop();
        }
    }

    /**
     * Close-bracket drain (see header, mechanism 3). Returns true after emitting a DEDENT
     * and pushing the close-bracket back (call again); false once drained, for the caller
     * to emit the bracket itself. Loops over sibling lambdas at the same depth.
     */
    private boolean drainLambdaFramesAtCurrentDepth() {
        while (true) {
            LambdaFrame top = lambdaFrames.peek();
            if (top == null
                || top.startIndentSize < 0
                || top.parenDepth != parens.getDepth()) {
                return false;
            }
            if (indentSizes.size() > top.startIndentSize) {
                dedent();
                yypushback(yylength());
                return true;
            }
            // Fully drained -- pop and check the next frame at this depth.
            lambdaFrames.pop();
        }
    }

    public void resetState() {
        parens = new ParenTracker();
        indent = 0;
        indentSizes.clear();
        eofFinished = false;
        newLineProcessed = false;
        gotBackslash = false;
        lambdaFrames.clear();
    }

    public boolean hasNonDefaultState() {
        return newLineProcessed
            || indent != 0
            || !indentSizes.isEmpty()
            || eofFinished
            || gotBackslash
            || parens.getDepth() != 0
            || !lambdaFrames.isEmpty();
    }

%}

LETTER = [\p{L}_]
DIGIT = [\p{Nd}]
MARK = [\p{M}]

NEW_LINE = [\r\n]
IGNORE_NEW_LINE = \\[\r\n]
INDENT = [ \t]+
EMPTY_INDENT = [ \t]+{NEW_LINE}
IDENTIFIER = {LETTER}({LETTER}|{DIGIT}|{MARK})*
NUMBER = ( [0-9][0-9_]*(\.[0-9_]+)? ) | ( \.[0-9][0-9_]* ) | ( [0-9][0-9_]*\. )
HEX_NUMBER = 0x[0-9_a-fA-F]+
BIN_NUMBER = 0b[01_]+
REAL_NUMBER = {NUMBER}[eE][+-]?[0-9][0-9_]*

COMMENT = "#"[^\r\n]*
INDENTED_COMMENT = {INDENT}"#"
ANNOTATOR = "@"[a-zA-Z_0-9]*
NODE_PATH = "^"\"([^\\\"\r\n ]|\\.)*\"
STRING_NAME = "&"(\"([^\\\"\r\n]|\\.)*\"|'([^\\'\r\n]|\\.)*')
NODE_PATH_LEX = ( ("$"|"%")[\%a-zA-Z0-9_/]+ ) | ( ("$"|"%")\"[\%a-zA-Z0-9:_/\. ]*\" )

ASSIGN = "+=" | "-=" | "*=" | "/=" | "**=" | "%=" | "&=" | "|=" | "^=" | "<<=" | ">>="
TEST_OPERATOR = "<" | ">" | "==" | "!=" | ">=" | "<="
ANY = .+

ONE_NL = \R
WHITE_SPACE = " " | \t | \f | \\ {ONE_NL}

STRING_NL = {ONE_NL}
STRING_ESC = \\ [^] | \\ ({WHITE_SPACE})+ (\n|\r)

SINGLE_QUOTED_CONTENT = {STRING_ESC} | [^'\\\r\n]
SINGLE_QUOTED_LITERAL = [\$\^]?\' {SINGLE_QUOTED_CONTENT}* \'?

TRIPLE_SINGLE_QUOTED_CONTENT = {SINGLE_QUOTED_CONTENT} | {STRING_NL} | \'(\')?[^\'\\$]
TRIPLE_SINGLE_QUOTED_LITERAL = \'\'\' {TRIPLE_SINGLE_QUOTED_CONTENT}* \'\'\'

DOUBLE_QUOTED_CONTENT = {STRING_ESC} | [^\"\n\r]
DOUBLE_QUOTED_LITERAL = [\$\^]?\" {DOUBLE_QUOTED_CONTENT}* \"

TRIPLE_DOUBLE_QUOTED_CONTENT = {DOUBLE_QUOTED_CONTENT} | {STRING_NL} | \"(\")?[^\"\\$]
TRIPLE_DOUBLE_QUOTED_LITERAL = \"\"\" {TRIPLE_DOUBLE_QUOTED_CONTENT}* \"\"\"

RAW_SINGLE_QUOTED_CONTENT = \\[^\r\n] | [^'\\\r\n]
RAW_SINGLE_QUOTED_LITERAL = r \' {RAW_SINGLE_QUOTED_CONTENT}* \'

RAW_DOUBLE_QUOTED_CONTENT = \\[^\r\n] | [^\"\\\r\n]
RAW_DOUBLE_QUOTED_LITERAL = r \" {RAW_DOUBLE_QUOTED_CONTENT}* \"

%state CREATE_INDENT

%%

<CREATE_INDENT> {
    {ANY} {
        yybegin(YYINITIAL);
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
    "vararg"       { return dedentRoot(GdTypes.VARARG); } // this is only for the gd sdk style
    "class"        { return dedentRoot(GdTypes.CLASS); }
    "super"        { return dedentRoot(GdTypes.SUPER); }

    "*"            { return dedentRoot(GdTypes.MUL); }
    "**"           { return dedentRoot(GdTypes.POWER); }
    "/"            { return dedentRoot(GdTypes.DIV); }
    "%"            { return dedentRoot(GdTypes.MOD); }
    "+"            { return dedentRoot(GdTypes.PLUS); }
    "-"            { return dedentRoot(GdTypes.MINUS); }
    "++"           { return dedentRoot(GdTypes.PPLUS); }
    "--"           { return dedentRoot(GdTypes.MMINUS); }
    "..."          { return dedentRoot(GdTypes.DOTDOTDOT); }
    ".."           { return dedentRoot(GdTypes.DOTDOT); }
    "."            { return dedentRoot(GdTypes.DOT); }
    ","            { drainLambdaFrameAtComma(); return dedentRoot(GdTypes.COMMA); }
    ":="           { return dedentRoot(GdTypes.CEQ); }
    ":"            { activateLambdaAfterColon(); return dedentRoot(GdTypes.COLON); }
    ";"            { newLineProcessed = true; return GdTypes.SEMICON; }
    "?"            { return dedentRoot(GdTypes.QUESTION_MARK); }
    "`"            { return dedentRoot(GdTypes.BACKTICK); }
    "$"            { return dedentRoot(GdTypes.DOLLAR); }
    "!"            { return dedentRoot(GdTypes.NEGATE); }
    "not"          { return dedentRoot(GdTypes.NEGATE); }
    "="            { return dedentRoot(GdTypes.EQ); }
    "->"           { return dedentRoot(GdTypes.RET); }
    ">>"           { return dedentRoot(GdTypes.RBSHIFT); }
    "<<"           { return dedentRoot(GdTypes.LBSHIFT); }
    "<<"           { return dedentRoot(GdTypes.LBSHIFT); }
    "("            { parens.open(); return dedentRoot(GdTypes.LRBR); }
    ")"            {
        if (drainLambdaFramesAtCurrentDepth()) return GdTypes.DEDENT;
        onCloseParen();
        newLineProcessed = false;
        return GdTypes.RRBR;
    }
    "["            { parens.open(); return dedentRoot(GdTypes.LSBR); }
    "]"            {
        if (drainLambdaFramesAtCurrentDepth()) return GdTypes.DEDENT;
        onCloseParen();
        newLineProcessed = false;
        return GdTypes.RSBR;
    }
    "{"            { parens.open(); return dedentRoot(GdTypes.LCBR); }
    "}"            {
        if (drainLambdaFramesAtCurrentDepth()) return GdTypes.DEDENT;
              onCloseParen();
              newLineProcessed = false;
              return GdTypes.RCBR;
    }
    "&"            { return dedentRoot(GdTypes.AND); }
    "&&"           { return dedentRoot(GdTypes.ANDAND); }
    "and"          { return dedentRoot(GdTypes.ANDAND); }
    "|"            { return dedentRoot(GdTypes.OR); }
    "||"           { return dedentRoot(GdTypes.OROR); }
    "or"           { return dedentRoot(GdTypes.OROR); }
    "^"            { return dedentRoot(GdTypes.XOR); }
    "~"            { return dedentRoot(GdTypes.NOT); }
    "_"            { return dedentRoot(GdTypes.UNDER); }
    "\\"           { newLineProcessed = true; gotBackslash = true; return GdTypes.BACKSLASH; }

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
    {TRIPLE_SINGLE_QUOTED_LITERAL} { return dedentRoot(GdTypes.STRING); }
    {DOUBLE_QUOTED_LITERAL}        { return dedentRoot(GdTypes.STRING); }
    {TRIPLE_DOUBLE_QUOTED_LITERAL} { return dedentRoot(GdTypes.STRING); }
    {RAW_SINGLE_QUOTED_LITERAL}    { return dedentRoot(GdTypes.STRING); }
    {RAW_DOUBLE_QUOTED_LITERAL}    { return dedentRoot(GdTypes.STRING); }

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
        if (yycolumn == 0 && !gotBackslash) {
            int spaces = yytext().length();
            if (spaces > indent) {
                if (isIgnored()) {
                    return TokenType.WHITE_SPACE;
                }

                indentSizes.push(spaces - indent);
                indent = spaces;

                return GdTypes.INDENT;
            } else if (indent > spaces) {
                if (isIgnored()) {
                    return TokenType.WHITE_SPACE;
                }
                LambdaFrame top = lambdaFrames.peek();
                if (top != null
                    && top.startIndentSize >= 0
                    && top.parenDepth == parens.getDepth()
                    && indentSizes.size() - 1 <= top.startIndentSize) {
                    // Body close (see header, mechanism 1): deactivate + consume as WHITE_SPACE, no DEDENT.
                    // Emitting a DEDENT here would leave a stray token the arg-list parser cannot place.
                    // Inner-block dedents (size still > startIndentSize after the pop) fall through below.
                    dedent();
                    lambdaFrames.pop();
                    return TokenType.WHITE_SPACE;
                }
                dedentSpaces();
                return GdTypes.DEDENT;
            }
        }
        gotBackslash = false;

        return TokenType.WHITE_SPACE;
    }

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
