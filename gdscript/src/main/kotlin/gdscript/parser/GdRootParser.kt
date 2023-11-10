package gdscript.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import gdscript.parser.common.GdArgListParser
import gdscript.parser.common.GdParamListParser
import gdscript.parser.common.GdReturnHintParser
import gdscript.parser.common.GdTypedParser
import gdscript.parser.expr.GdExprParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.roots.*
import gdscript.parser.stmt.GdStmtParser
import gdscript.psi.GdTypes.FILE

class GdRootParser : PsiParser, LightPsiParser {

    private val topLevelParsers = mutableListOf<GdBaseParser>()

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        parseLight(root, builder)
        return builder.treeBuilt
    }

    override fun parseLight(root: IElementType, builder: PsiBuilder) {
        prepareParsers(builder)

        val document = builder.mark()
        while (!builder.eof()) {
            val any = topLevelParsers.any { it.parse() }
            if (!any) {
                val m = builder.mark()
                val text = builder.tokenText
                while (!builder.eof()) builder.advanceLexer()
                m.error("Unexpected tokens, $text")
            }
        }
        document.done(FILE)
    }

    private fun prepareParsers(builder: PsiBuilder) {
        // Roots
        topLevelParsers.add(GdClassNameParser(builder))
        topLevelParsers.add(GdInheritanceParser(builder))
        topLevelParsers.add(GdAnnotationParser(builder))
        topLevelParsers.add(GdClassConstParser(builder))
        topLevelParsers.add(GdClassVarParser(builder))
        topLevelParsers.add(GdSignalParser(builder))
        topLevelParsers.add(GdEnumParser(builder))
        topLevelParsers.add(GdMethodParser(builder))
        // method
        // sub-class
        // Keep as last
        topLevelParsers.add(GdEmptyLineParser(builder))

        GdExprParser(builder)
        GdStmtParser(builder)

        // Helpers
        GdRecovery(builder)
        GdTypedParser(builder)
        GdParamListParser(builder)
        GdArgListParser(builder)
        GdReturnHintParser(builder)
    }

}
