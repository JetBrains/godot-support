package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdConstStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = CONST_DECL_ST
    override val endWithEndStmt: Boolean = true

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(CONST)) return optional

        b.advanceLexer() // const
        var ok = true
        ok = ok && b.mceIdentifier(VAR_NMI)

        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)

        return ok
    }

}
