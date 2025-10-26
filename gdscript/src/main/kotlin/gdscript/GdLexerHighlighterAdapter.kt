package gdscript

import com.intellij.lexer.FlexAdapter

class GdLexerHighlighterAdapter : FlexAdapter(GdLexerHighlighter(null))
