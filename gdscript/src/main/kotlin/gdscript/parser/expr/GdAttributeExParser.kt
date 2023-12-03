package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.ATTRIBUTE_EX
import gdscript.psi.GdTypes.DOT

object GdAttributeExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = ATTRIBUTE_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(DOT, pin = true)
        ok = ok && GdExprParser.parse(b)
        b.errorPin(ok, "expression")

        return ok
    }

}
