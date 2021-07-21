package gdscript.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import gdscript.GdLexerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GdSyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey> ATTRIBUTES = new HashMap<>();

    static {
        fillMap(ATTRIBUTES, GdTokenTypeSet.KEYWORDS, GdHighlighterColors.KEYWORD);
        fillMap(ATTRIBUTES, GdTokenTypeSet.FLOW_KEYWORDS, GdHighlighterColors.FLOW_KEYWORDS);
        fillMap(ATTRIBUTES, GdTokenTypeSet.IDENTIFIERS, GdHighlighterColors.IDENTIFIER);
        fillMap(ATTRIBUTES, GdTokenTypeSet.NUMBERS, GdHighlighterColors.NUMBER);
        fillMap(ATTRIBUTES, GdTokenTypeSet.STRINGS, GdHighlighterColors.STRING);
        fillMap(ATTRIBUTES, GdTokenTypeSet.BAD_CHARACTERS, GdHighlighterColors.BAD_CHARACTER);
        fillMap(ATTRIBUTES, GdTokenTypeSet.ANNOTATIONS, GdHighlighterColors.ANNOTATION);
        fillMap(ATTRIBUTES, GdTokenTypeSet.COMMENT, GdHighlighterColors.COMMENT);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new GdLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {
        return pack(ATTRIBUTES.get(iElementType));
    }

}