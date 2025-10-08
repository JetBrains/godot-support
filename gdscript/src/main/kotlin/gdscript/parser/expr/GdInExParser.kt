package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.IN_EX
import gdscript.psi.GdTypes.NEGATE

// comparison { ("in" | "not" "in") comparison }
object GdInExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = IN_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "InExpr")) return false

        // Accept either 'in' or the composite operator 'not in' (lexer: NEGATE followed by IN)
        var ok: Boolean
        if (b.followingTokensAre(NEGATE, IN)) {
            // consume both as operator
            ok = b.consumeToken(NEGATE, optional = false, pin = true)
            ok = ok && b.consumeToken(IN)
        } else {
            ok = b.consumeToken(IN, pin = true)
        }

        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
