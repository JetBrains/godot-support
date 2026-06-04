package gdscript.formatter.block

import com.intellij.formatting.ASTBlock
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import gdscript.formatter.GdFmtContext

// Taken from RsASTBlock.kt
/**
 * All blocks we have are actually [ASTBlock]s.
 * But [ASTBlock] interface is more flexible than we actually need:
 *  - [getNode] is nullable
 *  - [getSubBlocks] returns [com.intellij.formatting.Block]
 * This interface restricts [ASTBlock] in these ways.
 */
interface GdASTBlock : ASTBlock {
    val parent: GdASTBlock?
    override fun getSubBlocks(): List<GdASTBlock>
    override fun getNode(): ASTNode
    val ctx: GdFmtContext
}
