package gdscript.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import gdscript.parser.expr.GdLiteralExParser
import gdscript.parser.recovery.GdRecovery
import gdscript.parser.roots.GdAnnotationParser
import gdscript.parser.roots.GdClassNameParser
import gdscript.parser.roots.GdEmptyLineParser
import gdscript.parser.roots.GdInheritanceParser
import gdscript.psi.GdTypes.*

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

        // Keep as last
        topLevelParsers.add(GdEmptyLineParser(builder))

        // Expressions
        GdLiteralExParser(builder)

        // Helpers
        GdRecovery(builder)
    }

}
