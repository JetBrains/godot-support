package gdscript.codeInsight

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.VersionedWordsScanner
import com.intellij.lang.cacheBuilder.WordOccurrence
import com.intellij.util.Processor
import gdscript.GdLexerAdapter
import gdscript.highlighter.GdTokenTypeSet

/**
 * Indexes GDScript words for Find Usages.
 *
 * A plain [DefaultWordsScanner] treats `$` as an identifier character, so it indexes a `$Node`
 * node-path token as the single word `$Node` and never indexes the bare name `Node`. That stops
 * a Find Usages search that starts from the node's declaration in a `.tscn` file from finding
 * this `.gd` usage - see [gdscript.search.GdOwnReferencesSearcher]. This scanner also splits a
 * `$Node`/`%UniqueNode` token at its leading sigil, so the bare node name is a word on its own.
 */
class GdWordsScanner : VersionedWordsScanner() {

    override fun getVersion(): Int = 2

    override fun processWords(fileText: CharSequence, processor: Processor<in WordOccurrence>) {
        val lexer = GdLexerAdapter()
        lexer.start(fileText)
        val occurrence = WordOccurrence(fileText, 0, 0, null)

        var type = lexer.tokenType
        while (type != null) {
            val start = lexer.tokenStart
            val end = lexer.tokenEnd
            val kind = if (GdTokenTypeSet.COMMENT.contains(type)) WordOccurrence.Kind.COMMENTS else WordOccurrence.Kind.CODE
            if (!DefaultWordsScanner.stripWords(processor, fileText, start, end, kind, occurrence, false)) return

            if (GdTokenTypeSet.NODE_PATH.contains(type)) {
                // Skip the leading "$"/"%" sigil, so the bare node name splits off as its own
                // word instead of gluing to the sigil.
                if (!DefaultWordsScanner.stripWords(processor, fileText, start + 1, end, WordOccurrence.Kind.CODE, occurrence, false)) return
            }

            lexer.advance()
            type = lexer.tokenType
        }
    }
}
