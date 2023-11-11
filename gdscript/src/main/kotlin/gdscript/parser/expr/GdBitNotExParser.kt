package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.BIT_NOT_EX
import gdscript.psi.GdTypes.NOT
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdBitNotExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = BIT_NOT_EX

    companion object {
        lateinit var INSTANCE: GdBitNotExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(NOT, true)
        ok = ok && GdExprParser.INSTANCE.parse(b)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
