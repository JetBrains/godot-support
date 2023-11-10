package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser

abstract class GdStmtBaseParser : GdBaseParser {

    abstract val STMT_TYPE: IElementType
    open val endWithEndStmt: Boolean = false

    constructor(builder: PsiBuilder) : super(builder)

    fun parseEndStmt(): Boolean {
        return if (endWithEndStmt) mceEndStmt()
        else true
    }

}
