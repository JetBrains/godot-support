package tscn

import com.intellij.lexer.FlexAdapter

class TscnLexerAdapter : FlexAdapter {

    constructor() : super(TscnLexerV2(null));

}