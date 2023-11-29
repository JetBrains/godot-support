package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

interface GdStmtBaseParser : GdBaseParser {

    val STMT_TYPE: IElementType
    val endWithEndStmt: Boolean
    val pinnable: Boolean

    fun parseEndStmt(b: GdPsiBuilder): Boolean {
        return if (endWithEndStmt) b.mceEndStmt()
        else true
    }

}
