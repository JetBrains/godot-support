package gdscript;

import com.intellij.lexer.FlexAdapter;

public class GdLexerHighlighterAdapter extends FlexAdapter {

    public GdLexerHighlighterAdapter() {
        super(new GdLexerHighlighter(null));
    }

}
