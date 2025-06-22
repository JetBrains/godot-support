package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdLiteralExParser : GdExprBaseParser() {

    override val EXPR_TYPE: IElementType = LITERAL_EX

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        if (!b.recursionGuard(l, "LiteralExpr")) return false
        if (b.nextTokenIs(LITERAL_EX, TRUE, FALSE, STRING_NAME, NODE_PATH_LIT, NUMBER, NULL, NAN, INF)) {
            b.advance()
            return true
        }

        if (b.mcToken(REF_ID_NM, SELF, SUPER)) {
            return true
        }

        // func - Array.gd, signal: Vector2.gd, class_name - Array.gd, FileAccess - pass
        // these exceptions are due to the SDK parser -> some parameters match by name, just like methods
        if (parseExtendedRefId(b)) {
            return true
        }

        if (b.mcToken(STRING_VAL_NM, STRING)) {
            return true
        }

        return false
    }

    fun parseAndMark(b: GdPsiBuilder, l: Int): Boolean {
        if (!b.recursionGuard(l, "LiteralExprMark")) return false
        b.enterSection(LITERAL_EX)
        val ok = parse(b, l + 1)
        b.exitSection(ok)

        return ok
    }

    fun checkExtendedRefId(b: GdPsiBuilder): Boolean {
        return b.nextTokenIs(IDENTIFIER, GET, SET, MATCH, SIGNAL, FUNC, CLASS_NAME, PASS, CLASS)
    }

    fun parseExtendedRefId(b: GdPsiBuilder, type: IElementType? = null): Boolean {
        if (checkExtendedRefId(b)) {
            val m = b.mark()
            b.advance()
            if (type != null) m.done(type)
            else m.done(REF_ID_NM)
            return true
        }

        return false
    }

}
