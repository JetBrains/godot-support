package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser

abstract class GdExprBaseParser : GdBaseParser() {

    abstract val EXPR_TYPE: IElementType

}
