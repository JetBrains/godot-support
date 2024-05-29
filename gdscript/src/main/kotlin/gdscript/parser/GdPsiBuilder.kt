package gdscript.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdLiteralExParser
import gdscript.psi.GdTypes
import io.ktor.util.*

class GdPsiBuilder {

    val MAX_RECURSION_LEVEL = StringUtil.parseInt(System.getProperty("grammar.kit.gpub.max.level"), 1000)

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

    val isArgs get() = state.isArgs
    val isError get() = state.isError
    var errorAt: Int? get() = state.errorAt ?: 0
        set(value) { state.errorAt = value }

    /** Lexer **/

    fun advance() = b.advanceLexer()
    fun mark(): Marker = b.mark()
    fun pinned(): Boolean = state.pinned()
    val latestDoneMarker get() = b.latestDoneMarker as Marker

    fun rawLookup(steps: Int = 1): IElementType? {
        return b.rawLookup(steps)
    }

    fun remapCurrentToken(type: IElementType) {
        b.remapCurrentToken(type)
    }

    fun setDebugMode(boolean: Boolean) {
        b.setDebugMode(boolean)
    }

    fun pin(result: Boolean = true): Boolean {
        return state.pin(result)
    }

    fun unpin() {
        state.unpin()
    }

    /** Checks **/

    fun nextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = tokenType
        return elementTypes.any { it == searchFor }
    }

    fun followingTokensAre(vararg elementTypes: IElementType): Boolean {
        var step = 0

        return elementTypes.all {
            it == b.lookAhead(step++)
        }
    }

    /** Tokens **/

    fun consumeToken(elementType: IElementType, optional: Boolean = false, pin: Boolean = false): Boolean {
        if (tokenType == elementType) {
            advance()
            pin(pin)
            return true
        } else if (!optional) {
            error(elementType.toString(), false)
            return false
        }

        return false
    }

    fun mceEndStmt(optional: Boolean = false): Boolean {
        if (!nextTokenIs(GdTypes.SEMICON, GdTypes.NEW_LINE)) {
            if (!optional) {
                error("END_STMT", false)
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
        val ok = GdLiteralExParser.parseExtendedRefId(this, markerType)
        if (!ok) error("IDENTIFIER", false)

        return ok
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

    fun enterSection(elementType: IElementType): Marker {
        val m = b.mark()
        state.enterSection(elementType, m)
        return m
    }

    fun precedeEnterSection(elementType: IElementType): Marker {
        val m = (b.latestDoneMarker as Marker).precede()
        state.enterSection(elementType, m)
        return m
    }

    fun exitSection(result: Boolean, drop: Boolean = false): Boolean {
        return state.exitSection(result, drop)
    }

    fun exitSection(result: Boolean, elementType: IElementType): Boolean {
        state.remapElement(elementType)
        return state.exitSection(result)
    }

    fun dropSection(result: Boolean): Boolean {
        return state.dropSection(result)
    }

    /** Errors **/

    fun error(expected: String, consume: Boolean = true) {
        if (!isError) {
            val m = b.mark()
            if (consume) {
                advance()
            }
            errorAt = positionAt
            m.error("${expected.removePrefix("GdTokenType.")} expected")
        }
    }

    fun errorPin(result: Boolean, expected: String): Boolean {
        if (!result && pinned()) {
            error(expected.toUpperCasePreservingASCIIRules(), false)
        }

        return pinned()
    }

    fun clearState() {
        errorAt = null
        unpin()
    }

    private fun consumeUnexpected(vararg elementTypes: IElementType) {
        error(elementTypes.getOrNull(0)?.toString() ?: "{}")
    }

    private fun consumeUnexpected(elementType: IElementType) {
        error(elementType.toString())
    }

    private fun consumeUnexpected(expected: String) {
        error(expected)
    }


    fun recursionGuard(level: Int, funcName: String?): Boolean {
        if (level > MAX_RECURSION_LEVEL) {
            b.mark().error("Maximum recursion level ($MAX_RECURSION_LEVEL) reached $funcName")
            return false
        }

        return true
    }

}
