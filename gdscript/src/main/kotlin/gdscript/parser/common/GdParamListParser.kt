package gdscript.parser.common

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser

class GdParamListParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdParamListParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(optional: Boolean): Boolean {
        TODO("Not yet implemented")
    }
}
