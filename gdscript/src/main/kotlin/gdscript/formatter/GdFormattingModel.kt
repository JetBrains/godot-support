package gdscript.formatter

import com.intellij.formatting.Block
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.formatter.FormattingDocumentModelImpl
import com.intellij.psi.formatter.PsiBasedFormattingModel
import com.intellij.psi.impl.source.tree.Factory
import com.intellij.psi.impl.source.tree.SharedImplUtil
import com.intellij.psi.util.PsiTreeUtil
import gdscript.formatter.block.GdBlocks

/**
 * Fixes garbled formatting in non-physical [PsiFile]s without a committed
 * [com.intellij.openapi.editor.Document] (e.g. the code-style settings preview).
 *
 * [PsiBasedFormattingModel] only rewrites [TokenType.WHITE_SPACE] leaves, but GDScript's
 * indentation-sensitive lexer emits suite-opening indentation as a `NEW_LINE` + `INDENT` pair.
 * The stock model thus inserts new indentation without removing the old, leaving stale tabs and
 * blank lines that drift on every subsequent edit. Physical files are unaffected.
 *
 * Fix: if the whitespace run before the anchor contains any non-`WHITE_SPACE` GDScript whitespace
 * token, replace the entire run with a single `WHITE_SPACE` leaf; otherwise defer to the stock
 * behavior.
 */
class GdFormattingModel(file: PsiFile, rootBlock: Block) :
    PsiBasedFormattingModel(file, rootBlock, FormattingDocumentModelImpl.createOn(file)) {

    override fun replaceWithPsiInLeaf(textRange: TextRange, whiteSpace: String, leafElement: ASTNode): String? {
        val run = collectLeadingWhitespaceRun(textRange, leafElement)
        if (run.none { it.elementType != TokenType.WHITE_SPACE }) {
            // Empty, or pure WHITE_SPACE — the stock model already handles this correctly.
            return super.replaceWithPsiInLeaf(textRange, whiteSpace, leafElement)
        }

        val project = leafElement.psi?.project ?: return super.replaceWithPsiInLeaf(textRange, whiteSpace, leafElement)
        CodeStyleManager.getInstance(project).performActionWithFormatterDisabled(Runnable {
            if (whiteSpace.isEmpty()) {
                run.forEach { it.treeParent.removeChild(it) }
            } else {
                val charTable = SharedImplUtil.findCharTableByTree(leafElement)
                val manager = SharedImplUtil.getManagerByTree(leafElement)
                val replacement = Factory.createSingleLeafElement(TokenType.WHITE_SPACE, whiteSpace, charTable, manager)
                // Replace the leaf closest to the anchor; drop the rest of the run (e.g. the NEW_LINE).
                val last = run.last()
                last.treeParent.replaceChild(last, replacement)
                for (i in 0 until run.size - 1) {
                    run[i].treeParent.removeChild(run[i])
                }
            }
        })
        return whiteSpace
    }

    /** The contiguous whitespace leaves ending right before [leafElement] and not starting before [textRange]. */
    private fun collectLeadingWhitespaceRun(textRange: TextRange, leafElement: ASTNode): List<ASTNode> {
        val leafPsi = leafElement.psi ?: return emptyList()
        val run = ArrayList<ASTNode>()
        var leaf = PsiTreeUtil.prevLeaf(leafPsi)
        while (leaf != null) {
            val node = leaf.node ?: break
            if (node.elementType !in GdBlocks.WHITE_SPACE) break
            if (node.textRange.startOffset < textRange.startOffset) break
            run.add(node)
            leaf = PsiTreeUtil.prevLeaf(leaf)
        }
        run.reverse() // document order
        return run
    }
}
