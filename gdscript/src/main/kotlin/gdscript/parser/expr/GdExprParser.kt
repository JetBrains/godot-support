package gdscript.parser.expr

import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.psi.GdTypes.EXPR

class GdExprParser : GdBaseParser {

    companion object {
        lateinit var INSTANCE: GdExprParser
    }

    val parsers = mutableListOf<GdExprBaseParser>()
    val leftLeadParsers = mutableListOf<GdExprBaseParser>()

    constructor(builder: PsiBuilder) : super(builder) {
        INSTANCE = this
        parsers.add(GdLiteralExParser(builder))
        parsers.add(GdPrimaryExParser(builder))

        leftLeadParsers.add(GdArrayExParser(builder))
        leftLeadParsers.add(GdCastExParser(builder))
        // ternary
        // logic
        // in
        // compari
        // bit_and
        // shift
        // plus
        // factor
        // is
        // attribute
        leftLeadParsers.add(GdCallExParser(builder))
    }

    override fun parse(optional: Boolean): Boolean {
        var left = mark()
        var leftType: IElementType = EXPR

        if (
            !parsers.any {
                leftType = it.EXPR_TYPE
                it.parse()
            }
        ) {
            left.drop()
            return optional
        }

        left.done(leftType)
        left = left.precede()
        var rightType: IElementType = EXPR
        while (
            leftLeadParsers.any {
                rightType = it.EXPR_TYPE
                it.parse()
            }
        ) {
            left.done(rightType)
            left = left.precede()
        }

        left.drop()

        return true
    }
}
