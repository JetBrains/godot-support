package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.AWAIT_EX

class GdAwaitExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = AWAIT_EX

    companion object {
        lateinit var INSTANCE: GdAwaitExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(AWAIT)) return optional

        val m = mark()
        var ok = true
        ok = ok && consumeToken(AWAIT, true)
        ok = ok && GdExprParser.INSTANCE.parse()

        if (ok) m.drop()
        else m.rollbackTo()

        return ok || optional
    }

}
