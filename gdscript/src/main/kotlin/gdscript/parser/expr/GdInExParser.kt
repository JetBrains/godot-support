package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.IN_EX

class GdInExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = IN_EX

    companion object {
        lateinit var INSTANCE: GdInExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeToken(IN, true)
        ok = ok && GdExprParser.INSTANCE.parse(false)

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
