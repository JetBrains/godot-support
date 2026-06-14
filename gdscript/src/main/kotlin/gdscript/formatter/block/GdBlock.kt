package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.lang.tree.util.children
import com.intellij.openapi.util.TextRange
import com.intellij.psi.formatter.FormatterUtil
import gdscript.formatter.GdFmtContext
import gdscript.formatter.impl.computeChildIndent
import gdscript.formatter.impl.computeSpacing
import gdscript.formatter.impl.customIsIncomplete
import gdscript.formatter.impl.doGetChildAttributes
import gdscript.formatter.impl.getWrappingStrategy
import gdscript.psi.GdTypes

class GdBlock(
    override val parent: GdASTBlock?,
    private val node: ASTNode,
    private val wrap: Wrap?,
    private val alignment: Alignment?,
    private val indent: Indent,
    override val ctx: GdFmtContext,
) : GdASTBlock {

    override fun getNode(): ASTNode = node
    override fun getTextRange(): TextRange = myTextRange

    // Mirrors how Python's parser does not overconsume whitespaces by trimming the text range
    private val myTextRange: TextRange by lazy {
        if (parent == null) node.textRange else node.trimmedTextRange()
    }
    override fun getAlignment(): Alignment? = alignment
    override fun getIndent(): Indent = indent
    override fun getWrap(): Wrap? = wrap

    override fun getSubBlocks(): List<GdASTBlock> = mySubBlocks
    private val mySubBlocks: List<GdASTBlock> by lazy { buildChildren() }

    override fun isLeaf(): Boolean = node.firstChildNode == null

    override fun isIncomplete(): Boolean = myIsIncomplete
    private val myIsIncomplete: Boolean by lazy { customIsIncomplete() || FormatterUtil.isIncomplete(node) }

    private fun buildChildren(): List<GdASTBlock> {
        val wrappingStrategy = getWrappingStrategy(node, ctx)

        // Flatten the outermost CALL_EX / ATTRIBUTE_EX chain to synthetic blocks, anchored to correct indentation
        if (node.isOutermostChainNode()) {
            node.flattenCallChain()?.let { segments ->
                return buildChainChildren(segments, wrappingStrategy)
            }
        }

        return getSubBlockNodes()
            .flatMap { it.skipIrrelevantTokens() }
            .map { child ->
                GdBlock(
                    parent = this,
                    node = child,
                    wrap = wrappingStrategy(child),
                    alignment = null,
                    indent = computeChildIndent(child, ctx),
                    ctx = ctx,
                )
            }.toList()
    }

    private fun buildChainChildren(
        segments: List<ChainSegment>,
        wrappingStrategy: (ASTNode) -> Wrap?,
    ): List<GdASTBlock> {
        val result = mutableListOf<GdASTBlock>()
        for (segment in segments) {
            when (segment) {
                is ChainSegment.Base -> {
                    result.add(
                        GdBlock(
                            parent = this,
                            node = segment.node,
                            wrap = wrappingStrategy(segment.node),
                            alignment = null,
                            indent = Indent.getNoneIndent(),
                            ctx = ctx,
                        )
                    )
                }
                is ChainSegment.Step -> {
                    for (mid in segment.midNodes) {
                        result.add(blockFor(mid, wrappingStrategy, Indent.getNoneIndent()))
                    }
                    val stepChildren = mutableListOf<GdASTBlock>()
                    stepChildren.add(blockFor(segment.dot, wrappingStrategy, Indent.getNoneIndent()))
                    stepChildren.add(blockFor(segment.ref, wrappingStrategy, Indent.getNoneIndent()))
                    if (segment.argList != null) {
                        stepChildren.add(blockFor(segment.argList, wrappingStrategy, computeChildIndent(segment.argList, ctx)))
                    }

                    result.add(
                        GdSyntheticBlock(
                            parent = this,
                            representativeNode = segment.attributeEx,
                            subBlocks = stepChildren,
                            indent = Indent.getContinuationIndent(),
                            ctx = ctx,
                        )
                    )
                }
            }
        }
        return result
    }

    private fun blockFor(child: ASTNode, wrappingStrategy: (ASTNode) -> Wrap?, indent: Indent): GdBlock =
        GdBlock(
            parent = this,
            node = child,
            wrap = wrappingStrategy(child),
            alignment = null,
            indent = indent,
            ctx = ctx,
        )

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = doGetChildAttributes(newChildIndex)

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = computeSpacing(child1, child2, ctx)

    /**
     * Children of this block, with one exception: when this block *is* a binary expression,
     * the entire nested binary-expression subtree is flattened into a single sequence of
     * operators and operands. This way the topmost binary-expression block owns all its
     * leaves directly, and there are no intermediate binary-expression blocks below it.
     *
     * Mirrors PyBlock#getSubBlockNodes.
     */
    private fun getSubBlockNodes(): Sequence<ASTNode> =
        if (node.elementType in GdBlocks.BINARY_EXPRESSIONS) node.flattenBinaryOperandsAndOperators()
        else node.children()

}

/** Recursively yields operands and operators from a nested binary-expression subtree. */
private fun ASTNode.flattenBinaryOperandsAndOperators(): Sequence<ASTNode> =
    if (elementType in GdBlocks.BINARY_EXPRESSIONS) children().flatMap { it.flattenBinaryOperandsAndOperators() }
    else sequenceOf(this)

internal fun ASTNode.skipIrrelevantTokens(): List<ASTNode> = when {
    textRange.isEmpty -> emptyList()
    (elementType == GdTypes.STMT_OR_SUITE || elementType == GdTypes.SUITE) && chars.isBlank() -> emptyList()
    elementType in GdBlocks.EMPTY_TOKENS -> emptyList()
    elementType in GdBlocks.SKIP_TOKENS -> children().toList().flatMap { it.skipIrrelevantTokens() }
    else -> listOf(this)
}

/** This node's range with whitespace-ish (and zero-width) leaves trimmed off both ends. */
fun ASTNode.trimmedTextRange(): TextRange {
    val start = firstMeaningfulLeafOffset() ?: return textRange
    val end = lastMeaningfulLeafEndOffset() ?: return textRange
    return TextRange(start, end)
}

private fun ASTNode.isMeaningfulLeafCandidate(): Boolean =
    elementType !in GdBlocks.WHITE_SPACE && textLength > 0

private fun ASTNode.firstMeaningfulLeafOffset(): Int? {
    if (firstChildNode == null) return if (isMeaningfulLeafCandidate()) startOffset else null
    var c: ASTNode? = firstChildNode
    while (c != null) {
        c.firstMeaningfulLeafOffset()?.let { return it }
        c = c.treeNext
    }
    return null
}

private fun ASTNode.lastMeaningfulLeafEndOffset(): Int? {
    if (firstChildNode == null) return if (isMeaningfulLeafCandidate()) textRange.endOffset else null
    var c: ASTNode? = lastChildNode
    while (c != null) {
        c.lastMeaningfulLeafEndOffset()?.let { return it }
        c = c.treePrev
    }
    return null
}
