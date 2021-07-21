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

    // Red
    TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "GD_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER
    );

}
