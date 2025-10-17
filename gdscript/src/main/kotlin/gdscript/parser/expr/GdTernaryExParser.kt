package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.*

/**
 * Parses GDScript's inline conditional (a.k.a. "ternary") expression and builds a PSI node of type [TERNARY_EX].
 *
 * Grammar (simplified):
 *   logicOr [ "if" logicOr "else" logicOr ] ;
 *
 * Examples:
 *   - val x = 1 if cond else 2
 *   - print("ok") if a > 0 else print("bad")
 *
 * Notes:
 *   - This is an expression form of if/else. It differs from the statement form because it returns a value and
 *     can appear wherever an expression is allowed.
 *   - The PSI element type used for this node is [TERNARY_EX], declared in generated GdTypes.
 */
// logicOr [ "if" logicOr "else" logicOr ] ;
object GdTernaryExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = TERNARY_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "TernaryExpr")) return false

        var ok = b.consumeToken(IF, pin = true)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")
        ok = ok && b.consumeToken(ELSE)
        ok = ok && GdExprParser.parseFrom(b, l, optional, POSITION + 1)
        b.errorPin(ok, "expression")

        return ok || b.pinned()
    }

}
