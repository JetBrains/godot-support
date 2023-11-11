package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.IN_EX
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdInExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = IN_EX

    companion object {
        lateinit var INSTANCE: GdInExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(IN, true)
        ok = ok && GdExprParser.INSTANCE.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
