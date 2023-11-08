package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdFactorExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = FACTOR_EX

    companion object {
        lateinit var INSTANCE: GdFactorExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && mcAnyOfForce(FACTOR_SIGN, MUL, DIV, MOD, POWER)
        ok = ok && GdExprParser.INSTANCE.parse(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
