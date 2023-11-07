package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

class GdLiteralExParser : GdExprBaseParser {

    override val EXPR_TYPE: IElementType = LITERAL_EX

    companion object {
        lateinit var INSTANCE: GdLiteralExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (nextTokenIs(LITERAL_EX, TRUE, FALSE, STRING_NAME, NODE_PATH_LIT, NUMBER, NULL, NAN, INF)) {
            advance()
            return true
        }

        if (nextTokenIs(SELF, SUPER)) {
            markToken(REF_ID_NM)
            return true
        }

        // func - Array.gd, signal: Vector2.gd, class_name - Array.gd, FileAccess - pass
        // tyhle vyjímky jsou kvůli parseru sdk -> nějaké params se shodují jmenovitě, stejně jako metody
        if (nextTokenIs(IDENTIFIER, GET, SET, MATCH, SIGNAL, FUNC, CLASS_NAME, PASS, CLASS)) {
            markToken(REF_ID_NM)
            return true
        }

        if (nextTokenIs(STRING)) {
            markToken(STRING_VAL)
            return true
        }

        return false
    }

    fun parseAndMark(): Boolean {
        val expr = mark()
        val ok = parse()
        if (ok) expr.done(LITERAL_EX)
        else expr.rollbackTo()

        return ok
    }

}
