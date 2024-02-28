package tscn.highlighter

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object TscnHighlighterColors {

    val SYNTAX_TOKENS = TextAttributesKey.createTextAttributesKey(
            "TSCN_SYNTAX_TOKENS", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP
    )

    val STRING = TextAttributesKey.createTextAttributesKey(
        "TSCN_STRING", DefaultLanguageHighlighterColors.STRING
    )

    val RES_STRING = TextAttributesKey.createTextAttributesKey(
        "TSCN_RES_STRING", DefaultLanguageHighlighterColors.STRING
    )

    val ATTRIBUTES = TextAttributesKey.createTextAttributesKey(
        "TSCN_ATTRIBUTES", DefaultLanguageHighlighterColors.IDENTIFIER
    )

    val NODE_RESOURCE = TextAttributesKey.createTextAttributesKey(
            "TSCN_NODE_RESOURCE", DefaultLanguageHighlighterColors.CONSTANT
    )

    val MEMBER_REF = TextAttributesKey.createTextAttributesKey(
        "TSCN_MEMBER_REF", DefaultLanguageHighlighterColors.FUNCTION_CALL
    )

    val NODE_TYPE = TextAttributesKey.createTextAttributesKey(
            "TSCN_NODE_TYPE", DefaultLanguageHighlighterColors.STRING
    )

    val ATTRIBUTE_VALUES = TextAttributesKey.createTextAttributesKey(
            "TSCN_ATTRIBUTE_VALUES", DefaultLanguageHighlighterColors.NUMBER
    )
}
