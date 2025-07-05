package project

import com.intellij.lexer.FlexAdapter

class ProjectLexerAdapter : FlexAdapter {

    constructor() : super(ProjectLexer(null));

}
