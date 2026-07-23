package gdscript.formatter

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import gdscript.lineMarker.GdTraitLineMarkerContributor
import gdscript.psi.GdArrayDecl
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdDictDecl
import gdscript.psi.GdSuite
import gdscript.psi.GdTypes
import gdscript.utils.GdCommentUtil

/**
 * Unified folding for GDScript. Handles:
 *  * Custom `#region` / `#endregion` blocks (via the [CustomFoldingBuilder] base).
 *  * Trait comment blocks (see [GdTraitLineMarkerContributor]).
 *  * Suite (indented block) and top-level class declaration bodies.
 *  * Multi-line string literals (including triple-quoted `""" ... """`).
 *  * Multi-line bracket blocks — dictionaries `{ ... }` and arrays `[ ... ]`.
 */
class GdFoldingBuilder : CustomFoldingBuilder() {

    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ) {
        root.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun visitElement(element: PsiElement) {
                when {
                    element is GdSuite -> foldSuite(element, descriptors)
                    element is GdClassDeclTl -> foldClassDecl(element, descriptors)
                    element is GdDictDecl -> foldMultiline(element, descriptors, document)
                    element is GdArrayDecl -> foldMultiline(element, descriptors, document)
                    element is PsiComment -> foldTrait(element, descriptors)
                    element.elementType == GdTypes.STRING -> foldMultiline(element, descriptors, document)
                }
                super.visitElement(element)
            }
        })
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        return when (node.elementType) {
            GdTypes.DICT_DECL -> "{...}"
            GdTypes.ARRAY_DECL -> "[...]"
            GdTypes.STRING -> stringPlaceholder(node.text)
            GdTypes.SUITE, GdTypes.CLASS_DECL_TL -> "{ ... }"
            else -> node.text // trait comment header line
        }
    }

    override fun isRegionCollapsedByDefault(node: ASTNode): Boolean {
        // Only trait comment blocks collapse by default; everything else stays expanded.
        return node.psi is PsiComment
    }

    /**
     * Godot recognizes only `#region` / `#endregion` with no space after `#`.
     * The platform's default provider also accepts a space (e.g. `# region`), so
     * we narrow the candidate set here to match the Godot editor exactly.
     */
    override fun isCustomFoldingCandidate(node: ASTNode): Boolean {
        if (node.psi !is PsiComment) return false
        val text = node.text
        return text.startsWith("#region") || text.startsWith("#endregion")
    }

    private fun foldSuite(element: GdSuite, descriptors: MutableList<FoldingDescriptor>) {
        val ending = PsiTreeUtil.getDeepestVisibleLast(element) ?: return
        if (ending.endOffset <= element.startOffset) return
        descriptors.add(FoldingDescriptor(element.node, TextRange(element.startOffset, ending.endOffset)))
    }

    private fun foldClassDecl(element: GdClassDeclTl, descriptors: MutableList<FoldingDescriptor>) {
        val start = element.classNameNmi?.nextLeaf { it.elementType == GdTypes.COLON } ?: return
        val ending = PsiTreeUtil.getDeepestVisibleLast(element) ?: return
        if (ending.endOffset <= start.startOffset + 1) return
        descriptors.add(FoldingDescriptor(element.node, TextRange(start.startOffset + 1, ending.endOffset)))
    }

    private fun foldTrait(element: PsiComment, descriptors: MutableList<FoldingDescriptor>) {
        if (!element.text.startsWith(GdTraitLineMarkerContributor.PREFIX)) return
        val footer = GdCommentUtil.endTraitComment(element) ?: return
        descriptors.add(FoldingDescriptor(element.node, TextRange(element.startOffset, footer.endOffset)))
    }

    private fun foldMultiline(element: PsiElement, descriptors: MutableList<FoldingDescriptor>, document: Document) {
        val range = element.textRange ?: return
        if (!isMultiline(range, document)) return
        descriptors.add(FoldingDescriptor(element.node, range))
    }

    private fun isMultiline(range: TextRange, document: Document): Boolean {
        return document.getLineNumber(range.startOffset) < document.getLineNumber(range.endOffset)
    }

    private fun stringPlaceholder(text: String): String {
        val i = text.indexOfFirst { it == '"' || it == '\'' }
        if (i < 0) return "\"...\""
        val quote = text[i]
        val triple = i + 2 < text.length && text[i + 1] == quote && text[i + 2] == quote
        val delim = if (triple) "$quote$quote$quote" else "$quote"
        return "$delim...$delim"
    }
}
