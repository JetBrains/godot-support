package tscn.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import tscn.TscnLexerHighlighterAdapter

class TscnSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return TscnLexerHighlighterAdapter()
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        return pack(ATTRIBUTES[iElementType])
    }

    companion object {
        val ATTRIBUTES: MutableMap<IElementType, TextAttributesKey> = HashMap()

        init {
            fillMap(ATTRIBUTES, TscnTokenTypeSet.SYNTAX_TOKENS, TscnHighlighterColors.SYNTAX_TOKENS)
        }
    }
}
