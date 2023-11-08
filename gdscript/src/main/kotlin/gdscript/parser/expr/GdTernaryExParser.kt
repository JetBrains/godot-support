package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdTernaryExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = TERNARY_EX

    companion object {
        lateinit var INSTANCE: GdTernaryExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(IF, true)
        ok = ok && GdExprParser.INSTANCE.parse(false)
        ok = ok && consumeToken(ELSE, true)
        ok = ok && GdExprParser.INSTANCE.parse(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
