package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdPlusExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = PLUS_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        var ok = true
        ok = ok && b.mceAnyOf(SIGN, false, PLUS, MINUS)
        ok = ok && GdExprParser.parse(b, false)

        return ok
    }

}
