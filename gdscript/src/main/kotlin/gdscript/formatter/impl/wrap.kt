package gdscript.formatter.impl

import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.DO_NOT_WRAP
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import gdscript.formatter.GdFmtContext
import gdscript.psi.GdArrEx
import gdscript.psi.GdTypes

/**
 * Returns the wrap to use for a given child of some parent block.
 *
 * Created once per parent block in [gdscript.formatter.block.GdBlock.buildChildren], then invoked
 * per child. The implementation captures a single shared [Wrap] instance so that all matching
 * siblings share *object identity* — IntelliJ's formatter groups wraps by identity, which is what
 * makes "wrap on every item" and "wrap if long" behave as a single decision across the list.
 */
typealias WrappingStrategy = (ASTNode) -> Wrap?

val noWrapping: WrappingStrategy = { null }

/**
 * Picks a wrapping strategy based on the parent node type.
 *
 * Mirrors the central `when` in Kotlin's `KotlinCommonBlock.getWrappingStrategy`.
 */
fun getWrappingStrategy(node: ASTNode, ctx: GdFmtContext): WrappingStrategy {
    val commonSettings = ctx.commonSettings
    val gdSettings = ctx.gdSettings
    return when (node.elementType) {
        GdTypes.ARG_LIST -> itemTypeStrategy(commonSettings.CALL_PARAMETERS_WRAP, GdTypes.ARG_EXPR)
        GdTypes.PARAM_LIST -> itemTypeStrategy(commonSettings.METHOD_PARAMETERS_WRAP, GdTypes.PARAM)
        GdTypes.DICT_DECL -> itemTypeStrategy(gdSettings.DICT_WRAPPING, GdTypes.KEY_VALUE)
        GdTypes.ARRAY_DECL -> arrayItemStrategy(gdSettings.ARRAY_WRAPPING)
        GdTypes.ARR_EX -> arraySubscriptionStrategy()
        else -> noWrapping
    }
}

private fun arraySubscriptionStrategy(): WrappingStrategy {
    val itemWrap = Wrap.createWrap(WrapType.NORMAL, true)
    return { child ->
        val arrEx = child.treeParent.psi as? GdArrEx
        if (arrEx?.indexExpr?.node == child)
            itemWrap
        else
            null
    }
}

private fun itemTypeStrategy(
    wrapType: Int,
    itemType: IElementType,
    wrapFirstElement: Boolean = true,
): WrappingStrategy {
    if (wrapType == DO_NOT_WRAP) return noWrapping
    val itemWrap = Wrap.createWrap(wrapType, wrapFirstElement)
    return { child -> if (child.elementType === itemType) itemWrap else null }
}

/**
 * Array literals don't have a single element type for their items — anything that's an expression
 * is an item. So we wrap by exclusion: everything that isn't a bracket, comma, or comment.
 */
private val ARRAY_NON_ITEM_TOKENS: TokenSet = TokenSet.create(
    GdTypes.LSBR,
    GdTypes.RSBR,
    GdTypes.COMMA,
    GdTypes.COMMENT,
)

private fun arrayItemStrategy(wrapType: Int): WrappingStrategy {
    if (wrapType == DO_NOT_WRAP) return noWrapping
    val itemWrap = Wrap.createWrap(wrapType, false)
    return { child -> if (child.elementType !in ARRAY_NON_ITEM_TOKENS) itemWrap else null }
}
