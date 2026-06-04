package gdscript.formatter.impl

import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.formatter.GdFmtContext
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.block.GdASTBlock
import gdscript.formatter.block.GdBlocks
import gdscript.psi.GdArrEx
import gdscript.psi.GdKeyValue
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdStmt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdTypes

fun GdASTBlock.computeChildIndent(child: ASTNode, childCtx: GdFmtContext): Indent {
    val gdSettings = childCtx.gdSettings
    val parentType = node.elementType
    val childType = child.elementType
    return when {
        childType == GdTypes.STMT_OR_SUITE -> Indent.getNoneIndent()
        childType == GdTypes.SUITE -> Indent.getNormalIndent()
        parentType == GdTypes.STMT_OR_SUITE -> Indent.getNormalIndent()
        parentType == GdTypes.SUITE -> Indent.getNoneIndent()
        parentType == GdTypes.MATCH_ST && childType == GdTypes.COMMENT -> Indent.getNormalIndent()
        // Comments inside list-like parents: no brace handling needed; just pick continuation
        // vs normal based on the parent's setting.
        parentType in GdBlocks.LIST_ELEMENT_TYPES && childType == GdTypes.COMMENT ->
            if (useContinuationIndent(parentType, gdSettings)) Indent.getContinuationIndent()
            else Indent.getNormalIndent()
        // Binary expressions
        parentType in GdBlocks.BINARY_EXPRESSIONS && childType !in GdBlocks.BINARY_EXPRESSIONS -> {
            val topmostBinary = findTopmostBinaryExpressionBlock(child)
            val binaryParentBlock = topmostBinary.parent
            val binaryParentNode = binaryParentBlock?.node
            val binaryParentType = binaryParentNode?.elementType
            val parenthesized = binaryParentType == GdTypes.PARENTHESIZED_EX
            if (binaryParentType != GdTypes.RETURN)
                if (parenthesized) Indent.getContinuationIndent() else Indent.getContinuationWithoutFirstIndent()
            else
                Indent.getNormalIndent()
        }
        // Binary expressions own their indent via the BINARY_EXPRESSIONS rule above,
        // which walks up to a PARENTHESIZED_EX ancestor. Adding parent indent here would double-indent.
        parentType == GdTypes.PARENTHESIZED_EX && childType in GdBlocks.BINARY_EXPRESSIONS ->
            Indent.getNoneIndent()
        // Collection literals, parenthesized exprs, arg/param lists, and match patterns ([1,2] / {1:2}).
        parentType in GdBlocks.BRACKETED_INDENT_PARENTS -> when {
            isStickyBracket(childType, gdSettings.HANG_CLOSING_BRACKETS) -> Indent.getNoneIndent()
            useContinuationIndent(parentType, gdSettings) -> Indent.getContinuationIndent()
            else -> Indent.getNormalIndent()
        }
        // TODO: There is a clear bug with indentation of type arguments e.g. `Array[int]` due to the fact of how the PSI is parsed
        //       In essence the `Array`, `[`, `int`, `]` are all siblings!
        //       Compare this to params/args e.g. `foo(a, b)` is `foo` and `PARAM_LIST` with children `(`, `a`, `,`, `b`, `)`
        parentType == GdTypes.TYPED_VAL && childType == GdTypes.TYPE_HINT -> Indent.getContinuationIndent()
        childType in GdBlocks.ALWAYS_INDENTED_TOKENS -> Indent.getNormalIndent()
        isValueOfKeyValuePair(child) -> if (gdSettings.USE_CONTINUATION_INDENT_FOR_COLLECTIONS) Indent.getContinuationIndent() else Indent.getNormalIndent()
        // Array subscription arr[2]
        parentType == GdTypes.ARR_EX ->
            if (child == (node.psi as GdArrEx).indexExpr?.node) Indent.getContinuationIndent() else Indent.getNoneIndent()

        child.isAfterStatementList() && !hasLineBreaksBeforeInSameParent(
            child,
            2
        ) && child.elementType != GdTypes.COMMENT -> Indent.getNormalIndent()

        childType in GdBlocks.CLASS_CHILDREN_INDENT && parentType == GdTypes.CLASS_DECL_TL -> Indent.getNormalIndent()
        else -> Indent.getNoneIndent()
    }
        // Fix backslash indents
        .let {
            var childIndent = it
            var prev = child.treePrev
            // The backslash itself is its own PSI token (GdTypes.BACKSLASH, registered as a
            // comment), so it sits between the surrounding whitespace nodes instead of being
            // embedded inside one (as in Python's PyBlock). We walk through both kinds.
            while (prev != null && (prev.elementType == TokenType.WHITE_SPACE
                    || prev.elementType in GdBlocks.WHITE_SPACE
                    || prev.elementType == GdTypes.BACKSLASH)
            ) {
                if (prev.elementType == GdTypes.BACKSLASH && !isContinuationIndent(childIndent)) {
                    childIndent = if (isIndentNext(child)) Indent.getContinuationIndent() else Indent.getNormalIndent()
                    break
                }
                prev = prev.treePrev
            }
            childIndent
        }
}

private fun isContinuationIndent(indent: Indent): Boolean =
    indent == Indent.getContinuationIndent(false) ||
        indent == Indent.getContinuationIndent(true) ||
        indent == Indent.getContinuationWithoutFirstIndent(false) ||
        indent == Indent.getContinuationWithoutFirstIndent(true)

private fun isChildOfKeyValuePair(node: ASTNode): Boolean =
    node.treeParent.let { parent -> parent != null && parent.elementType == GdTypes.KEY_VALUE }

private fun isValueOfKeyValuePair(node: ASTNode): Boolean =
    isChildOfKeyValuePair(node) && node.treeParent.getPsi(GdKeyValue::class.java).value == node.getPsi()

private fun ASTNode.isAfterStatementList(): Boolean {
    val prev = psi.prevSibling
    if (prev !is GdStmt && prev !is GdMethodDeclTl) // TODO: Python's PyAstStatement has methods classes maybe etc
        return false
    val lastChild = PsiTreeUtil.getDeepestLast(prev)
    return lastChild.parent is GdStmtOrSuite || lastChild.parent is GdMethodDeclTl
}

/**
 * For a child of a bracketed parent, picks whether the parent's setting opts in to
 * a continuation indent instead of a normal indent. Match patterns never use continuation:
 * a continuation indent looks wrong next to the surrounding case body.
 */
private fun useContinuationIndent(parentType: IElementType, settings: GdCodeStyleSettings): Boolean =
    when (parentType) {
        GdTypes.PARAM_LIST -> settings.USE_CONTINUATION_INDENT_FOR_PARAMETERS
        GdTypes.ARG_LIST -> settings.USE_CONTINUATION_INDENT_FOR_ARGUMENTS
        GdTypes.ARRAY_PATTERN, GdTypes.DICT_PATTERN -> false
        // ARRAY_DECL, DICT_DECL, PARENTHESIZED_EX
        else -> settings.USE_CONTINUATION_INDENT_FOR_COLLECTIONS
    }

/**
 * A bracket that should stick to the parent's indent: any opening bracket, plus
 * closing brackets when hang-closing is off.
 */
private fun isStickyBracket(childType: IElementType, hangClosing: Boolean): Boolean =
    childType in GdBlocks.LEFT_BRACES || (childType in GdBlocks.RIGHT_BRACES && !hangClosing)


private fun GdASTBlock.findTopmostBinaryExpressionBlock(child: ASTNode): GdASTBlock {
    assert(child.elementType !in GdBlocks.BINARY_EXPRESSIONS)
    var parentBlock: GdASTBlock? = this
    var topmostBinaryExpr: GdASTBlock = this
    while (parentBlock != null && parentBlock.node.elementType !in GdBlocks.BINARY_EXPRESSIONS) {
        topmostBinaryExpr = parentBlock
        parentBlock = parentBlock.parent
    }
    return topmostBinaryExpr
}

/**
 * Ported from `PyBlock.isIndentNext` (PyBlock.java:850-862). Returns true when [child] sits in
 * the header of a statement that introduces an indented body (i.e. ends with `:`), so a backslash
 * continuation on the next line should use a continuation indent rather than a normal one.
 *
 * We walk up the AST and stop at [GdTypes.SUITE] so children inside a body return false.
 *
 * Caveat: for `match`, [GdBlocks.NEW_LINE_AFTER_COLON_PARENTS] contains `MATCH_BLOCK` (the colon's
 * direct parent), not `MATCH_ST`. If the parser ever places the matched expression directly under
 * `MATCH_ST`, a backslash continuation in a `match` header won't be detected here.
 */
private fun isIndentNext(child: ASTNode): Boolean {
    var cur: ASTNode? = child.treeParent
    while (cur != null) {
        val type = cur.elementType
        if (type == GdTypes.SUITE) return false
        if (type in GdBlocks.NEW_LINE_AFTER_COLON_PARENTS) return true
        cur = cur.treeParent
    }
    return false
}