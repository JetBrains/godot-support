package gdscript.parser.recovery

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser

class GdRecovery : GdBaseParser {

    companion object {
        lateinit var recovery: GdRecovery

        fun argumentList() = recovery.recoverUntil(*ARG_END)
        fun topLevel() = recovery.recoverUntil(*TOP_LEVEL)
        fun setGet() = recovery.recoverUntil(*SET_GET)
        fun stmt() = recovery.recoverUntil(*STMT)
        fun stmtNoLine() = recovery.recoverUntil(*STMT_NO_LINE)
    }

    constructor(builder: PsiBuilder): super(builder) {
        recovery = this
    }

    override fun parse(optional: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    fun recoverUntil(vararg elementTypes: IElementType) {
        var any: String? = null
        val m = mark()

        while (!builder.eof() && !nextTokenIs(*elementTypes)) {
            any = builder.tokenText
            advance()
        }

        if (any != null) {
            m.error("unexpected '$any'")
        } else {
            m.drop()
        }
    }

}
