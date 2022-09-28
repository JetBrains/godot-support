package gdscript.highlighter;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

public interface GdHighlighterColors {

    // Red
    TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey(
            "GD_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
    );

    // Pink
    TextAttributesKey FLOW_KEYWORDS = TextAttributesKey.createTextAttributesKey(
            "GD_FLOW_KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD
    );

    // Blue
    TextAttributesKey METHOD_DECLARATION = TextAttributesKey.createTextAttributesKey(
            "GD_METHOD_DECLARATION", DefaultLanguageHighlighterColors.INSTANCE_METHOD
    );

    // Blue
    TextAttributesKey METHOD_CALL = TextAttributesKey.createTextAttributesKey(
            "GD_METHOD_CALL", DefaultLanguageHighlighterColors.INSTANCE_METHOD
    );

    // Teal
    TextAttributesKey CLASS_TYPE = TextAttributesKey.createTextAttributesKey(
            "GD_CLASS_TYPE", DefaultLanguageHighlighterColors.KEYWORD
    );

    // Teal
    TextAttributesKey MEMBER = TextAttributesKey.createTextAttributesKey(
            "GD_MEMBER", DefaultLanguageHighlighterColors.CONSTANT
    );

    // Teal
    TextAttributesKey NUMBER = TextAttributesKey.createTextAttributesKey(
            "GD_NUMBER", DefaultLanguageHighlighterColors.NUMBER
    );

    // White
    TextAttributesKey IDENTIFIER = TextAttributesKey.createTextAttributesKey(
            "GD_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER
    );

    // Grey
    TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey(
            "GD_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT
    );

    // Yellow
    TextAttributesKey STRING = TextAttributesKey.createTextAttributesKey(
            "GD_STRING", DefaultLanguageHighlighterColors.STRING
    );

    // Red
    TextAttributesKey ANNOTATION = TextAttributesKey.createTextAttributesKey(
            "GD_ANNOTATION", DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP
    );

    // Green
    TextAttributesKey NODE_PATH = TextAttributesKey.createTextAttributesKey(
            "GD_NODE_PATH", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG
    );

    // Red
    TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "GD_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER
    );

}
