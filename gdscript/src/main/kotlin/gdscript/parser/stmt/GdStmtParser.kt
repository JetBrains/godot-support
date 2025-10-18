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
        parsers.add(GdVarStmtParser)
        parsers.add(GdConstStmtParser)
        parsers.add(GdIfStmtParser)
        parsers.add(GdWhileStmtParser)
        parsers.add(GdForStmtParser)
        parsers.add(GdMatchStmtParser)
        parsers.add(GdFlowStmtParser)
        parsers.add(GdAnnotationStmtParser)
        parsers.add(GdExStmtParser)
        parsers.add(GdEmptyStmtParser)
    }

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Stmt")) return false
        // When parsing inside an argument list (e.g., a lambda passed as an argument),
        // continue treating nested statements as lambda-context to allow multiline suites.
        return parseLambda(b, l + 1, optional, b.isArgs)
    }

    fun parseLambda(b: GdPsiBuilder, l: Int, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.recursionGuard(l, "Lambda")) return false
        b.enterSection(STMT_OR_SUITE)
        var ok = suite(b, l + 1, false, asLambda)

        if (!ok)
            ok = stmt(b, l + 1, optional, asLambda)

        return b.exitSection(ok)
    }

    private fun suite(b: GdPsiBuilder, l: Int, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.recursionGuard(l, "Suite")) return false
        if (!b.nextTokenIs(NEW_LINE)) return optional
        var ok = true
        val suite = b.mark()
        b.advance() // NEW_LINE
        ok = ok && b.consumeToken(INDENT)
        b.pin(ok)

        ok = ok && stmt(b, l + 1, false, asLambda)
        moved = true
        while (ok && moved) {
            ok = ok && stmt(b, l + 1, true, asLambda)
        }

        ok && asLambda && b.passToken(NEW_LINE)
        if (asLambda) {
            if (b.nextTokenIs(DEDENT)) {
                if (!b.followingTokensAre(DEDENT, NEW_LINE) && !b.followingTokensAre(DEDENT, END_STMT)) {
                    b.remapCurrentToken(NEW_LINE)
                } else {
                    b.consumeToken(DEDENT)
                }
            }
        } else {
            ok = ok && b.consumeToken(DEDENT)
        }

        if (ok || b.pinned()) {
            GdRecovery.stmt(b)
            suite.done(SUITE)
        } else {
            suite.rollbackTo()
        }

        return ok || b.pinned() || optional
    }

    private fun stmt(b: GdPsiBuilder, l: Int, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.recursionGuard(l, "InnerStmt")) return false

        // multiline lambda
        if (optional && asLambda && b.nextTokenIs(NEW_LINE)){
            b.passToken(NEW_LINE)
            return true
        }

        moved = false

        if (
            parsers.any {
                if (asLambda && it is GdEmptyStmtParser) return@any false
                b.enterSection(it.STMT_TYPE)
                var ok = it.parse(b, l + 1)
                ok = ok || b.pinned()

                if (asLambda) {
                    ok = ok && b.nextTokenIs(SEMICON, RRBR, COMMA)
                    if (ok && b.isArgs) {
                        b.passToken(SEMICON) || b.nextTokenIs(COMMA)
                    }
                } else {
                    ok = ok && it.parseEndStmt(b)
                }

                ok = ok || b.pinned()
                if (ok && !asLambda) {
                    if (it.endWithEndStmt) GdRecovery.stmt(b)
                    else GdRecovery.stmtNoLine(b)
                }
                b.exitSection(ok, true)

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
