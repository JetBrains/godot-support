package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdVarStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = VAR_DECL_ST
    override val endWithEndStmt: Boolean = true
    override val pinnable: Boolean = true

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(VAR)) return false

        b.advanceLexer() // var
        var ok = true
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)

        return true
    }

}
