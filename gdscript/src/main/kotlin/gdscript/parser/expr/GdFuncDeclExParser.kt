package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*

class GdFuncDeclExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = FUNC_DECL_EX

    companion object {
        lateinit var INSTANCE: GdFuncDeclExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(FUNC)) return false

        advance() // func
        val m = mark()
        var ok = true
        if (nextTokenIs(IDENTIFIER)) mceIdentifier(FUNC_DECL_ID_NMI)
        ok = ok && consumeToken(LRBR, true)
        ok = ok && GdParamListParser.INSTANCE.parse(true)
        ok = ok && consumeToken(RRBR, true)
        ok = ok && GdReturnHintParser.INSTANCE.parse(true)
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parseLambda(false, true)

        m.drop()

        return true
    }

}
