package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.IS
import gdscript.psi.GdTypes.IS_EX

object GdIsExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = IS_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = b.consumeToken(IS, pin = true)
        ok = ok && GdTypedParser.typedVal(b, false)
        b.errorPin(ok, "type")

        return ok || b.pinned()
    }

}
