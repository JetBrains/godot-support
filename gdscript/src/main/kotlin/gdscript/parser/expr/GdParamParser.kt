package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import gdscript.parser.GdBaseParser

class GdParamParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdParamParser
    }

    constructor(builder: PsiBuilder): super(builder) {
        INSTANCE = this
    }

    override fun parse(): Boolean {
        TODO("Not yet implemented")
    }

}
