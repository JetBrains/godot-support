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

        leftLeadParsers.add(GdCallExParser(builder))
    }

    override fun parse(optional: Boolean): Boolean {
        val left = mark()
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

        var right = left.precede()
        left.done(leftType)
        var rightType: IElementType = EXPR
        while (
            leftLeadParsers.any {
                rightType = it.EXPR_TYPE
                it.parse()
            }
        ) {
            right.done(rightType)
            right = mark()
        }

        right.drop()

        return true
    }
}
