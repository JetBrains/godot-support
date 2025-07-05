package gdscript

import com.intellij.lexer.FlexAdapter

class GdLexerAdapter : FlexAdapter(GdLexer(null))