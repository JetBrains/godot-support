package gdscript.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import gdscript.GdLexerHighlighterAdapter

class GdSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return GdLexerHighlighterAdapter()
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        return pack(ATTRIBUTES[iElementType])
    }

    companion object {
        val ATTRIBUTES: Map<IElementType, TextAttributesKey> = HashMap()

        init {
            fillMap(ATTRIBUTES, GdTokenTypeSet.KEYWORDS, GdHighlighterColors.KEYWORD)
            fillMap(ATTRIBUTES, GdTokenTypeSet.FLOW_KEYWORDS, GdHighlighterColors.FLOW_KEYWORDS)
            fillMap(ATTRIBUTES, GdTokenTypeSet.CLASS_TYPE, GdHighlighterColors.CLASS_TYPE)
            fillMap(ATTRIBUTES, GdTokenTypeSet.IDENTIFIERS, GdHighlighterColors.IDENTIFIER)
            fillMap(ATTRIBUTES, GdTokenTypeSet.NUMBERS, GdHighlighterColors.NUMBER)
            fillMap(ATTRIBUTES, GdTokenTypeSet.STRINGS, GdHighlighterColors.STRING)
            fillMap(ATTRIBUTES, GdTokenTypeSet.BAD_CHARACTERS, GdHighlighterColors.BAD_CHARACTER)
            fillMap(ATTRIBUTES, GdTokenTypeSet.ANNOTATIONS, GdHighlighterColors.ANNOTATION)
            fillMap(ATTRIBUTES, GdTokenTypeSet.NODE_PATH, GdHighlighterColors.NODE_PATH)
            fillMap(ATTRIBUTES, GdTokenTypeSet.COMMENT, GdHighlighterColors.COMMENT)
        }
    }
}
