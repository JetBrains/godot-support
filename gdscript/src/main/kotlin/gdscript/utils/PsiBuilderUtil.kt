package gdscript.utils

import com.intellij.lang.PsiBuilder.Marker

object PsiBuilderUtil {

    fun Marker.rollOrDrop(result: Boolean): Boolean {
        if (result) this.drop()
        else this.rollbackTo()

        return result
    }

//    fun PsiBuilder.consumeAnyOfToken(force: Boolean, vararg elementTypes: IElementType): Boolean {
//        if (nextTokenIs(*elementTypes)) {
//            advanceLexer()
//            return true
//        } else if (force) {
//            val m = mark()
//            val err = "expected ${elementTypes[0]}, got $tokenText"
//            advanceLexer()
//            m.error(err)
//            return false
//        }
//
//        return false
//    }
//
//    fun PsiBuilder.markToken(markType: IElementType, steps: Int = 1): Boolean {
//        val m = mark()
//        repeat(steps) { advanceLexer() }
//        m.done(markType)
//
//        return true
//    }
//
//    fun PsiBuilder.consumeNewLine() {
//        if (nextTokenIs(GdTypes.NEW_LINE)) {
//            remapCurrentToken(TokenType.WHITE_SPACE)
//            advanceLexer()
//        }
//    }
//
//    fun PsiBuilder.ensureNextTokenIs(vararg elementTypes: IElementType): Boolean {
//        val searchFor = tokenType
//        if (elementTypes.none { it == searchFor }) {
//            val m = mark()
//            advanceLexer()
//            m.error("${elementTypes.first()} expected, got $tokenText")
//
//            return false
//        }
//
//        return true
//    }
//
//    /** CHECKERS **/
//
//    fun PsiBuilder.followingTokensAre(vararg elementTypes: IElementType): Boolean {
//        var step = 0
//
//        return elementTypes.all {
//            it == lookAhead(step++)
//        }
//    }

}
