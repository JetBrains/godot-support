package gdscript.parser.recovery

import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

object GdRecovery : GdBaseParser {

    fun argumentList(b: GdPsiBuilder, result: Boolean = true) = recoverUntil(b, result, *ARG_END)
    fun topLevel(b: GdPsiBuilder, result: Boolean = true) = recoverUntil(b, result, *TOP_LEVEL)
    fun setGet(b: GdPsiBuilder, result: Boolean = true) = recoverUntil(b, result, *SET_GET)
    fun stmt(b: GdPsiBuilder, result: Boolean = true) = recoverUntil(b, result, *STMT)
    fun stmtNoLine(b: GdPsiBuilder, result: Boolean = true) = recoverUntil(b, result, *STMT_NO_LINE)

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    fun recoverUntil(b: GdPsiBuilder, result: Boolean, vararg elementTypes: IElementType): Boolean {
        if (!result && !b.pinned()) return true
        var any: String? = null
        val m = b.mark()

        while (!b.eof && !b.nextTokenIs(*elementTypes)) {
            any = b.tokenText
            b.advance()
        }

        if (any != null && !b.isError && !any.isEmpty()) {
            m.error("recovery unexpected '${any}'")
        }
        else {
            m.drop()
        }

        return true
    }

}
