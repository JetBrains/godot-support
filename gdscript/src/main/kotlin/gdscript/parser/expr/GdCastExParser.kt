package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.AS
import gdscript.psi.GdTypes.CAST_EX
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdCastExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CAST_EX

    companion object {
        lateinit var INSTANCE: GdCastExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(AS, true)
        ok = ok && GdTypedParser.INSTANCE.typedVal(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
