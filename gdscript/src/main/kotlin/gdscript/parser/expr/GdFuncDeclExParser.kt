package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdFuncDeclExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = FUNC_DECL_EX

    companion object {
        lateinit var INSTANCE: GdFuncDeclExParser
    }

    constructor() {
        INSTANCE = this
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(FUNC)) return false

        b.advanceLexer() // func
        val m = b.mark()
        var ok = true
        if (b.nextTokenIs(IDENTIFIER)) b.mceIdentifier(FUNC_DECL_ID_NMI)
        ok = ok && b.consumeToken(LRBR, true)
        ok = ok && GdParamListParser.INSTANCE.parse(b, true)
        ok = ok && b.consumeToken(RRBR, true)
        ok = ok && GdReturnHintParser.INSTANCE.parse(b, true)
        ok = ok && b.consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parseLambda(b, false, true)

        m.drop()

        return true
    }

}
