package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.BIT_NOT_EX
import gdscript.psi.GdTypes.NOT

class GdBitNotExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = BIT_NOT_EX

    companion object {
        lateinit var INSTANCE: GdBitNotExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(NOT, true)
        ok = ok && GdExprParser.INSTANCE.parse()

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
