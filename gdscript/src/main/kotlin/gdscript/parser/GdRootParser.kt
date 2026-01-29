package gdscript.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.LightPsiParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import gdscript.GdScriptBundle
import gdscript.parser.roots.*

class GdRootParser : PsiParser, LightPsiParser {

    companion object {
        val topLevelParsers = mutableListOf(
            GdClassNameParser,
            GdInheritanceParser,
            GdAnnotationTlParser,
            GdClassConstParser,
            GdClassVarParser,
            GdSignalParser,
            GdEnumParser,
            GdMethodParser,
            GdClassParser,
            GdEmptyLineParser,
            GdPassParser,
            GdStringParser
        )
    }

    override fun parse(root: IElementType, b: PsiBuilder): ASTNode {
        return parseGd(root, GdPsiBuilder(b))
    }

    override fun parseLight(root: IElementType, b: PsiBuilder) {
        return parseLightGd(root, GdPsiBuilder(b))
    }

    fun parseGd(root: IElementType, b: GdPsiBuilder): ASTNode {
        parseLightGd(root, b)
        b.setDebugMode(true)
        return b.treeBuilt
    }

    fun parseLightGd(root: IElementType, b: GdPsiBuilder) {
        val document = b.mark()
        while (!b.eof) {
            val any = topLevelParsers.any { it.parse(b, 0) }
            if (!any) {
                val m = b.mark()
                val text = b.tokenText
                val type = b.tokenType
                if (!b.eof) b.advance()
                if (type == null) {
                    m.error(GdScriptBundle.message("parsing.error.unexpected.eof"))
                } else {
                    m.error(GdScriptBundle.message("parsing.error.unexpected.tokens", type.toString(), text ?: ""))
                }
            }
        }
        document.done(root)
    }

}
