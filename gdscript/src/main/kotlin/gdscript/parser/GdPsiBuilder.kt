package gdscript.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType

class GdPsiBuilder {

    val b: GdPsiBuilder
    val state: GdPsiState

    constructor(builder: PsiBuilder) {
        b = builder
        state = GdPsiState()
    }

    /** Getters **/

    val tokenType get() = b.tokenType

    /** Lexer **/

    fun advance() {
        b.advanceLexer()
    }

    /** Checks **/

    fun nextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = tokenType
        return elementTypes.any { it == searchFor }
    }

    /** Sections **/

    fun enterSection(elementType: IElementType): Marker {
        state.enterSection(elementType)

        return b.mark()
    }

    fun exitSection(marker: Marker, elementType: IElementType) {
        marker.done(elementType)
    }

}
