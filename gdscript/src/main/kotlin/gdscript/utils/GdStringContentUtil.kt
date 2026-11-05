package gdscript.utils

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdStringValRef

/** The content boundary excludes the prefix and the complete literal delimiters. */
fun stringContentRange(text: String): TextRange {
    val prefix = if (text.firstOrNull() in listOf('&', 'r', '^')) 1 else 0
    val quote = text.getOrNull(prefix)
    if (quote != '"' && quote != '\'') return TextRange(0, text.length)
    // Distinguish single and triple quotes.
    val delimiter = quote.toString().repeat(if (text.startsWith(quote.toString().repeat(3), prefix)) 3 else 1)
    val start = prefix + delimiter.length
    val closingStart = text.length - delimiter.length
    val backslashes = text.take(closingStart.coerceAtLeast(0)).takeLastWhile { it == '\\' }.length
    // Distinguish closed and unclosed strings.
    val optionalClosingQuote = delimiter == "'" && (prefix == 0 || text[0] == '^')
    val escapedClosingQuote = optionalClosingQuote && backslashes % 2 != 0
    val end = if (closingStart >= start && text.endsWith(delimiter) && !escapedClosingQuote) {
        closingStart
    } else text.length
    return TextRange(start, end)
}

/** Tests the caret against the same boundary that the string manipulator supplies. */
fun isInsideStringContent(position: PsiElement, offset: Int): Boolean {
    val string = PsiTreeUtil.getParentOfType(position, GdStringValRef::class.java, false) ?: return false
    val range = stringContentRange(string.text).shiftRight(string.textRange.startOffset)
    return offset in range.startOffset..range.endOffset
}

/**
 * Removes one quote pair and the optional Godot literal prefix from LSP insert text or a lookup string.
 * Godot sends one pair of single or double quotes, never triple quotes.
 */
fun String.toStringContent(): String? {
    val text = if (firstOrNull() == '&' || firstOrNull() == '^') drop(1) else this
    val quote = text.firstOrNull()
    return if (text.length >= 2 && (quote == '"' || quote == '\'') && text.last() == quote) {
        text.substring(1, text.length - 1)
    } else null
}
