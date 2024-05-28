package gdscript.parser.expr

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.psi.tree.IElementType
import gdscript.parser.GdBaseParser
import gdscript.parser.GdPsiBuilder

object GdExprParser : GdBaseParser {

    val parsers = mutableListOf<GdExprBaseParser>()
    val leftLeadParsers = mutableListOf<GdExprBaseParser>()

    init {
        leftLeadParsers.add(GdCastExParser)
        leftLeadParsers.add(GdTernaryExParser)
        leftLeadParsers.add(GdLogicExParser) // Or -> And
        parsers.add(GdNegateExParser) //  ( "!" | "not" ) logicNot | in;
        leftLeadParsers.add(GdInExParser)
        leftLeadParsers.add(GdComparisonExParser)
        leftLeadParsers.add(GdBitExParser) // Or -> Xor -> And
        leftLeadParsers.add(GdShiftExParser)
        leftLeadParsers.add(GdPlusExParser) // minus -> plus
        leftLeadParsers.add(GdFactorExParser)
        parsers.add(GdSignExParser) // ( "-" | "+" ) sign | bitNot ;
        parsers.add(GdBitNotExParser) // ~ bitNot | is
        leftLeadParsers.add(GdIsExParser) // call [ "is" ( IDENTIFIER | BUILTINTYPE ) ] ;
        leftLeadParsers.add(GdCallExParser)
        leftLeadParsers.add(GdAttributeExParser)
        leftLeadParsers.add(GdArrayExParser) // await [ "[" expression "]" ] ;
        parsers.add(GdAwaitExParser)
        parsers.add(GdFuncDeclExParser)
        parsers.add(GdPrimaryExParser) // "true" | "false" | "null" | "self" | literal | arrayDecl | dictDecl | "(" expression ")" ;
        parsers.add(GdLiteralExParser)
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

    public fun parseFrom(b: GdPsiBuilder, l: Int, optional: Boolean): Boolean {


        return true
    }

}
