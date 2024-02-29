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
            fillMap(ATTRIBUTES, TscnTokenTypeSet.STRING_TOKENS, TscnHighlighterColors.STRING)
            fillMap(ATTRIBUTES, TscnTokenTypeSet.RES_STRING_TOKENS, TscnHighlighterColors.RES_STRING)
            fillMap(ATTRIBUTES, TscnTokenTypeSet.GODOT_MEMBER_REF_TOKENS, TscnHighlighterColors.MEMBER_REF)
            fillMap(ATTRIBUTES, TscnTokenTypeSet.VALUE_TOKENS, TscnHighlighterColors.ATTRIBUTE_VALUES)
            fillMap(ATTRIBUTES, TscnTokenTypeSet.ATTRIBUTE_TOKENS, TscnHighlighterColors.ATTRIBUTES)
            fillMap(ATTRIBUTES, TscnTokenTypeSet.RESOURCE_TOKENS, TscnHighlighterColors.NODE_RESOURCE)
            fillMap(ATTRIBUTES, TscnTokenTypeSet.GODOT_CLASS_REF_TOKENS, TscnHighlighterColors.NODE_TYPE)
        }
    }
}
