package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.*

class GdCallExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CALL_EX

    companion object {
        lateinit var INSTANCE: GdCallExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(LRBR, true)
        ok = ok && GdArgListParser.INSTANCE.parse(true)
        ok = ok && consumeToken(RRBR, true)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
