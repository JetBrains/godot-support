package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdArrayExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ARR_EX

    companion object {
        lateinit var INSTANCE: GdArrayExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(LSBR, true)
        ok = ok && GdExprParser.INSTANCE.parse(false)
        ok = ok && consumeToken(RSBR, true)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
