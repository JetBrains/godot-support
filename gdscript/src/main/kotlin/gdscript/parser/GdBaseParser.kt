package gdscript.parser

import com.intellij.lang.PsiBuilder

interface GdBaseParser {

    fun parse(b: PsiBuilder, optional: Boolean = false): Boolean

}
