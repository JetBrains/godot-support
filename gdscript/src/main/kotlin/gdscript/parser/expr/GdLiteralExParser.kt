package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.*

class GdLiteralExParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdLiteralExParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        if (mcAnyOf(LITERAL_EX, TRUE, FALSE, STRING_NAME, NODE_PATH_LIT, NUMBER, NULL, NAN, INF)) return true

        if (nextTokenIs(SELF, SUPER)) {
            val m = mark()
            markToken(REF_ID_NM)
            m.done(LITERAL_EX)
            return true
        }

        // func - Array.gd, signal: Vector2.gd, class_name - Array.gd, FileAccess - pass
        // tyhle vyjímky jsou kvůli parseru sdk -> nějaké params se shodují jmenovitě, stejně jako metody
        if (nextTokenIs(IDENTIFIER, GET, SET, MATCH, SIGNAL, FUNC, CLASS_NAME, PASS, CLASS)) {
            val m = mark()
            markToken(REF_ID_NM)
            m.done(LITERAL_EX)
            return true
        }

        if (nextTokenIs(STRING)) {
            val m = mark()
            markToken(STRING_VAL)
            m.done(LITERAL_EX)
            return true
        }

        return false
    }

}
