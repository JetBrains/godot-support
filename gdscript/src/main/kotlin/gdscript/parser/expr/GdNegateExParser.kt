package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.NEGATE
import gdscript.psi.GdTypes.NEGATE_EX

class GdNegateExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = NEGATE_EX

    companion object {
        lateinit var INSTANCE: GdNegateExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(NEGATE, true)
        ok = ok && GdExprParser.INSTANCE.parse()

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
