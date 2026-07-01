package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes

/**
 * Parses a lone/extra semicolon as an empty statement (`;`).
 *
 * [parse] only checks that the current token is a `;`; it does NOT consume it. The `;` is consumed
 * by [parseEndStmt] -> [GdPsiBuilder.mceEndStmt], which builds the nested END_STMT terminator, so
 * the resulting shape is `EMPTY_STMT > END_STMT > ;`.
 *
 * A real statement keeps its own terminator (`pass;` stays `FLOW_ST > END_STMT > ;`); only
 * leftover/standalone semicolons become EMPTY_STMTs -- e.g. `pass;;` -> `FLOW_ST` + `EMPTY_STMT`,
 * and `;;;` -> three `EMPTY_STMT`s. This lets Godot's empty statements parse without error PSI.
 */
object GdSemicolonStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = GdTypes.EMPTY_STMT

    override val endWithEndStmt: Boolean = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "SemicolonStmt")) return false
        return b.nextTokenIs(GdTypes.SEMICON)
    }
}
