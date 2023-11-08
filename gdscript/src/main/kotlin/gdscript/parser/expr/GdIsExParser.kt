package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.IS
import gdscript.psi.GdTypes.IS_EX

class GdIsExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = IS_EX

    companion object {
        lateinit var INSTANCE: GdIsExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(IS, true)
        ok = ok && GdTypedParser.INSTANCE.typedVal(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
