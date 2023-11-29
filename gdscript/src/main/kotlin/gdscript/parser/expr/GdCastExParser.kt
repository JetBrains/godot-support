package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdTypedParser
import gdscript.psi.GdTypes.AS
import gdscript.psi.GdTypes.CAST_EX

object GdCastExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = CAST_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = true

        ok = ok && b.consumeToken(AS)
        ok = ok && GdTypedParser.typedVal(b, false)

        return ok
    }

}
