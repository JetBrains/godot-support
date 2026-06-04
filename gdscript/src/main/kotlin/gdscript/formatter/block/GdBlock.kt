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

class GdBlock(
    override val parent: GdASTBlock?,
    private val node: ASTNode,
    private val wrap: Wrap?,
    private val alignment: Alignment?,
    private val indent: Indent,
    override val ctx: GdFmtContext,
) : GdASTBlock {

    override fun getNode(): ASTNode = node
    override fun getTextRange(): TextRange = node.textRange
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

        // Flatten the outermost CALL_EX / ATTRIBUTE_EX chain into [base, step1, step2, ...].
        // Each step's [DOT, REF_ID, (ARG_LIST?)] is grouped into one synthetic block so that
        // multi-line arguments inside a chain step (e.g. .prefetch_related(\n  "a",\n  "b"\n))
        // — and the closing `)` — anchor to the chain-step column rather than the outer
        // expression column.
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
                    // Mid nodes (e.g. line-continuation BACKSLASH) sit on the *previous* line —
                    // emit them as siblings of the synth step at the outer-block level, so the
                    // synth step's first content (DOT) lands on a fresh line and its
                    // Continuation indent actually applies.
                    for (mid in segment.midNodes) {
                        result.add(blockFor(mid, wrappingStrategy, Indent.getNoneIndent()))
                    }
                    val stepChildren = mutableListOf<GdASTBlock>()
                    stepChildren.add(blockFor(segment.dot, wrappingStrategy, Indent.getNoneIndent()))
                    stepChildren.add(blockFor(segment.ref, wrappingStrategy, Indent.getNoneIndent()))
                    // The step's ARG_LIST is emitted as a normal child of the synth — its own
                    // buildChildren will produce LRBR/args/RRBR and use the existing
                    // BRACKETED_INDENT_PARENTS rule, which now anchors against the synth step
                    // (the chain-step column) instead of the outer expression column.
                    if (segment.argList != null) {
                        // Derive the ARG_LIST block-level indent from the same computeChildIndent used
                        // on the non-chain path (see buildChildren above) rather than hardcoding None,
                        // so chain and non-chain ARG_LIST layout stay identical by construction. The
                        // synth step owns the chain-step column; the ARG_LIST's own children still hang
                        // via the BRACKETED_INDENT_PARENTS rule.
                        stepChildren.add(blockFor(segment.argList, wrappingStrategy, computeChildIndent(segment.argList, ctx)))
                    }

                    // The wrapping ATTRIBUTE_EX is reported as the synth's element type so
                    // SpacingBuilder rules that key off the chain-step's logical AST type see
                    // ATTRIBUTE_EX. Leading-DOT spacing is handled by computeSpacing's
                    // leaf-unwrap fallback.
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

internal fun ASTNode.skipIrrelevantTokens(): List<ASTNode> = when (elementType) {
    in GdBlocks.EMPTY_TOKENS -> emptyList()
    in GdBlocks.SKIP_TOKENS -> children().toList().flatMap { it.skipIrrelevantTokens() }
    else -> listOf(this)
}
