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
        parsers.add(GdNegateExParser(builder))
        parsers.add(GdPlusExParser(builder))
        parsers.add(GdBitNotExParser(builder))
        parsers.add(GdPrimaryExParser(builder))
        parsers.add(GdLiteralExParser(builder))

        leftLeadParsers.add(GdArrayExParser(builder))
        leftLeadParsers.add(GdCastExParser(builder))
        leftLeadParsers.add(GdTernaryExParser(builder))
        leftLeadParsers.add(GdLogicExParser(builder))
        leftLeadParsers.add(GdInExParser(builder))
        leftLeadParsers.add(GdComparisonExParser(builder))
        leftLeadParsers.add(GdBitExParser(builder))
        leftLeadParsers.add(GdShiftExParser(builder))
        leftLeadParsers.add(GdPlusExParser(builder))
        leftLeadParsers.add(GdFactorExParser(builder))
        leftLeadParsers.add(GdIsExParser(builder))
        leftLeadParsers.add(GdAttributeExParser(builder))
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
