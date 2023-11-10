package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdIfStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = IF_ST

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(IF)) return optional

        advance() // if
        var ok = true
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse()

        while (ok && elifSt()) {}
        ok && elseSt()

        return ok
    }

    private fun elifSt(): Boolean {
        if (!nextTokenIs(ELIF)) return false

        val elif = mark()
        var ok = true
        advance() // elif
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse()

        GdRecovery.stmt()
        elif.done(ELIF_ST)

        return true
    }

    private fun elseSt(): Boolean {
        if (!nextTokenIs(ELSE)) return false

        val elseSt = mark()
        var ok = true
        advance() // else
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse()

        GdRecovery.stmt()
        elseSt.done(ELSE_ST)

        return true
    }

}
