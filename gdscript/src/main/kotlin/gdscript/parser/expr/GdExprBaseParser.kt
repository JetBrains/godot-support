package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser

abstract class GdExprBaseParser : GdBaseParser {

    abstract val EXPR_TYPE: IElementType

    constructor(builder: PsiBuilder): super(builder)

}
