package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

abstract class GdExprBaseParser : GdBaseParser {

    abstract val EXPR_TYPE: IElementType
    var POSITION: Int = 999
    open val isNested = false

}
