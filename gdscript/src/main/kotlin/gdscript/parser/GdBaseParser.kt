package gdscript.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes.*

abstract class GdBaseParser {

    val builder: PsiBuilder

    constructor(builder: PsiBuilder) {
        this.builder = builder
    }

    abstract fun parse(optional: Boolean = false): Boolean

    fun consumeToken(elementType: IElementType, force: Boolean = false): Boolean {
        if (builder.tokenType == elementType) {
            advance()
            return true
        } else if (force) {
            val m = mark()
            val err = "expected $elementType, got ${builder.tokenText}"
            advance()
            m.error(err)
            return false
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

    fun mcAnyOfForce(markElement: IElementType, vararg elementTypes: IElementType): Boolean {
        if (!mcAnyOf(markElement, *elementTypes)) {
            markError("expected [$elementTypes]")
            return false
        }

        return true
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

    fun mceIdentifier(markerType: IElementType): Boolean {
        val m = mark()

        if (!nextTokenIs(IDENTIFIER)) {
            val err = "expected IDENTIFIER, got ${builder.tokenText}"
            advance()
            m.error(err)

            return false
        }

        advance()
        m.done(markerType)

        return true
    }

    fun mceEndStmt(): Boolean {
        if (!mcAnyOf(END_STMT, SEMICON, NEW_LINE)) {
            markError("expected endStmt")
            return false
        }

        return true
    }

    fun consumeNewLine() {
        if (nextTokenIs(NEW_LINE)) {
            builder.remapCurrentToken(TokenType.WHITE_SPACE)
            advance()
        }
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

    /** SHARED PARSERS **/

    fun mcTyped() {

    }

}
