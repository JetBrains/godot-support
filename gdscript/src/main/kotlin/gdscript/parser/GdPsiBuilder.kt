package gdscript.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes

class GdPsiBuilder {

    val b: PsiBuilder
    val state: GdPsiState

    constructor(builder: PsiBuilder) {
        b = builder
        state = GdPsiState(this)
    }

    /** PsiBuilder **/

    val tokenType get() = b.tokenType
    val tokenText get() = b.tokenText
    val positionAt get() = b.rawTokenIndex()
    val eof get() = b.eof()
    val treeBuilt get() = b.treeBuilt

    /** GdPsiState **/

    val isError get() = state.isError
    var errorAt get() = state.errorAt ?: 0
        set(value) { state.errorAt = value }

    /** Lexer **/

    fun advance() = b.advanceLexer()
    fun mark(): Marker = b.mark()
    fun pinned(): Boolean = state.pinned()

    fun remapCurrentToken(type: IElementType) {
        b.remapCurrentToken(type)
    }

    fun setDebugMode(boolean: Boolean) {
        b.setDebugMode(boolean)
    }

    fun pin(result: Boolean = true): Boolean {
        return state.pin(result)
    }

    /** Checks **/

    fun nextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = tokenType
        return elementTypes.any { it == searchFor }
    }

    /** Tokens **/

    fun consumeToken(elementType: IElementType, optional: Boolean = false, pin: Boolean = false): Boolean {
        if (tokenType == elementType) {
            advance()
            pin(pin)
            return true
        } else if (!optional) {
            pin(pin)
            error(elementType.debugName, false)
            return false
        }

        return false
    }

    fun mceEndStmt(optional: Boolean = false): Boolean {
        if (!nextTokenIs(GdTypes.SEMICON, GdTypes.NEW_LINE)) {
            if (!optional) {
                consumeUnexpected("END_STMT")
                return false
            }
        }

        val m = mark()
        consumeToken(GdTypes.SEMICON, true)
        consumeToken(GdTypes.NEW_LINE, true)
        m.done(GdTypes.END_STMT)

        return true
    }

    fun mceIdentifier(markerType: IElementType): Boolean {
        return mceAnyOf(markerType, false, GdTypes.IDENTIFIER)
    }

    fun mceAnyOf(markElement: IElementType, optional: Boolean, vararg elementTypes: IElementType): Boolean {
        if (!nextTokenIs(*elementTypes)) {
            if (!optional) {
                consumeUnexpected(*elementTypes)
                return false
            }

            return true
        }

        val m = mark()
        advance()
        m.done(markElement)

        return true
    }

    fun mcToken(markToken: IElementType, vararg elementTypes: IElementType): Boolean {
        val lookFor = if (elementTypes.isEmpty()) arrayOf(markToken) else elementTypes
        if (nextTokenIs(*lookFor)) {
            val m = mark()
            advance()
            m.done(markToken)
            return true
        }

        return false
    }

    fun passToken(vararg elementTypes: IElementType): Boolean {
        if (nextTokenIs(*elementTypes)) {
            advance()
            return true
        }

        return false
    }

    /** Sections **/

    fun enterSection(elementType: IElementType) {
        state.enterSection(elementType, b.mark())
    }

    fun exitSection(result: Boolean): Boolean {
        return state.exitSection(result)
    }

    /** Errors **/

    fun error(expected: String, consume: Boolean = true) {
        if (errorAt == 0) {
            val m = b.mark()
            if (consume) {
                advance()
            }
            errorAt = positionAt
            m.error("$expected expected")
        }
    }

    fun errorPin(result: Boolean, expected: String): Boolean {
        if (!result && pinned()) {
            error(expected, false)
        }

        return pinned()
    }

    private fun consumeUnexpected(vararg elementTypes: IElementType) {
        error(elementTypes.getOrNull(0)?.debugName ?: "{}")
    }

    private fun consumeUnexpected(elementType: IElementType) {
        error(elementType.debugName)
    }

    private fun consumeUnexpected(expected: String) {
        error(expected)
    }

}
