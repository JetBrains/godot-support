package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.*

class GdStmtParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdStmtParser
    }

    val parsers = mutableListOf<GdBaseParser>()
    var moved = false

    constructor(builder: PsiBuilder): super(builder) {
        parsers.add(GdAssignStmtParser(builder))

        parsers.add(GdFlowStmtParser(builder))
        parsers.add(GdVarStmtParser(builder))
        parsers.add(GdConstStmtParser(builder))
        parsers.add(GdIfStmtParser(builder))
        parsers.add(GdWhileStmtParser(builder))
        parsers.add(GdForStmtParser(builder))
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        val stmtOrSuite = mark()
        var ok = suite(true) || stmt(false)

        stmtOrSuite.done(STMT_OR_SUITE)

        return true
    }

    private fun suite(optional: Boolean): Boolean {
        if (!nextTokenIs(NEW_LINE)) return optional
        var ok = true
        val suite = mark()
        advance() // NEW_LINE
        ok = ok && consumeToken(INDENT)

        ok = ok && stmt(false)
        moved = true
        while (ok && moved) {
            ok = ok && stmt(true)
        }
        ok = ok && consumeToken(DEDENT, true)

        GdRecovery.stmt()
        suite.done(SUITE)

        return true
    }

    private fun stmt(optional: Boolean): Boolean {
//        val stmt = mark()
        moved = false

        if (parsers.any { it.parse() }) {
//            stmt.done(STMT)
            moved = true
            return true
        }

        if (!optional) {
            markError("Statement expected")
//            stmt.error("Statement expected")
        }

//        stmt.drop()

        return optional
    }

}
