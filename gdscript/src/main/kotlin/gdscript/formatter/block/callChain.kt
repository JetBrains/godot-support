package gdscript.formatter.block

import com.intellij.lang.ASTNode
import gdscript.psi.GdTypes

/**
 * Returns true when this node is the outermost CALL_EX / ATTRIBUTE_EX of a method-call /
 * attribute chain. A chain node is "outermost" when its tree parent is not itself a chain
 * node (CALL_EX or ATTRIBUTE_EX) — i.e. the enclosing context does not own this chain.
 */
internal fun ASTNode.isOutermostChainNode(): Boolean {
    if (elementType != GdTypes.CALL_EX && elementType != GdTypes.ATTRIBUTE_EX) return false
    val parent = treeParent ?: return true
    return parent.elementType != GdTypes.CALL_EX && parent.elementType != GdTypes.ATTRIBUTE_EX
}

/**
 * One segment of a flattened call chain.
 *
 *   Mock.new().filter(1).prefetch_related("a","b") flattens to
 *     Base(Mock) | Step([], .new, ()) | Step([], .filter, (1)) | Step([], .prefetch_related, ("a","b"))
 *
 *   x.a() \<NL><TAB><TAB>.b() flattens to
 *     Base(x) | Step([], .a, ()) | Step([BACKSLASH], .b, ())
 *
 * `midNodes` are the non-empty AST nodes that sit between this step's wrapping
 * ATTRIBUTE_EX's inner expression and its DOT (most commonly: a line-continuation
 * BACKSLASH). They must be re-emitted as sub-blocks of the step so they don't fall
 * outside any formatter block.
 */
internal sealed class ChainSegment {
    data class Base(val node: ASTNode) : ChainSegment()
    data class Step(
        /** The wrapping ATTRIBUTE_EX node for this step — used as the synth block's
         *  representative so spacing rules see the chain step as an ATTRIBUTE_EX and don't
         *  collapse around the leading DOT. */
        val attributeEx: ASTNode,
        val midNodes: List<ASTNode>,
        val dot: ASTNode,
        val ref: ASTNode,
        val argList: ASTNode?,
    ) : ChainSegment()
}

private data class WrapperInfo(
    val node: ASTNode,
    val isCallEx: Boolean,
    /** Non-empty AST children that come *after* the inner expression of this wrapper. */
    val tailNodes: List<ASTNode>,
)

/**
 * Flatten a CALL_EX / ATTRIBUTE_EX chain rooted at this node into a list of segments
 * ordered left-to-right (base first, then chain steps). Returns null when the chain has
 * fewer than two segments — i.e. there's nothing useful to flatten.
 */
internal fun ASTNode.flattenCallChain(): List<ChainSegment>? {
    if (!isOutermostChainNode()) return null

    // 1. Descend through the chain, collecting each wrapper's tail (the AST children after
    //    its inner expression). We keep them in deepest-first order, which makes it easy
    //    to pair an ATTRIBUTE_EX (".method") with the CALL_EX that wraps it ("(args)").
    val wrappersDeepestFirst = ArrayDeque<WrapperInfo>()
    var current: ASTNode = this
    while (current.elementType == GdTypes.CALL_EX || current.elementType == GdTypes.ATTRIBUTE_EX) {
        val children = current.getChildren(null)
        val innerIdx = children.indexOfFirst { it.elementType !in GdBlocks.EMPTY_TOKENS }
        if (innerIdx < 0) return null
        val inner = children[innerIdx]
        val tail = children.drop(innerIdx + 1).filter { it.elementType !in GdBlocks.EMPTY_TOKENS }
        wrappersDeepestFirst.addFirst(
            WrapperInfo(node = current, isCallEx = current.elementType == GdTypes.CALL_EX, tailNodes = tail)
        )
        current = inner
    }
    // `current` is the base operand (a non-chain expression).

    // 2. Pair wrappers into steps. Each ATTRIBUTE_EX produces one step; if the next
    //    wrapper above it is a CALL_EX, its ARG_LIST is attached to that step.
    val wrappers = wrappersDeepestFirst.toList()
    val steps = mutableListOf<ChainSegment.Step>()
    var i = 0
    while (i < wrappers.size) {
        val w = wrappers[i]
        if (w.isCallEx) {
            // A standalone CALL_EX with no ATTRIBUTE_EX inside ("foo()") — not a chain.
            return null
        }
        val tail = w.tailNodes
        val dotIdx = tail.indexOfFirst { it.elementType == GdTypes.DOT }
        if (dotIdx < 0) return null
        val midNodes = tail.subList(0, dotIdx)
        val dot = tail[dotIdx]
        val ref = tail.drop(dotIdx + 1).firstOrNull { it.elementType == GdTypes.REF_ID_NM }
            ?: return null
        var argList: ASTNode? = null
        if (i + 1 < wrappers.size && wrappers[i + 1].isCallEx) {
            argList = wrappers[i + 1].tailNodes.firstOrNull { it.elementType == GdTypes.ARG_LIST }
            i++
        }
        steps.add(
            ChainSegment.Step(
                attributeEx = w.node,
                midNodes = midNodes,
                dot = dot,
                ref = ref,
                argList = argList,
            )
        )
        i++
    }

    if (steps.isEmpty()) return null
    return listOf<ChainSegment>(ChainSegment.Base(current)) + steps
}
