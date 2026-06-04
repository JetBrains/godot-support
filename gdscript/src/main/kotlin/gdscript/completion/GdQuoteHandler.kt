package gdscript.completion

import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.editor.Editor
import com.intellij.psi.TokenType
import gdscript.psi.GdTypes

class GdQuoteHandler :
    SimpleTokenSetQuoteHandler(GdTypes.STRING, TokenType.BAD_CHARACTER, GdTypes.NODE_PATH_LEX, GdTypes.NODE_PATH_LIT, GdTypes.STRING_NAME),
    MultiCharQuoteHandler {


    /**
     * `NODE_PATH_LEX` covers both `$"path"` / `%"path"` (quoted, behaves like a
     * string) and `$foo` / `%foo` (unquoted identifier, does NOT behave like a
     * string). The platform's quote-handling logic treats every token in our set
     * as a string -- which is wrong for the unquoted form (typing `"` next to
     * `$foo` would mispair). Return false here to opt the unquoted form out.
     */
    private fun isStringLikeNodePath(iterator: HighlighterIterator): Boolean {
        if (iterator.tokenType != GdTypes.NODE_PATH_LEX) return true
        val doc = iterator.document ?: return true
        val start = iterator.start
        // The highlighter emits `$"..."` / `%"..."` as TWO NODE_PATH_LEX tokens: the `$`/`%`
        // prefix and the quoted part. When the iterator is on the quoted-part token it
        // begins with the quote itself and is always string-like.
        val firstChar = doc.charsSequence[start]
        if (firstChar == '"' || firstChar == '\'') return true
        // Prefix token (`$`/`%`): string-like only if immediately followed by a quote;
        // the unquoted `$foo` / `%foo` form must opt out so `"` does not mispair.
        val secondPos = start + 1
        return secondPos < doc.textLength && doc.charsSequence[secondPos] == '"'
    }

    override fun isInsideLiteral(iterator: HighlighterIterator): Boolean =
        isStringLikeNodePath(iterator) && super.isInsideLiteral(iterator)

    override fun isOpeningQuote(iterator: HighlighterIterator, offset: Int): Boolean =
        isStringLikeNodePath(iterator) && super.isOpeningQuote(iterator, offset)

    override fun isClosingQuote(iterator: HighlighterIterator, offset: Int): Boolean =
        isStringLikeNodePath(iterator) && super.isClosingQuote(iterator, offset)

    override fun hasNonClosedLiteral(editor: Editor, iterator: HighlighterIterator, offset: Int): Boolean =
        isStringLikeNodePath(iterator) && super.hasNonClosedLiteral(editor, iterator, offset)

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
