package gdscript.parser.expr

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser

interface GdExprBaseParser : GdBaseParser {

    val EXPR_TYPE: IElementType

}
