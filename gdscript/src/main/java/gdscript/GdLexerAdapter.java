package gdscript;

import com.intellij.lexer.FlexAdapter;

public class GdLexerAdapter extends FlexAdapter {

    public GdLexerAdapter() {
        super(new GdLexer(null));
    }

}
