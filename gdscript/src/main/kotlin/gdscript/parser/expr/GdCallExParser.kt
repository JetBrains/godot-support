package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.parser.common.GdArgListParser
import gdscript.psi.GdTypes.CALL_EX
import gdscript.psi.GdTypes.LRBR

/*
call
    = (attribute [ "(" [ argList ] ")" ])
    TODO - not implemented
    | "." IDENTIFIER "(" [ argList ] ")"
    | "$" ( STRING | IDENTIFIER { '/' IDENTIFIER } );
 */
object GdCallExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = CALL_EX
    override val isNested: Boolean = true

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "CallExpr")) return false
        if (!b.nextTokenIs(LRBR)) return false

        b.pin()
        return GdArgListParser.parse(b, l + 1, false)
    }

}
