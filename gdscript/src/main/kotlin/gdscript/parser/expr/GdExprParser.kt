package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser

class GdExprParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdExprParser
    }

    val parsers = mutableListOf<GdBaseParser>()

    constructor(builder: PsiBuilder): super(builder) {
        parsers.add(GdLiteralExParser(builder))
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        return parsers.any { it.parse() }
    }
}
