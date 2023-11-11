package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.utils.PsiBuilderUtil.mceEndStmt

abstract class GdStmtBaseParser : GdBaseParser() {

    abstract val STMT_TYPE: IElementType
    open val endWithEndStmt: Boolean = false

    fun parseEndStmt(b: PsiBuilder): Boolean {
        return if (endWithEndStmt) b.mceEndStmt()
        else true
    }

}
