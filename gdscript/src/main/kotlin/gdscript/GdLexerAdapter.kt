package gdscript

import com.intellij.lexer.FlexAdapter
import gdscript.psi.GdTypes


class GdLexerAdapter : FlexAdapter(GdLexer(null)) {

    private val gdLexer: GdLexer get() = flex as GdLexer

    override fun getState(): Int {
        return if (gdLexer.hasNonDefaultState() || tokenType == GdTypes.DEDENT) 1 else 0
    }

    // In our case getState() doesn't return the full lexer state (indent stack, paren depth, etc.).
    // So restoring from initialState is not possible - we always reset to a clean state instead.
    // IntelliJ selects startOffset at a position where getState() previously returned 0
    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        gdLexer.resetState()
        super.start(buffer, startOffset, endOffset, 0)
    }
}
