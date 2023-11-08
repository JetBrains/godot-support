package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdSignExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = SIGN_EX

    companion object {
        lateinit var INSTANCE: GdSignExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val m = mark()
        var ok = true
        ok = ok && consumeAnyOfToken(true, PLUS, MINUS)
        ok = ok && GdExprParser.INSTANCE.parse()

        if (ok) m.drop()
        else m.rollbackTo()

        return ok
    }

}
