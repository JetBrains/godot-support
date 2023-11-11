package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.ATTRIBUTE_EX
import gdscript.psi.GdTypes.DOT
import gdscript.utils.PsiBuilderUtil.consumeToken

class GdAttributeExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ATTRIBUTE_EX

    companion object {
        lateinit var INSTANCE: GdAttributeExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        val m = b.mark()
        var ok = true
        ok = ok && b.consumeToken(DOT, true)
        ok = ok && GdExprParser.INSTANCE.parse(b, false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
