package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdTernaryExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = TERNARY_EX

    companion object {
        lateinit var INSTANCE: GdTernaryExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(IF, true)
        ok = ok && GdExprParser.INSTANCE.parse(b, false)
        ok = ok && b.consumeToken(ELSE, true)
        ok = ok && GdExprParser.INSTANCE.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
