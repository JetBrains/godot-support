package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdShiftExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = SHIFT_EX

    companion object {
        lateinit var INSTANCE: GdShiftExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeAnyOfToken(true, LBSHIFT, RBSHIFT)
        ok = ok && GdExprParser.INSTANCE.parse(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
