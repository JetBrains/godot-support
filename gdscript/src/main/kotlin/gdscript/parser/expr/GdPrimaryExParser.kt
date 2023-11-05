package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdPrimaryExParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdPrimaryExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val primary = mark()
        val ok = mcAnyOf(NODE_PATH, NODE_PATH_LEX)
                || arrayDecl()
        // TODO dictDecl
        // TODO (LRBR expr RRBR)

        if (ok) primary.done(PRIMARY_EX)
        else primary.rollbackTo()

        return ok
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
