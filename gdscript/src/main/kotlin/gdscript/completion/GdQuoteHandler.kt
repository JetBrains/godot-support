package gdscript.completion

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import gdscript.psi.GdTypes

class GdQuoteHandler : SimpleTokenSetQuoteHandler {

    constructor() : super(GdTypes.STRING, GdTypes.BAD_CHARACTER)

}
