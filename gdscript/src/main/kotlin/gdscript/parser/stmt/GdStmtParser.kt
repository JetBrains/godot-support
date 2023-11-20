package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.consumeToken
import gdscript.utils.PsiBuilderUtil.markError
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdStmtParser : GdBaseParser {

    val parsers = mutableListOf<GdStmtBaseParser>()
    var moved = false

    init {
        parsers.add(GdAssignStmtParser)
        parsers.add(GdVarStmtParser)
        parsers.add(GdConstStmtParser)
        parsers.add(GdIfStmtParser)
        parsers.add(GdWhileStmtParser)
        parsers.add(GdForStmtParser)
        parsers.add(GdMatchStmtParser)
        parsers.add(GdFlowStmtParser)
        parsers.add(GdExStmtParser)
    }

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        return parseLambda(b, optional, false)
    }

    fun parseLambda(b: PsiBuilder, optional: Boolean, asLambda: Boolean): Boolean {
        val stmtOrSuite = b.mark()
        var ok = suite(b, false, asLambda) || stmt(b, optional, asLambda)

        stmtOrSuite.done(STMT_OR_SUITE)

        return true
    }

    private fun suite(b: PsiBuilder, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.nextTokenIs(NEW_LINE)) return optional
        var ok = true
        val suite = b.mark()
        b.advanceLexer() // NEW_LINE
        ok = ok && b.consumeToken(INDENT)

        ok = ok && stmt(b, false, asLambda)
        moved = true
        while (ok && moved) {
            ok = ok && stmt(b, true, asLambda)
        }
        if (asLambda && b.nextTokenIs(NEW_LINE)) {
            b.remapCurrentToken(DEDENT)
        }
        ok = ok && b.consumeToken(DEDENT, true)
        if (asLambda && b.nextTokenIs(DEDENT)) {
            b.remapCurrentToken(NEW_LINE)
        }

        if (ok) {
            GdRecovery.stmt(b)
            suite.done(SUITE)
        } else {
            suite.rollbackTo()
        }

        return ok || optional
    }

    private fun stmt(b: PsiBuilder, optional: Boolean, asLambda: Boolean): Boolean {
        moved = false

        if (
            parsers.any {
                val stmt = b.mark()
                var ok = it.parse(b)
                if (asLambda) {
                    ok = ok && b.nextTokenIs(SEMICON, NEW_LINE, RRBR, DEDENT)
                } else {
                    ok = ok && it.parseEndStmt(b)
                }

                if (ok) {
                    GdRecovery.stmt(b)
                    stmt.done(it.STMT_TYPE)
                } else {
                    stmt.rollbackTo()
                }

                ok
            }
        ) {
            moved = true

            return true
        }

        if (!optional) {
            b.markError("Statement expected")
        }

        return optional
    }

}
