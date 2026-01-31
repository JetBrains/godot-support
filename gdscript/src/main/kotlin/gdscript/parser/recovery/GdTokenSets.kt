package gdscript.parser.recovery

import gdscript.psi.GdTypes.ANNOTATOR
import gdscript.psi.GdTypes.ASSET
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.BREAK
import gdscript.psi.GdTypes.BREAKPOINT
import gdscript.psi.GdTypes.CLASS
import gdscript.psi.GdTypes.CLASS_NAME
import gdscript.psi.GdTypes.COMMA
import gdscript.psi.GdTypes.CONST
import gdscript.psi.GdTypes.CONTINUE
import gdscript.psi.GdTypes.DEDENT
import gdscript.psi.GdTypes.ELIF
import gdscript.psi.GdTypes.ELSE
import gdscript.psi.GdTypes.ENUM
import gdscript.psi.GdTypes.EXTENDS
import gdscript.psi.GdTypes.FALSE
import gdscript.psi.GdTypes.FOR
import gdscript.psi.GdTypes.FUNC
import gdscript.psi.GdTypes.GET
import gdscript.psi.GdTypes.IDENTIFIER
import gdscript.psi.GdTypes.IF
import gdscript.psi.GdTypes.INDENT
import gdscript.psi.GdTypes.INF
import gdscript.psi.GdTypes.LCBR
import gdscript.psi.GdTypes.LRBR
import gdscript.psi.GdTypes.LSBR
import gdscript.psi.GdTypes.MASTER
import gdscript.psi.GdTypes.MASTERSYNC
import gdscript.psi.GdTypes.MATCH
import gdscript.psi.GdTypes.NAN
import gdscript.psi.GdTypes.NEGATE
import gdscript.psi.GdTypes.NEW_LINE
import gdscript.psi.GdTypes.NODE_PATH_LEX
import gdscript.psi.GdTypes.NODE_PATH_LIT
import gdscript.psi.GdTypes.NULL
import gdscript.psi.GdTypes.NUMBER
import gdscript.psi.GdTypes.PASS
import gdscript.psi.GdTypes.PUPPET
import gdscript.psi.GdTypes.PUPPETSYNC
import gdscript.psi.GdTypes.RCBR
import gdscript.psi.GdTypes.REMOTE
import gdscript.psi.GdTypes.REMOTESYNC
import gdscript.psi.GdTypes.RETURN
import gdscript.psi.GdTypes.RRBR
import gdscript.psi.GdTypes.RSBR
import gdscript.psi.GdTypes.SELF
import gdscript.psi.GdTypes.SEMICON
import gdscript.psi.GdTypes.SET
import gdscript.psi.GdTypes.SIGNAL
import gdscript.psi.GdTypes.STATIC
import gdscript.psi.GdTypes.STRING
import gdscript.psi.GdTypes.STRING_NAME
import gdscript.psi.GdTypes.SUPER
import gdscript.psi.GdTypes.TRUE
import gdscript.psi.GdTypes.UNDER
import gdscript.psi.GdTypes.VAR
import gdscript.psi.GdTypes.VARARG
import gdscript.psi.GdTypes.WHILE

val END_STMT_SET = arrayOf(SEMICON, NEW_LINE)
val ARG_END = arrayOf(RRBR)
//val ARG_END = arrayOf(RRBR, *END_STMT_SET)

val LITERAL = arrayOf(TRUE, FALSE, STRING_NAME, NODE_PATH_LIT, STRING, NUMBER, NULL, NAN, INF, SELF, SUPER,
    IDENTIFIER, GET, SET, MATCH, SIGNAL, FUNC, CLASS_NAME, PASS, CLASS)
val PRIMARY = arrayOf(NODE_PATH_LEX, LSBR, LCBR, LRBR)

val TOP_LEVEL = arrayOf(FUNC, CONST, SIGNAL, VAR, ENUM, ANNOTATOR, IDENTIFIER, DEDENT, INDENT,
    REMOTE, REMOTESYNC, MASTER, PUPPET, STATIC, VARARG, CLASS_NAME, CLASS_NAME,
    MASTERSYNC, PUPPETSYNC, RRBR, RCBR, RSBR, EXTENDS, CLASS, PASS, STRING)

val STMT_NO_LINE = arrayOf(SET, GET, IF, PASS, CONTINUE, BREAK, BREAKPOINT, WHILE, FOR,
    MATCH, RETURN, AWAIT, ASSET, INDENT, DEDENT, NEGATE, ELIF,
    ELSE, UNDER, IDENTIFIER, *TOP_LEVEL, *LITERAL, *PRIMARY, COMMA)

val STMT = arrayOf(*STMT_NO_LINE, NEW_LINE, SEMICON)

val SET_GET = arrayOf(SET, GET, COMMA, *TOP_LEVEL, NEW_LINE)
