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

class GdRootParser : PsiParser, LightPsiParser {

    private val topLevelParsers = mutableListOf<GdBaseParser>()

    override fun parse(root: IElementType, b: PsiBuilder): ASTNode {
        parseLight(root, b)
        // b.setDebugMode(true)
        return b.treeBuilt
    }

    override fun parseLight(root: IElementType, b: PsiBuilder) {
        prepareParsers(b)

        val document = b.mark()
        while (!b.eof()) {
            val any = topLevelParsers.any { it.parse(b) }
            if (!any) {
                val m = b.mark()
                val text = b.tokenText
                while (!b.eof()) b.advanceLexer()
                m.error("Unexpected tokens, $text")
            }
        }
        document.done(root)
    }

    private fun prepareParsers(builder: PsiBuilder) {
        // Roots
        topLevelParsers.add(GdClassNameParser())
        topLevelParsers.add(GdInheritanceParser())
        topLevelParsers.add(GdAnnotationParser())
        topLevelParsers.add(GdClassConstParser())
        topLevelParsers.add(GdClassVarParser())
        topLevelParsers.add(GdSignalParser())
        topLevelParsers.add(GdEnumParser())
        topLevelParsers.add(GdMethodParser())
        // sub-class
        // Keep as last
        topLevelParsers.add(GdEmptyLineParser())

        GdExprParser()
        GdStmtParser()

        // Helpers
        GdRecovery()
        GdTypedParser()
        GdParamListParser()
        GdArgListParser()
        GdReturnHintParser()
    }

}
