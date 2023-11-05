package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdPrimaryExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = PRIMARY_EX

    companion object {
        lateinit var INSTANCE: GdExprBaseParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        return nextTokenIs(NODE_PATH, NODE_PATH_LEX)
                || arrayDecl()
        // TODO dictDecl
        // TODO (LRBR expr RRBR)
    }

    private fun arrayDecl(): Boolean {
        val array = mark()
        var ok = true

        ok = ok && consumeToken(LSBR)
        while (ok && GdExprParser.INSTANCE.parse(true)) {
            if (!nextTokenIs(COMMA)) break
        }
        ok = ok && consumeToken(RSBR)

        if (ok) array.done(ARRAY_DECL)
        else array.rollbackTo()

        return ok
    }

}
