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
            advance()
            return true
        }

        return false
    }

    fun mcToken(markToken: IElementType, vararg elementTypes: IElementType): Boolean {
        if (nextTokenIs(*elementTypes)) {
            return markToken(markToken)
        } else {
            val m = mark()
            advance()
            m.error("expected [$elementTypes]")
        }

        return false
    }

    fun mark(): Marker {
        return builder.mark()
    }

    fun mcAnyOf(markElement: IElementType, vararg elementTypes: IElementType): Boolean {
        if (nextTokenIs(*elementTypes)) {
            val m = mark()
            advance()
            m.done(markElement)
            return true
        }

        return false
    }

    fun markToken(markType: IElementType, steps: Int = 1): Boolean {
        val m = mark()
        repeat(steps) { advance() }
        m.done(markType)

        return true
    }

    fun markError(error: String) {
        val m = mark()
        advance()
        m.error(error)
    }

    fun mcIdentifier(markerType: IElementType): Boolean {
        if (!nextTokenIs(IDENTIFIER)) return false
        val m = mark()
        advance()
        m.done(markerType)

        return true
    }

    fun mcEndStmt(): Boolean {
        if (!nextTokenIs(SEMICON, NEW_LINE)) return false
        val m = mark()
        consumeToken(SEMICON)
        m.done(END_STMT)

        return true
    }

    /** CHECKERS **/

    fun nextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = builder.tokenType
        return elementTypes.any { it == searchFor }
    }

    fun ensureNextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = builder.tokenType
        if (elementTypes.none { it == searchFor }) {
            val m = mark()
            advance()
            m.error("${elementTypes.first()} expected, got ${builder.tokenText}")

            return false
        }

        return true
    }

    fun followingTokensAre(vararg elementTypes: IElementType): Boolean {
        var step = 0

        return elementTypes.all {
            it == builder.lookAhead(step++)
        }
    }

    /** HELPERS **/

    fun advance() {
        return builder.advanceLexer()
    }

}
