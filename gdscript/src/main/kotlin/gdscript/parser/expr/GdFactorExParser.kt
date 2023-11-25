package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.mcAnyOfForce

object GdFactorExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = FACTOR_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.mcAnyOfForce(FACTOR_SIGN, MUL, DIV, MOD, POWER)
        ok = ok && GdExprParser.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
