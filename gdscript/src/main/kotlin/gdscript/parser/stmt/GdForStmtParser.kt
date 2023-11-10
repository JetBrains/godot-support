package gdscript.parser.stmt

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdExprParser
import gdscript.psi.GdTypes.*

class GdForStmtParser : GdStmtBaseParser {

    override val STMT_TYPE: IElementType = FOR_ST

    constructor(builder: PsiBuilder) : super(builder)

    override fun parse(optional: Boolean): Boolean {
        if (!nextTokenIs(FOR)) return false

        advance() // for
        var ok = true
        ok = ok && mceIdentifier(VAR_NMI)
        ok = ok && consumeToken(IN, true)
        ok = ok && GdExprParser.INSTANCE.parse()
        ok = ok && consumeToken(COLON, true)
        ok = ok && GdStmtParser.INSTANCE.parse()

        return ok
    }

}
