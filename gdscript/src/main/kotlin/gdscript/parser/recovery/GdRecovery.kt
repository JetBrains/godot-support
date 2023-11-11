package gdscript.parser.recovery

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.utils.PsiBuilderUtil.nextTokenIs

class GdRecovery : GdBaseParser {

    companion object {
        lateinit var recovery: GdRecovery

        fun argumentList(b: PsiBuilder) = recovery.recoverUntil(b, *ARG_END)
        fun topLevel(b: PsiBuilder) = recovery.recoverUntil(b, *TOP_LEVEL)
        fun setGet(b: PsiBuilder) = recovery.recoverUntil(b, *SET_GET)
        fun stmt(b: PsiBuilder) = recovery.recoverUntil(b, *STMT)
        fun stmtNoLine(b: PsiBuilder) = recovery.recoverUntil(b, *STMT_NO_LINE)
    }

    constructor() {
        recovery = this
    }

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
