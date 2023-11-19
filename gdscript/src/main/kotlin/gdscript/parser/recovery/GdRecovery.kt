package gdscript.parser.recovery

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.utils.PsiBuilderUtil.nextTokenIs

object GdRecovery : GdBaseParser {

    fun argumentList(b: PsiBuilder) = recoverUntil(b, *ARG_END)
    fun topLevel(b: PsiBuilder) = recoverUntil(b, *TOP_LEVEL)
    fun setGet(b: PsiBuilder) = recoverUntil(b, *SET_GET)
    fun stmt(b: PsiBuilder) = recoverUntil(b, *STMT)
    fun stmtNoLine(b: PsiBuilder) = recoverUntil(b, *STMT_NO_LINE)

    override fun parse(b: PsiBuilder, optional: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    fun recoverUntil(b: PsiBuilder, vararg elementTypes: IElementType) {
        var any: String? = null
        val m = b.mark()

        while (!b.eof() && !b.nextTokenIs(*elementTypes)) {
            any = b.tokenText
            b.advanceLexer()
        }

        if (any != null) {
            m.error("unexpected '$any'")
        } else {
            m.drop()
        }
    }

}
