package gdscript.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import gdscript.parser.roots.*

class GdRootParser : PsiParser, LightPsiParser {

    private val topLevelParsers = mutableListOf<GdBaseParser>()

    constructor() {
        prepareParsers()
    }

    override fun parse(root: IElementType, b: PsiBuilder): ASTNode {
        parseLight(root, b)
        // b.setDebugMode(true)
        return b.treeBuilt
    }

    override fun parseLight(root: IElementType, b: PsiBuilder) {
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

    private fun prepareParsers() {
        // Roots
        topLevelParsers.add(GdClassNameParser)
        topLevelParsers.add(GdInheritanceParser)
        topLevelParsers.add(GdAnnotationParser)
        topLevelParsers.add(GdClassConstParser)
        topLevelParsers.add(GdClassVarParser)
        topLevelParsers.add(GdSignalParser)
        topLevelParsers.add(GdEnumParser)
        topLevelParsers.add(GdMethodParser)
        // sub-class
        // Keep as last
        topLevelParsers.add(GdEmptyLineParser)
    }

}
