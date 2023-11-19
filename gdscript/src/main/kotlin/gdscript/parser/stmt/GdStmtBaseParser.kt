package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.utils.PsiBuilderUtil.mceEndStmt

interface GdStmtBaseParser : GdBaseParser {

    val STMT_TYPE: IElementType
    val endWithEndStmt: Boolean

    fun parseEndStmt(b: PsiBuilder): Boolean {
        return if (endWithEndStmt) b.mceEndStmt()
        else true
    }

}
