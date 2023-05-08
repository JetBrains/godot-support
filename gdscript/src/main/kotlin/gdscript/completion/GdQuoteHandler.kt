package gdscript.completion

import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType
import gdscript.psi.GdTypes

class GdQuoteHandler : SimpleTokenSetQuoteHandler, MultiCharQuoteHandler {

    constructor() : super(GdTypes.STRING, TokenType.BAD_CHARACTER, GdTypes.BAD_CHARACTER)

    override fun getClosingQuote(iterator: HighlighterIterator, offset: Int): CharSequence? {
        val document = iterator.document ?: return null
        if (offset >= 3) {
            val quote = document.getText(TextRange(offset - 3, offset))
            if ("\"\"\"" == quote) return quote
        }
        if (offset >= 1) {
            val quote = document.getText(TextRange(offset - 1, offset))
            if (quote == "\"" || quote == "'") return quote
        }

        return null
    }

}
