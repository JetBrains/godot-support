package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.AS
import gdscript.psi.GdTypes.CAST_EX

class GdCastExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CAST_EX

    companion object {
        lateinit var INSTANCE: GdCastExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(AS, true)
        ok = ok && GdTypedParser.INSTANCE.typedVal(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
