package gdscript.formatter

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import gdscript.lineMarker.GdTraitLineMarkerContributor

/**
 * Folding for Trait comments
 */
class GdTraitFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val members = PsiTreeUtil.findChildrenOfType(root, PsiComment::class.java);

        return members
            .filter { it.text.startsWith(GdTraitLineMarkerContributor.PREFIX) }
            .mapNotNull { mapRegion(it) }.toTypedArray();
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return node.text;
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true;
    }

    private fun mapRegion(element: PsiComment): FoldingDescriptor? {
        var next = element.nextSibling;
        while (next != null) {
            if (next is PsiComment && next.text.startsWith(GdTraitLineMarkerContributor.SUFFIX)) break;
            next = next.nextSibling;
        }

        if (next == null) return null;

        return FoldingDescriptor(element.node, TextRange(element.startOffset, next.endOffset - 1));
    }

}
