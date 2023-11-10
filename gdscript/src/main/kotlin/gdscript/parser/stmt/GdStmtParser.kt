package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdStmtParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdStmtParser
    }

    val parsers = mutableListOf<GdStmtBaseParser>()
    var moved = false
    var lambda = false

    constructor(builder: PsiBuilder) : super(builder) {
        parsers.add(GdAssignStmtParser(builder))
        parsers.add(GdVarStmtParser(builder))
        parsers.add(GdConstStmtParser(builder))
        parsers.add(GdIfStmtParser(builder))
        parsers.add(GdWhileStmtParser(builder))
        parsers.add(GdForStmtParser(builder))
        parsers.add(GdMatchStmtParser(builder))
        parsers.add(GdFlowStmtParser(builder))
        parsers.add(GdAwaitStmtParser(builder))
        parsers.add(GdExStmtParser(builder))
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        return parseLambda(optional, false)
    }

    fun parseLambda(optional: Boolean, asLambda: Boolean): Boolean {
        val stmtOrSuite = mark()
        var ok = suite(false, asLambda) || stmt(optional, asLambda)

        stmtOrSuite.done(STMT_OR_SUITE)

        return true
    }

    private fun suite(optional: Boolean, asLambda: Boolean): Boolean {
        if (!nextTokenIs(NEW_LINE)) return optional
        var ok = true
        val suite = mark()
        advance() // NEW_LINE
        ok = ok && consumeToken(INDENT)

        ok = ok && stmt(false, asLambda)
        moved = true
        while (ok && moved) {
            ok = ok && stmt(true, asLambda)
        }
        if (asLambda) {
            builder.remapCurrentToken(DEDENT)
        }
        ok = ok && consumeToken(DEDENT, true)
        if (asLambda) {
            builder.remapCurrentToken(NEW_LINE)
        }

        if (ok) {
            GdRecovery.stmt()
            suite.done(SUITE)
        } else {
            suite.rollbackTo()
        }

        return ok || optional
    }

    private fun stmt(optional: Boolean, asLambda: Boolean): Boolean {
        moved = false

        if (
            parsers.any {
                val stmt = mark()
                var ok = it.parse()
                ok = ok &&
                    if (asLambda) {
                        nextTokenIs(SEMICON, NEW_LINE, RRBR)
                    } else {
                        it.parseEndStmt()
                    }

                if (ok) {
                    GdRecovery.stmt()
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
            markError("Statement expected")
        }

        return optional
    }

}
