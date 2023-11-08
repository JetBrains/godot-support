package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.ATTRIBUTE_EX
import gdscript.psi.GdTypes.DOT

class GdAttributeExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ATTRIBUTE_EX

    companion object {
        lateinit var INSTANCE: GdAttributeExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(DOT, true)
        ok = ok && GdExprParser.INSTANCE.parse(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
