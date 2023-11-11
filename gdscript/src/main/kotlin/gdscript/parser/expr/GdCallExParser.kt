package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdCallExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CALL_EX

    companion object {
        lateinit var INSTANCE: GdCallExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(LRBR, true)
        ok = ok && GdArgListParser.INSTANCE.parse(b, true)
        ok = ok && b.consumeToken(RRBR, true)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
