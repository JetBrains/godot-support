package gdscript.parser.expr

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

object GdExprParser : GdBaseParser {

    val parsers = mutableListOf<GdExprBaseParser>()
    val leftLeadParsers = mutableListOf<GdExprBaseParser>()

    init {
        parsers.add(GdFuncDeclExParser)
        parsers.add(GdNegateExParser)
        parsers.add(GdAwaitExParser)
        parsers.add(GdSignExParser)
        parsers.add(GdBitNotExParser)
        parsers.add(GdPrimaryExParser)
        parsers.add(GdLiteralExParser)

        leftLeadParsers.add(GdArrayExParser)
        leftLeadParsers.add(GdCastExParser)
        leftLeadParsers.add(GdTernaryExParser)
        leftLeadParsers.add(GdLogicExParser)
        leftLeadParsers.add(GdInExParser)
        leftLeadParsers.add(GdComparisonExParser)
        leftLeadParsers.add(GdBitExParser)
        leftLeadParsers.add(GdShiftExParser)
        leftLeadParsers.add(GdPlusExParser)
        leftLeadParsers.add(GdFactorExParser)
        leftLeadParsers.add(GdIsExParser)
        leftLeadParsers.add(GdAttributeExParser)
        leftLeadParsers.add(GdCallExParser)
    }

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        var latestMark: Marker? = null
        if (
            !parsers.any {
                latestMark = b.enterSection(it.EXPR_TYPE)
                val ok = it.parse(b, l + 1)
                b.exitSection(ok, true)
                ok
            }
        ) {
            return optional
        }

        var type: IElementType? = null
        while (
            leftLeadParsers.any {
                type = it.EXPR_TYPE
                b.enterSection(it.EXPR_TYPE)
                val ok = it.parse(b, l + 1) || b.pinned()

                b.dropSection(ok)
            }
        ) {
            val m = latestMark!!.precede()
            m.done(type!!)
            latestMark = m
        }

        return true
    }
}
