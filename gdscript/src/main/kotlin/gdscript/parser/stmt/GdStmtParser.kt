package gdscript.parser.stmt

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

object GdStmtParser : GdBaseParser {

    val parsers = mutableListOf<GdStmtBaseParser>()
    var moved = false

    init {
        parsers.add(GdAssignStmtParser)
//        parsers.add(GdVarStmtParser)
//        parsers.add(GdConstStmtParser)
//        parsers.add(GdIfStmtParser)
//        parsers.add(GdWhileStmtParser)
//        parsers.add(GdForStmtParser)
//        parsers.add(GdMatchStmtParser)
        parsers.add(GdFlowStmtParser)
        parsers.add(GdAnnotationStmtParser)
        parsers.add(GdExStmtParser)
    }

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        return parseLambda(b, optional, false)
    }

    fun parseLambda(b: GdPsiBuilder, optional: Boolean, asLambda: Boolean): Boolean {
        b.enterSection(STMT_OR_SUITE)
        b.pin()
        var ok = suite(b, false, asLambda) || stmt(b, optional, asLambda)

        return b.exitSection(ok)
    }

    private fun suite(b: GdPsiBuilder, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.nextTokenIs(NEW_LINE)) return optional
        var ok = true
        var pin = false
        val suite = b.mark()
        b.advance() // NEW_LINE
        ok = ok && b.consumeToken(INDENT)
        pin = ok

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

        if (pin) {
            GdRecovery.stmt(b)
            suite.done(SUITE)
        } else {
            suite.rollbackTo()
        }

        return pin || optional
    }

    private fun stmt(b: GdPsiBuilder, optional: Boolean, asLambda: Boolean): Boolean {
        moved = false

        if (
            parsers.any {
                val stmt = b.mark()
                var ok = it.parse(b)
                if (asLambda) {
                    ok = ok && (b.nextTokenIs(SEMICON, NEW_LINE, RRBR, DEDENT) || it.pinnable)
                } else {
                    ok = ok && (it.parseEndStmt(b) || it.pinnable)
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
            b.error("Statement expected")
        }

        return optional
    }

}
