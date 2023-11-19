package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.mcAnyOfForce

object GdComparisonExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = COMPARISON_EX

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.mcAnyOfForce(OPERATOR, TEST_OPERATOR)
        ok = ok && GdExprParser.parse(b)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
