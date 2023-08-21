package gdscript.parser.recovery

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser

class GdRecovery : GdBaseParser {

    companion object {
        lateinit var recovery: GdRecovery

        fun argumentList() = recovery.recoverUntil(*ARG_END)
    }

    constructor(builder: PsiBuilder): super(builder) {
        recovery = this
    }

    override fun parse(): Boolean {
        TODO("Not yet implemented")
    }

    fun recoverUntil(vararg elementTypes: IElementType) {
        var any: String? = null
        val m = mark()

        while (!nextTokenIs(*elementTypes)) {
            any = builder.tokenText
            advance()
        }

        if (any != null) {
            m.error("expected COMMA, got '$any'")
        } else {
            m.drop()
        }
    }

}
