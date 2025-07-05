package config

import com.intellij.lexer.FlexAdapter

class GdConfigLexerAdapter : FlexAdapter {

    constructor() : super(GdConfigLexer(null))

}
