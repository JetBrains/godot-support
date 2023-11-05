package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdVarStmtParser : GdBaseParser {

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(VAR)) return false

        val stmt = mark()
        advance() // var
        var ok = true
        ok = ok && mceIdentifier(VAR_NMI)
        ok = ok && GdTypedParser.INSTANCE.parseWithAssignTypedAndExpr(true)
        ok = ok && mceEndStmt()

        GdRecovery.stmt()
        stmt.done(VAR_DECL_ST)

        return ok
    }

}
