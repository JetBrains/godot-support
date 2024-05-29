package gdscript.parser.expr

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

object GdExprParser : GdBaseParser {

    val _simpleParsers = mutableListOf<GdExprBaseParser>()
    val _nestedParsers = mutableListOf<GdExprBaseParser>()

    init {
        _simpleParsers.add(GdNegateExParser)
        // plus/minus ??
        _simpleParsers.add(GdSignExParser)
        _simpleParsers.add(GdBitNotExParser)
        _simpleParsers.add(GdAwaitExParser)
        _simpleParsers.add(GdFuncDeclExParser)
        _simpleParsers.add(GdPrimaryExParser)
        _simpleParsers.add(GdLiteralExParser)

        _nestedParsers.add(GdArrayExParser)
        _nestedParsers.add(GdCastExParser)
        _nestedParsers.add(GdTernaryExParser)
        _nestedParsers.add(GdLogicOrExParser)
        _nestedParsers.add(GdLogicAndExParser)
        _nestedParsers.add(GdInExParser)
        _nestedParsers.add(GdComparisonExParser)
        _nestedParsers.add(GdBitOrExParser)
        _nestedParsers.add(GdBitXorExParser)
        _nestedParsers.add(GdBitAndExParser)
        _nestedParsers.add(GdShiftExParser)
        _nestedParsers.add(GdMinusExParser)
        _nestedParsers.add(GdPlusExParser)
        _nestedParsers.add(GdFactorExParser)
        _nestedParsers.add(GdIsExParser)
        _nestedParsers.add(GdCallExParser)
        _nestedParsers.add(GdAttributeExParser)

        var pos = 0
        _nestedParsers.forEach { it.POSITION = pos++ }
    }

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        return this.parseFrom(b, l, optional)
    }

    public fun parseFrom(b: GdPsiBuilder, l: Int, optional: Boolean, from: Int = 0): Boolean {
        var ok = _simpleParsers.any {
            b.enterSection(it.EXPR_TYPE)
            val ok = it.parse(b, l + 1) || b.pinned()

            b.exitSection(ok, true)
        }

        var nested = ok
        while (nested) {
            nested = _nestedParsers.drop(from).any {
                val root = b.latestDoneMarker
                b.enterSection(it.EXPR_TYPE)
                val nestedOk = it.parse(b, l + 1) || b.pinned()
                b.dropSection(false)
                if (nestedOk) {
                    root.precede().done(it.EXPR_TYPE)
                }

                nestedOk
            }
        }

        return ok || optional
    }

}
