package gdscript.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import gdscript.GdLexerHighlighterAdapter

class GdSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return GdLexerHighlighterAdapter()
    }

    override fun getTokenHighlights(iElementType: IElementType): Array<TextAttributesKey> {
        return pack(ATTRIBUTES[iElementType])
    }

    companion object {
        val ATTRIBUTES: MutableMap<IElementType, TextAttributesKey> = HashMap()

        init {
            fillMap(ATTRIBUTES, GdTokenTypeSet.KEYWORDS, GdHighlighterColors.KEYWORD)
            fillMap(ATTRIBUTES, GdTokenTypeSet.VOID, GdHighlighterColors.BASE_TYPE)
            fillMap(ATTRIBUTES, GdTokenTypeSet.FLOW_KEYWORDS, GdHighlighterColors.FLOW_KEYWORDS)
            fillMap(ATTRIBUTES, GdTokenTypeSet.CLASS_TYPE, GdHighlighterColors.CLASS_TYPE)
            fillMap(ATTRIBUTES, GdTokenTypeSet.MEMBER, GdHighlighterColors.MEMBER)
            fillMap(ATTRIBUTES, GdTokenTypeSet.NUMBERS, GdHighlighterColors.NUMBER)
            fillMap(ATTRIBUTES, GdTokenTypeSet.STRINGS, GdHighlighterColors.STRING)
            fillMap(ATTRIBUTES, GdTokenTypeSet.STRING_FORMAT, GdHighlighterColors.STRING_FORMAT)
            fillMap(ATTRIBUTES, GdTokenTypeSet.STRING_NAME, GdHighlighterColors.STRING_NAME)
            fillMap(ATTRIBUTES, GdTokenTypeSet.BAD_CHARACTERS, GdHighlighterColors.BAD_CHARACTER)
            fillMap(ATTRIBUTES, GdTokenTypeSet.ANNOTATIONS, GdHighlighterColors.ANNOTATION)
            fillMap(ATTRIBUTES, GdTokenTypeSet.NODE_PATH, GdHighlighterColors.NODE_PATH)
            fillMap(ATTRIBUTES, GdTokenTypeSet.NODE_STRING_PATH, GdHighlighterColors.NODE_STRING_PATH)
            fillMap(ATTRIBUTES, GdTokenTypeSet.COMMENT, GdHighlighterColors.COMMENT)
            fillMap(ATTRIBUTES, TokenSet.EMPTY, GdHighlighterColors.DANGER)
            fillMap(ATTRIBUTES, TokenSet.EMPTY, GdHighlighterColors.WARNING)
            fillMap(ATTRIBUTES, TokenSet.EMPTY, GdHighlighterColors.NOTE)
        }
    }
}
