package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.mceIdentifier
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdParamListParser : GdBaseParser {

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (!b.nextTokenIs(VAR, IDENTIFIER)) return optional
        var ok = true
        val paramList = b.mark()

        while (ok && b.nextTokenIs(VAR, IDENTIFIER)) {
            ok = param(b)
            if (!b.consumeToken(COMMA)) break
        }

        paramList.done(PARAM_LIST)

        return true
    }

    private fun param(b: GdPsiBuilder): Boolean {
        val param = b.mark()
        var ok = true

        b.consumeToken(VAR)
        ok = ok && b.mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.parseWithAssignTypedAndExpr(b, true)

        param.done(PARAM)

        return ok
    }
}
