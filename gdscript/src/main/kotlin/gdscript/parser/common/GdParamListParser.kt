package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdParamListParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdParamListParser
    }

    constructor(builder: PsiBuilder) : super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(VAR, IDENTIFIER)) return optional
        var ok = true
        val paramList = mark()

        while (ok && nextTokenIs(VAR, IDENTIFIER)) {
            ok = param()
            if (!consumeToken(COMMA)) break
        }

        paramList.done(PARAM_LIST)

        return true
    }

    private fun param(): Boolean {
        val param = mark()
        var ok = true

        consumeToken(VAR)
        ok = ok && mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.INSTANCE.parseWithAssignTypedAndExpr(true)

        param.done(PARAM)

        return ok
    }
}
