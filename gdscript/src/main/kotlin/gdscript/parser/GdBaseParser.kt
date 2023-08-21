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

    abstract fun parse(): Boolean

    fun consumeToken(elementType: IElementType): Boolean {
        if (builder.tokenType == elementType) {
            builder.advanceLexer()
            return true
        }

        return false
    }

    fun nextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = builder.tokenType
        return elementTypes.any { it == searchFor }
    }

    fun mark(): Marker {
        return builder.mark()
    }

    fun mcIdentifier(markerType: IElementType): Boolean {
        if (!nextTokenIs(IDENTIFIER)) return false
        val m = mark()
        builder.advanceLexer()
        m.done(markerType)

        return true
    }

    fun endSection(m: Marker, elementType: IElementType) {

    }

    fun mcEndStmt(): Boolean {
        if (!nextTokenIs(SEMICON, NEW_LINE)) return false
        val m = mark()
        consumeToken(SEMICON)
        m.done(END_STMT)

        return true
    }

}
