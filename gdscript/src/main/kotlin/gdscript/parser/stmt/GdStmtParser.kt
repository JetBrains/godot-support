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
        // Also propagate lambda context started by a func-decl expression (not only arg lists).
        return parseLambda(b, l + 1, optional, b.isArgs || b.isLambda)
    }

    fun parseLambda(b: GdPsiBuilder, l: Int, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.recursionGuard(l, "Lambda")) return false
        b.enterSection(STMT_OR_SUITE)
        if (asLambda) {
            // Mark lambda context so nested statements keep lambda rules
            b.markLambda()
        }
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

        if (asLambda) {
            // In lambda context, allow an optional newline before closing the suite
            b.passToken(NEW_LINE)
        }
        if (asLambda) {
            // Do not consume DEDENT here. Leave it for the outer (non-lambda) context so that
            // statement termination can be inferred by END_STMT when needed.
        } else {
            // Normally require a DEDENT to close the suite. However, Godot allows a dangling comma
            // on a separately indented line that logically belongs to the enclosing list/dict/args.
            // Example:
            //   if cond:
            //       pass
            //           ,
            // In such cases, INDENT is followed by a COMMA that closes the container element.
            // Be tolerant and do not force DEDENT here; leave INDENT/COMMA to the outer parser.
            if (b.nextTokenIs(DEDENT)) {
                ok = ok && b.consumeToken(DEDENT)
            } else if (b.followingTokensAre(INDENT, COMMA) || b.nextTokenIs(COMMA)) {
                // accept suite without consuming DEDENT; outer context will handle comma and dedents
                ok = ok && true
            } else {
                ok = ok && b.consumeToken(DEDENT)
            }
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
                    ok = ok && b.nextTokenIs(SEMICON, NEW_LINE, RRBR, DEDENT, COMMA)
                    if (ok && b.isArgs) {
                        // Do not consume NEW_LINE here; leave it to the statement-specific parser to attach appropriately.
                        b.passToken(SEMICON) || b.nextTokenIs(COMMA)
                    }
//                    if (ok && !b.followingTokensAre(NEW_LINE, DEDENT)) {
//                        b.passToken(NEW_LINE)
//                    }
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
