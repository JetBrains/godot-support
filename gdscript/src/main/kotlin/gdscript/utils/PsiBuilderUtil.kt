package gdscript.utils

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase.ErrorState
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import gdscript.psi.GdTypes

object PsiBuilderUtil {

    fun PsiBuilder.consumeToken(elementType: IElementType, force: Boolean = false): Boolean {
        if (tokenType == elementType) {
            advanceLexer()
            return true
        } else if (force) {
            val m = mark()
            val err = "expected $elementType, got $tokenText"
            advanceLexer()
            m.error(err)
            return false
        }

        return false
    }

    fun PsiBuilder.consumeAnyOfToken(force: Boolean, vararg elementTypes: IElementType): Boolean {
        if (nextTokenIs(*elementTypes)) {
            advanceLexer()
            return true
        } else if (force) {
            val m = mark()
            val err = "expected ${elementTypes[0]}, got $tokenText"
            advanceLexer()
            m.error(err)
            return false
        }

        return false
    }

    fun PsiBuilder.mcToken(markToken: IElementType, vararg elementTypes: IElementType): Boolean {
        if (nextTokenIs(*elementTypes)) {
            return markToken(markToken)
        } else {
            val m = mark()
            advanceLexer()
            m.error("expected [$elementTypes]")
        }

        return false
    }

    fun PsiBuilder.mcAnyOf(markElement: IElementType, vararg elementTypes: IElementType): Boolean {
        if (nextTokenIs(*elementTypes)) {
            val m = mark()
            advanceLexer()
            m.done(markElement)
            return true
        }

        return false
    }

    fun PsiBuilder.mcAnyOfForce(markElement: IElementType, vararg elementTypes: IElementType): Boolean {
        if (!mcAnyOf(markElement, *elementTypes)) {
            markError("expected [$elementTypes]")
            return false
        }

        return true
    }

    fun PsiBuilder.markToken(markType: IElementType, steps: Int = 1): Boolean {
        val m = mark()
        repeat(steps) { advanceLexer() }
        m.done(markType)

        return true
    }

    fun PsiBuilder.markError(error: String) {
        val m = mark()
        advanceLexer()
        m.error(error)
    }

    fun PsiBuilder.mceIdentifier(markerType: IElementType): Boolean {
        val m = mark()

        if (!nextTokenIs(GdTypes.IDENTIFIER)) {
            val err = "expected IDENTIFIER, got $tokenText"
            advanceLexer()
            m.error(err)

            return false
        }

        advanceLexer()
        m.done(markerType)

        return true
    }

    fun PsiBuilder.mceEndStmt(force: Boolean = true): Boolean {
        if (!nextTokenIs(GdTypes.SEMICON, GdTypes.NEW_LINE)) {
            if (force) {
                markError("expected endStmt")
                return false
            }
        }

        val m = mark()
        consumeToken(GdTypes.SEMICON)
        consumeToken(GdTypes.NEW_LINE)
        m.done(GdTypes.END_STMT)

        return true
    }

    fun PsiBuilder.consumeNewLine() {
        if (nextTokenIs(GdTypes.NEW_LINE)) {
            remapCurrentToken(TokenType.WHITE_SPACE)
            advanceLexer()
        }
    }

    fun PsiBuilder.ensureNextTokenIs(vararg elementTypes: IElementType): Boolean {
        val searchFor = tokenType
        if (elementTypes.none { it == searchFor }) {
            val m = mark()
            advanceLexer()
            m.error("${elementTypes.first()} expected, got $tokenText")

            return false
        }

        return true
    }

    /** CHECKERS **/

    fun PsiBuilder.followingTokensAre(vararg elementTypes: IElementType): Boolean {
        var step = 0

        return elementTypes.all {
            it == lookAhead(step++)
        }
    }

    /** Errors **/

    fun PsiBuilder.error(result: Boolean): Boolean {
        val state = ErrorState.get(this)
        val frame = state.currentFrame
        val position = this.rawTokenIndex()

        if (frame.errorReportedAt < position && frame.lastVariantAt > -1 && frame.lastVariantAt <= position) {
//            state.getExpected()
//            this.error(message)

            return false
        }

        return true
    }

}
