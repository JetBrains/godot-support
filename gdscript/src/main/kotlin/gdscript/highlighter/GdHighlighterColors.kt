package gdscript.highlighter

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

interface GdHighlighterColors {
    companion object {
        // Red
        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "GD_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
        )

        // Pink
        val FLOW_KEYWORDS = TextAttributesKey.createTextAttributesKey(
            "GD_FLOW_KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD
        )

        // Blue
        val METHOD_DECLARATION = TextAttributesKey.createTextAttributesKey(
            "GD_METHOD_DECLARATION", DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )

        // Blue
        val METHOD_CALL = TextAttributesKey.createTextAttributesKey(
            "GD_METHOD_CALL", DefaultLanguageHighlighterColors.INSTANCE_METHOD
        )

        // Teal
        val CLASS_TYPE = TextAttributesKey.createTextAttributesKey(
            "GD_CLASS_TYPE", DefaultLanguageHighlighterColors.KEYWORD
        )

        // Teal
        val MEMBER = TextAttributesKey.createTextAttributesKey(
            "GD_MEMBER", DefaultLanguageHighlighterColors.CONSTANT
        )

        // Teal
        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "GD_NUMBER", DefaultLanguageHighlighterColors.NUMBER
        )

        // White
        val IDENTIFIER = TextAttributesKey.createTextAttributesKey(
            "GD_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER
        )

        // Grey
        val COMMENT = TextAttributesKey.createTextAttributesKey(
            "GD_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        // Yellow
        val STRING = TextAttributesKey.createTextAttributesKey(
            "GD_STRING", DefaultLanguageHighlighterColors.STRING
        )

        // Blue-ish
        val STRING_FORMAT = TextAttributesKey.createTextAttributesKey(
            "GD_STRING_FORMAT", DefaultLanguageHighlighterColors.STRING
        )

        // Red
        val ANNOTATION = TextAttributesKey.createTextAttributesKey(
            "GD_ANNOTATION", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP
        )

        // Green
        val NODE_PATH = TextAttributesKey.createTextAttributesKey(
            "GD_NODE_PATH", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG
        )

        // Red
        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "GD_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER
        )
    }
}
