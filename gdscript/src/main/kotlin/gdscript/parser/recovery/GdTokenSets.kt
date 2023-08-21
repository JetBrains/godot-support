package gdscript.parser.recovery

import gdscript.psi.GdTypes.*

val END_STMT_SET = arrayOf(SEMICON, NEW_LINE)
val ARG_END = arrayOf(RRBR, *END_STMT_SET)
