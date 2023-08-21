package gdscript.parser.roots

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdClassVarParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(VAR)) return optional

        val m = mark()
        advance() // const
        var ok = mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.INSTANCE.parse(true)

        if (ok && nextTokenIs(EQ, CEQ)) {
            ok = ok && mcAnyOf(ASSIGN_TYPED, EQ, CEQ)
            ok = ok && GdExprParser.INSTANCE.parse()
        }

        // TODO set get

        ok && mcEndStmt()

        GdRecovery.topLevel()
        m.done(CLASS_VAR_DECL_TL)

        return true
    }

}
