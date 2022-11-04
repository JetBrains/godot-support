package gdscript.formatter

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdSuite
import gdscript.psi.GdTypes

/**
 * Folding for:
 * Suite, ClassDecl
 */
class GdFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val members = PsiTreeUtil.findChildrenOfAnyType(root, GdSuite::class.java, GdClassDeclTl::class.java);

        return members.mapNotNull {
            when (it) {
                is GdSuite -> mapSuite(it)
                is GdClassDeclTl -> mapClassDecl(it)
                else -> null
            }
        }.toTypedArray();
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "{ ... }";
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false;
    }

    private fun mapSuite(element: GdSuite): FoldingDescriptor? {
        val ending = PsiTreeUtil.getDeepestVisibleLast(element) ?: return null;
        if (ending.endOffset <= element.startOffset) return null;

        return FoldingDescriptor(element.node, TextRange(element.startOffset, ending.endOffset));
    }

    private fun mapClassDecl(element: GdClassDeclTl): FoldingDescriptor? {
        val start = element.classNameNmi?.nextLeaf { it.elementType == GdTypes.COLON } ?: return null;
        val ending = PsiTreeUtil.getDeepestVisibleLast(element) ?: return null;
        if (ending.endOffset <= start.startOffset + 1) return null;

        return FoldingDescriptor(element.node, TextRange(start.startOffset + 1, ending.endOffset));
    }

}
