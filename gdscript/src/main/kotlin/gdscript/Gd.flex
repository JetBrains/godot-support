/*
------------------------------------------------------------------------------
  GDSCRIPT INDENTATION & NESTING RULES
  ------------------------------------

  This lexer must handle indentation-sensitive syntax with nested regions
  where indentation is temporarily ignored or reactivated.

  State variables:
    int paren_depth;           // count of (, [, {
    bool indent_active;        // whether indentation affects NEWLINE
    stack<int> indent_stack;   // indentation levels
    stack<bool> react_stack;   // previous indent_active states (for reactivation)

------------------------------------------------------------------------------
  NEWLINE HANDLING
  ----------------
  - On each NEWLINE:
      if (indent_active && paren_depth == 0):
          compare current indentation to top(indent_stack)
          emit INDENT or one/more DEDENT tokens accordingly
      else:
          ignore indentation (leading spaces are insignificant)

------------------------------------------------------------------------------
  PARENTHESIS CONTROL
  -------------------
  - On '(', '[', '{'  → paren_depth++
  - On ')', ']', '}'  → paren_depth--
  - When paren_depth > 0 → indentation normally disabled
  - When paren_depth == 0 → indentation enabled (unless overridden)

------------------------------------------------------------------------------
  COLON BEHAVIOR
  --------------
  After ':' :
    - If next token is NEWLINE:
        // block suite
        enable indentation (indent_active = true)
        on next line, emit INDENT if deeper
    - Else:
        // inline suite
        no INDENT/DEDENT emitted

------------------------------------------------------------------------------
  INDENTATION REACTIVATION INSIDE PARENS
  --------------------------------------
  - If inside parentheses (paren_depth > 0)
    and ':' followed by NEWLINE occurs after a block-forming keyword
    (func, if, elif, else, for, while, match):
        push current indent_active to the react_stack
        set indent_active = true
  - On `return` keyword or dedent back to the outer level:
        restore indent_active from react_stack (usually false)
  - When block is interrupted with closing of the parentheses symbol:
    close block with NEWLINE,
    INDENT, DEDENT before closing parentheses to let it be placed exactly on the level of its start

------------------------------------------------------------------------------
  EXAMPLES
  --------
  func a():
      print(
          func():
              if x:
                  pass)            // Lexer should close the nested blocks with NEWLINE and DEDENTS before the closing bracket
  func a():
      print(
          func():
              if x:
                  pass             // Lexer should close the nested blocks
                  )                // ignore indents
  func a():
      print(
          func():
              if x:
                  pass
      )                            // expected result
------------------------------------------------------------------------------
*/

package gdscript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import gdscript.psi.GdTokenType;
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

    int lambdaReactivateDepth = -1;
    // Tracks pending reactivation at the bracket depth where 'func' was seen
    int lambdaPendingDepth = -1;
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
        // Inside parentheses, indentation is ignored unless reactivated at this depth
        return !(lambdaReactivateDepth == parens.getDepth());
    }

    private void onCloseParen() {
        parens.close();
        if (lambdaPendingDepth > parens.getDepth()) lambdaPendingDepth = -1;
        if (lambdaReactivateDepth > parens.getDepth()) lambdaReactivateDepth = -1;
    }

    private void markLambda() {
        if (parens.isNested()) {
            lambdaPendingDepth = parens.getDepth();
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
        // Reactivate indentation inside parentheses only for block suites,
        // i.e., when ':' is immediately followed by a newline.
        if (lambdaPendingDepth == parens.getDepth() && nextCharIsNewline()) {
            lambdaReactivateDepth = parens.getDepth();
        }
    }

    private void restoreLambdaOnReturn() {
        // When returning from a lambda defined inside parens with reactivated indentation,
        // stop treating NEW_LINE as significant at this bracket depth.
        if (parens.getDepth() > 0 && lambdaReactivateDepth == parens.getDepth()) {
            lambdaReactivateDepth = -1;
            // clear pending marker as well, we won't reactivate again for this lambda
            if (lambdaPendingDepth == parens.getDepth()) lambdaPendingDepth = -1;
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

RAW_SINGLE_QUOTED_CONTENT = [^'\r\n]
RAW_SINGLE_QUOTED_LITERAL = r \' {RAW_SINGLE_QUOTED_CONTENT}* \'?

RAW_DOUBLE_QUOTED_CONTENT = [^\"\r\n]
RAW_DOUBLE_QUOTED_LITERAL = r \" {RAW_DOUBLE_QUOTED_CONTENT}* \"?

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
    "return"       { restoreLambdaOnReturn(); return dedentRoot(GdTypes.RETURN); }
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
    ","            { return dedentRoot(GdTypes.COMMA); }
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
    ")"            { onCloseParen(); return dedentRoot(GdTypes.RRBR); }
    "["            { parens.open(); return dedentRoot(GdTypes.LSBR); }
    "]"            { onCloseParen(); return dedentRoot(GdTypes.RSBR); }
    "{"            { parens.open(); return dedentRoot(GdTypes.LCBR); }
    "}"            { onCloseParen(); return dedentRoot(GdTypes.RCBR); }
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
