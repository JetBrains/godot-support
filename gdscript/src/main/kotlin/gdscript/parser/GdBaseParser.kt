package gdscript.parser

import com.intellij.lang.PsiBuilder

abstract class GdBaseParser {

    abstract fun parse(b: PsiBuilder, optional: Boolean = false): Boolean

}
