package gdscript.parser.stmt

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes

object GdEmptyStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = TokenType.WHITE_SPACE

    override val endWithEndStmt: Boolean = false

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "StmtEmptyLine")) return false
        if (b.nextTokenIs(GdTypes.NEW_LINE)) {
            b.remapCurrentToken(TokenType.WHITE_SPACE)
            b.advance()
            return true
        }

        return false
    }
}
