package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdPsiBuilder
import gdscript.psi.GdTypes.*

object GdLiteralExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = LITERAL_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (b.nextTokenIs(LITERAL_EX, TRUE, FALSE, STRING_NAME, NODE_PATH_LIT, NUMBER, NULL, NAN, INF)) {
            b.advance()
            return true
        }

        if (b.mcToken(REF_ID_NM, SELF, SUPER)) {
            return true
        }

        // func - Array.gd, signal: Vector2.gd, class_name - Array.gd, FileAccess - pass
        // tyhle vyjímky jsou kvůli parseru sdk -> nějaké params se shodují jmenovitě, stejně jako metody
        if (b.mcToken(REF_ID_NM, IDENTIFIER, GET, SET, MATCH, SIGNAL, FUNC, CLASS_NAME, PASS, CLASS)) {
            return true
        }

        if (b.mcToken(STRING_VAL, STRING)) {
            return true
        }

        return false
    }

    fun parseAndMark(b: GdPsiBuilder): Boolean {
        b.enterSection(LITERAL_EX)
        val ok = parse(b)
        b.exitSection(ok)

        return ok
    }

}
