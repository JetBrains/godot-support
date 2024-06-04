package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

// subscription { "." IDENTIFIER } ;
object GdAttributeExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = ATTRIBUTE_EX
    override val isNested = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "AttributeExpr")) return false
        var ok = b.consumeToken(DOT, pin = true)
        ok = ok && b.mceIdentifier(REF_ID_NM)
        b.errorPin(ok, "expression")

        return ok
    }

}
