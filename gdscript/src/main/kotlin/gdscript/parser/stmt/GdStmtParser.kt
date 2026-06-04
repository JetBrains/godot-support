package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.lang.WhitespacesBinders
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder
import gdscript.parser.recovery.GdRecovery
import gdscript.psi.GdTypes.COMMA
import gdscript.psi.GdTypes.DEDENT
import gdscript.psi.GdTypes.END_STMT
import gdscript.psi.GdTypes.IF_ST
import gdscript.psi.GdTypes.INDENT
import gdscript.psi.GdTypes.NEW_LINE
import gdscript.psi.GdTypes.RRBR
import gdscript.psi.GdTypes.SEMICON
import gdscript.psi.GdTypes.STMT_OR_SUITE
import gdscript.psi.GdTypes.SUITE

object GdStmtParser : GdBaseParser {

    val parsers: MutableList<GdStmtBaseParser> = mutableListOf<GdStmtBaseParser>()
    private val parsersWithoutEmpty: MutableList<GdStmtBaseParser>
    data class StmtParsingResult(val ok: Boolean, val moved: Boolean)

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
        parsersWithoutEmpty = parsers.toMutableList()
        parsers.add(GdEmptyStmtParser)
    }

    /**
     * Tight edge binders so trailing whitespace (e.g. a blank line between a
     * compound block and the next top-level statement/function) doesn't get
     * absorbed into this compound statement's PSI range.
     *
     * Applies to STMT_OR_SUITE, IF_ST, ELIF_ST, ELSE_ST, WHILE_ST, FOR_ST,
     * MATCH_ST -- anything that contains a SUITE.
     */
    fun applyCompoundBinders(marker: PsiBuilder.Marker) {
        marker.setCustomEdgeTokenBinders(
            WhitespacesBinders.DEFAULT_LEFT_BINDER,
            WhitespacesBinders.DEFAULT_RIGHT_BINDER,
        )
    }

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "Stmt")) return false
        // When parsing inside an argument list (e.g., a lambda passed as an argument),
        // continue treating nested statements as lambda-context to allow multiline suites.
        return parseLambda(b, l + 1, optional, b.isArgs)
    }

    fun parseLambda(b: GdPsiBuilder, l: Int, optional: Boolean, asLambda: Boolean): Boolean {
        if (!b.recursionGuard(l, "Lambda")) return false
        val stmtOrSuiteMarker = b.enterSection(STMT_OR_SUITE)
        var ok = suite(b, l + 1, false, asLambda)

        if (!ok)
            ok = stmt(b, l + 1, optional, asLambda, false).ok

        if (!ok) {
            // Both suite() and stmt() failed -- attach an error PSI element as a child of
            // STMT_OR_SUITE so downstream consumers see the empty-body signal directly on
            // the marker. Use b.mark().error() (not b.error()) to bypass the isError guard
            // and to avoid consuming a token (we want a zero-width error at current pos).
            if (!b.isError) {
                b.mark().error("Expected statement")
            }
        }


        // Tight edge binders so blank lines / whitespace after the suite are NOT pulled
        // into STMT_OR_SUITE (and hence the enclosing compound statement).
        stmtOrSuiteMarker.setCustomEdgeTokenBinders(
            WhitespacesBinders.DEFAULT_LEFT_BINDER,
            WhitespacesBinders.DEFAULT_RIGHT_BINDER,
        )

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

        ok = ok && stmt(b, l + 1, false, asLambda).ok
        var moved = true
        while (ok && moved) {
            val res = stmt(b, l + 1, true, asLambda)
            ok = res.ok
            moved = res.moved
        }

        if (asLambda) {
            if (b.nextTokenIs(DEDENT)) {
                if (!b.followingTokensAre(DEDENT, NEW_LINE) && !b.followingTokensAre(DEDENT, END_STMT)) {
                    b.remapCurrentToken(NEW_LINE)
                } else {
                    b.consumeToken(DEDENT)
                }
            }
        } else {
            // Defer DEDENT consumption to AFTER suite.done(SUITE) so the zero-width
            // DEDENT (and any whitespace the lexer emits between the last NEW_LINE and
            // the DEDENT, e.g. a blank line before a top-level `func`) stays OUTSIDE the
            // SUITE PSI range
            ok = ok && b.nextTokenIs(DEDENT)
        }

        if (ok || b.pinned()) {
            // Close the SUITE marker BEFORE running recovery, so any trailing whitespace
            // skipped by recovery (e.g. blank lines after the last suite statement) is
            // attached to the outer scope, not absorbed into SUITE.
            suite.done(SUITE)
            if (!asLambda) {
                // Now consume DEDENT outside the closed SUITE marker. If DEDENT is
                // missing while we are pinned, consumeToken emits an error.
                b.consumeToken(DEDENT)
            }
            GdRecovery.stmt(b)
        } else {
            suite.rollbackTo()
        }

        return ok || b.pinned() || optional
    }

    private fun stmt(b: GdPsiBuilder, l: Int, optional: Boolean, asLambda: Boolean, acceptEmpty: Boolean = true): StmtParsingResult {
        if (!b.recursionGuard(l, "InnerStmt")) return StmtParsingResult(false, false)

        // multiline lambda
        if (optional && asLambda && b.nextTokenIs(NEW_LINE)){
            b.passToken(NEW_LINE)
            return StmtParsingResult(true, true)
        }

        // Track builder position before attempting to parse the statement
        val startPos = b.positionAt

        val parsed =
            (if (acceptEmpty) parsers else parsersWithoutEmpty).any {
                if (asLambda && it is GdEmptyStmtParser) return@any false
                val stmtMarker = b.enterSection(it.STMT_TYPE)
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
                if (!it.endWithEndStmt) {
                    applyCompoundBinders(stmtMarker)
                }
                if (it is GdEmptyStmtParser && ok) {
                    // Empty statement (blank line) is pure whitespace after remap; drop the
                    // marker so it doesn't appear as an empty WHITE_SPACE composite in the PSI.
                    b.dropSection(true)
                } else {
                    b.exitSection(ok, true)
                }

                ok
            }

        if (parsed && b.positionAt > startPos) return StmtParsingResult(true, true)

        if (!optional) {
            b.error("Statement expected")
        }

        return StmtParsingResult(optional, b.positionAt > startPos)
    }
}
