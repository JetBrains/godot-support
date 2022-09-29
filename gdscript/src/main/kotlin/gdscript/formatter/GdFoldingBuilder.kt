package gdscript.formatter

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdStmtOrSuite

class GdFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        //val group = FoldingGroup.newGroup("GdScript");
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        val methods = PsiTreeUtil.findChildrenOfType(root, GdMethodDeclTl::class.java);

        methods.forEach {
            val stmt = PsiTreeUtil.getChildOfType(it, GdStmtOrSuite::class.java);

            if (stmt != null) {
                val endOffset: Int =stmt.textRange.endOffset
                    PsiTreeUtil.prevVisibleLeaf(stmt.lastChild)?.textRange?.endOffset ?: stmt.textRange.endOffset;

                descriptors.add(FoldingDescriptor(stmt.node, TextRange(stmt.textRange.startOffset, endOffset)));
            }
        }
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "{ ... }";
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false;
    }

}
