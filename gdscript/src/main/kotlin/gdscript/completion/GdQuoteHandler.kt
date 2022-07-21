package gdscript.completion

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.TokenType
import gdscript.psi.GdTypes

class GdQuoteHandler : SimpleTokenSetQuoteHandler {

    constructor() : super(GdTypes.STRING, TokenType.BAD_CHARACTER, GdTypes.BAD_CHARACTER)

}
