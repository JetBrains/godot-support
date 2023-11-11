package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeAnyOfToken

class GdShiftExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = SHIFT_EX

    companion object {
        lateinit var INSTANCE: GdShiftExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeAnyOfToken(true, LBSHIFT, RBSHIFT)
        ok = ok && GdExprParser.INSTANCE.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
