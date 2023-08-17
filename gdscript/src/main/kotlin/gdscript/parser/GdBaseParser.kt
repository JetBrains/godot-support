package gdscript.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

abstract class GdBaseParser {

    val builder: PsiBuilder

    constructor(builder: PsiBuilder) {
        this.builder = builder
    }

    fun consumeToken(elementType: IElementType): Boolean {
        if (builder.tokenType == elementType) {
            builder.advanceLexer()
            return true
        }

        return false
    }

    fun nextTokenIs(elementType: IElementType): Boolean {
        return builder.tokenType == elementType
    }

    fun mark(): Marker {
        return builder.mark()
    }

    fun markAndConsumeIdentifier(markerType: IElementType): Boolean {
        if (!nextTokenIs(IDENTIFIER)) return false
        val m = mark()
        builder.advanceLexer()
        m.done(markerType)

        return true
    }

    fun endSection(m: Marker, elementType: IElementType) {

    }

}
