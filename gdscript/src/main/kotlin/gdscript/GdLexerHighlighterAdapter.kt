package gdscript

import com.intellij.lexer.FlexAdapter

class GdLexerHighlighterAdapter : FlexAdapter {

    constructor() : super(GdLexerHighlighter(null));

}
