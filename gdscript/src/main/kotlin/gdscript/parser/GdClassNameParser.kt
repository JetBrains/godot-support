package gdscript.parser

import com.intellij.lang.PsiBuilder
import gdscript.psi.GdTypes.*

class GdClassNameParser : GdBaseParser {

    constructor(builder: PsiBuilder): super(builder)

    fun parseClassName(): Boolean {
        if (!nextTokenIs(CLASS_NAME)) return false

        val m = mark()
        val ok =
                consumeToken(CLASS_NAME) &&
                markAndConsumeIdentifier(CLASS_NAME_NMI)

        return true
    }

}
