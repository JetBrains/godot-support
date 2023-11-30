package gdscript.parser.stmt

import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdAnnotationParser
import gdscript.psi.GdTypes.ANNOTATION_STMT

object GdAnnotationStmtParser : GdAnnotationParser, GdStmtBaseParser {

    override val STMT_TYPE: IElementType = ANNOTATION_STMT
    override val endWithEndStmt: Boolean = true
    override val pinnable: Boolean = false

}
