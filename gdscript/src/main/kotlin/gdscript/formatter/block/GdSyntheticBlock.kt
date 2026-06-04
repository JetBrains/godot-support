package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import gdscript.formatter.GdFmtContext
import gdscript.formatter.impl.computeSpacing

/**
 * Synthetic formatting block wraps a subsequence of sub blocks under a single block
 * boundary. SpacingBuilder/ChildAttributes consumers see [representativeNode]'s element
 * type, which is typically the AST node that semantically owns the group (e.g. the
 * wrapping ATTRIBUTE_EX of a chain step).
 */
class GdSyntheticBlock(
    override val parent: GdASTBlock?,
    private val representativeNode: ASTNode,
    private val subBlocks: List<GdASTBlock>,
    private val alignment: Alignment? = null,
    private val indent: Indent? = null,
    private val wrap: Wrap? = null,
    override val ctx: GdFmtContext,
) : GdASTBlock {

    init {
        assert(subBlocks.isNotEmpty()) { "tried to build empty synthetic block" }
    }

    // DELIBERATE DIVERGENCE: getTextRange() spans the sub-blocks' boundaries, NOT
    // representativeNode.textRange. The representative node (e.g. the chain step's ATTRIBUTE_EX)
    // is carried only so SpacingBuilder/ChildAttributes consumers see its ELEMENT TYPE. Callers
    // must use getNode().elementType only — never getNode().textRange, which can lie about this
    // block's extent.
    private val textRange = TextRange(
        subBlocks.first().textRange.startOffset,
        subBlocks.last().textRange.endOffset)

    override fun getTextRange(): TextRange = textRange

    override fun getNode(): ASTNode = representativeNode

    override fun getAlignment(): Alignment? = alignment
    override fun getIndent(): Indent? = indent
    override fun getWrap(): Wrap? = wrap

    override fun getSubBlocks(): List<GdASTBlock> = subBlocks

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(indent, null)
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = computeSpacing(child1, child2, ctx)

    override fun isLeaf(): Boolean = false
    override fun isIncomplete(): Boolean = subBlocks.last().isIncomplete

    override fun toString(): String {
        val text = findFirstNonSyntheticChild()?.psi?.containingFile?.text?.let { textRange.subSequence(it) }
            ?: "<gd synthetic>"
        return "$text $textRange"
    }

    private fun findFirstNonSyntheticChild(): ASTNode? {
        return when (val child = subBlocks.firstOrNull()) {
            is GdSyntheticBlock -> child.findFirstNonSyntheticChild()
            else -> child?.node
        }
    }
}
