package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.NEGATE
import gdscript.psi.GdTypes.NEGATE_EX
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdNegateExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = NEGATE_EX

    companion object {
        lateinit var INSTANCE: GdNegateExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(NEGATE, true)
        ok = ok && GdExprParser.INSTANCE.parse(b)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
