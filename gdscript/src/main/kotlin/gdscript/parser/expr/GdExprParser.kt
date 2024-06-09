package gdscript.parser.expr

import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

object GdExprParser : GdBaseParser {

    val parsers = mutableListOf<GdExprBaseParser>()

    init {
        parsers.add(GdFuncDeclExParser) // simple
        parsers.add(GdAwaitExParser) // simple
        parsers.add(GdCastExParser)
        parsers.add(GdTernaryExParser)
        parsers.add(GdLogicOrExParser)
        parsers.add(GdLogicAndExParser)
        parsers.add(GdNegateExParser) // simple
        parsers.add(GdInExParser)
        parsers.add(GdComparisonExParser)
        parsers.add(GdBitOrExParser)
        parsers.add(GdBitXorExParser)
        parsers.add(GdBitAndExParser)
        parsers.add(GdShiftExParser)
        parsers.add(GdMinusExParser)
        parsers.add(GdPlusExParser)
        parsers.add(GdFactorExParser)
        parsers.add(GdSignExParser) // simple
        parsers.add(GdBitNotExParser) // simple
        parsers.add(GdIsExParser)
        parsers.add(GdCallExParser)
        parsers.add(GdAttributeExParser)
        parsers.add(GdArrayExParser)
        parsers.add(GdPrimaryExParser) // simple
        parsers.add(GdLiteralExParser) // simple

        var pos = 0
        parsers.forEach { it.POSITION = pos++ }
    }

    override fun parse(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {
        return this.parseFrom(b, l, optional)
    }

    public fun parseFrom(b: GdPsiBuilder, l: Int, optional: Boolean, from: Int = 0): Boolean {
        var ok = parsers.any {
            if (it.isNested) return@any false

            b.enterSection(it.EXPR_TYPE)
            val ok = it.parse(b, l + 1) || b.pinned()

            b.exitSection(ok, true)
        }

        var nested = ok
        while (nested) {
            nested = parsers.drop(from).any {
                if (!it.isNested) return@any false

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
