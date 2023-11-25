package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*
import gdscript.utils.PsiBuilderUtil.markToken
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdLiteralExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = LITERAL_EX

    override fun parse(b: GdPsiBuilder, optional: Boolean): Boolean {
        if (b.nextTokenIs(LITERAL_EX, TRUE, FALSE, STRING_NAME, NODE_PATH_LIT, NUMBER, NULL, NAN, INF)) {
            b.advanceLexer()
            return true
        }

        if (b.nextTokenIs(SELF, SUPER)) {
            b.markToken(REF_ID_NM)
            return true
        }

        // func - Array.gd, signal: Vector2.gd, class_name - Array.gd, FileAccess - pass
        // tyhle vyjímky jsou kvůli parseru sdk -> nějaké params se shodují jmenovitě, stejně jako metody
        if (b.nextTokenIs(IDENTIFIER, GET, SET, MATCH, SIGNAL, FUNC, CLASS_NAME, PASS, CLASS)) {
            b.markToken(REF_ID_NM)
            return true
        }

        if (b.nextTokenIs(STRING)) {
            b.markToken(STRING_VAL)
            return true
        }

        return false
    }

    fun parseAndMark(b: GdPsiBuilder): Boolean {
        val expr = b.mark()
        val ok = parse(b)
        if (ok) expr.done(LITERAL_EX)
        else expr.rollbackTo()

        return ok
    }

}
