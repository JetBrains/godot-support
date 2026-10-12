package gdscript.formatter.impl

import com.intellij.formatting.FormattingMode
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.GdFmtContext
import gdscript.formatter.block.GdASTBlock
import gdscript.formatter.block.GdBlock
import gdscript.formatter.block.GdBlocks
import gdscript.formatter.isWhiteSpace
import gdscript.psi.GdArrEx
import gdscript.psi.GdKeyValue
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdStmt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdTypes

fun GdASTBlock.computeChildIndent(child: ASTNode, childCtx: GdFmtContext): Indent {
    lambdaTailIndent(child, childCtx)?.let { return it }
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
            childType in GdBlocks.RIGHT_BRACES &&
                childCtx.formattingMode != FormattingMode.REFORMAT &&
                child.prevNonSpaceSibling()?.elementType == GdTypes.COMMA ->
                if (useContinuationIndent(parentType, gdSettings)) Indent.getContinuationIndent()
                else Indent.getNormalIndent()
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

/**
 * Keeps a list tail at or left of the lambda body, independent of continuation and hanging bracket settings.
 * Assumes valid input and a tail bracket that is valid from column zero to the lambda-body column.
 * A tail comma is valid from column one to that column.
 * A column-zero comma requires a body that does not end in a nested suite.
 * A deeper column is invalid.
 *
 * For example, this comma starts between the call column and the body column:
 * ```gdscript
 * f(func():
 *     work()
 *   , 2)
 * ```
 * The rule moves the comma to the body column:
 * ```gdscript
 * f(func():
 *     work()
 *     , 2)
 * ```
 *
 * The comma column assumes that [computeChildIndent] gives a list item that starts its own line
 * a normal indent, or a continuation indent when [useContinuationIndent] is on.
 * Update this rule when the item indent rule changes, for example when list items get alignment.
 * The `testOwnLineLambda*` and `testItemLineBeforeLambdaLine*` tests fail when the two rules disagree.
 */
private fun GdASTBlock.lambdaTailIndent(child: ASTNode, childCtx: GdFmtContext): Indent? {
    // Apply the rule only during reformatting. Keep editing indents and other expression kinds unchanged.
    if (childCtx.formattingMode != FormattingMode.REFORMAT) return null
    if (node.elementType != GdTypes.ARG_LIST && node.elementType != GdTypes.ARRAY_DECL && node.elementType != GdTypes.DICT_DECL) {
        return null
    }
    val isComma = child.elementType == GdTypes.COMMA
    if (!isComma && child.elementType !in GdBlocks.RIGHT_BRACES) return null
    val block = this as? GdBlock ?: return null
    val indentOptions = childCtx.commonSettings.indentOptions ?: return null
    val lambdaLines = block.lambdaTailLines(indentOptions.TAB_SIZE)
    // A tail token must start its code line. Inline commas and brackets keep their usual indent.
    if (!lambdaLines.startsLine(child.startOffset)) return null

    // Skip blank and comment lines to find the code that the tail follows.
    var previous = PsiTreeUtil.prevLeaf(child.psi, true)
    while (previous != null && (previous.textLength == 0 || previous.node.elementType.isWhiteSpace()
                               || previous.node.elementType == GdTypes.COMMENT)) {
        previous = PsiTreeUtil.prevLeaf(previous, true)
    }
    // Require a preceding lambda body in this list. Do not treat a nested list tail as this list's tail.
    val previousCode = previous ?: return null
    var ancestor: ASTNode? = previousCode.node
    var lambda: ASTNode? = null
    while (ancestor != null && ancestor != node) {
        if (ancestor.elementType == GdTypes.FUNC_DECL_EX) lambda = ancestor
        ancestor = ancestor.treeParent
    }
    if (ancestor != node) return null
    val tailLambda = lambda ?: return null
    val suite = tailLambda.findChildByType(GdTypes.STMT_OR_SUITE)?.findChildByType(GdTypes.SUITE) ?: return null
    if (!PsiTreeUtil.isAncestor(suite.psi, previousCode, false)) return null

    // Compare visual columns so tabs and spaces use the same comma-choice rule.
    val callLine = lambdaLines.callLine
    // The lambda line is the item start line. An item after an inline comma uses the call line instead.
    var item = tailLambda
    while (item.treeParent != node) item = item.treeParent
    val itemStartsLine = lambdaLines.startsLine(item.startOffset)
    val lambdaLine = if (itemStartsLine) lambdaLines.line(item.startOffset) else callLine
    // Express the lambda-line indent relative to the list block, using the formatted item indent.
    val lambdaLineIndent = if (itemStartsLine && lambdaLine.startOffset != callLine.startOffset) {
        if (useContinuationIndent(node.elementType, childCtx.gdSettings)) indentOptions.CONTINUATION_INDENT_SIZE else indentOptions.INDENT_SIZE
    }
    else 0
    val bodyIndent = lambdaLineIndent + indentOptions.INDENT_SIZE
    // Keep the source choice relative to the lambda line. A deeper comma goes one normal indent to its right.
    if (isComma) {
        val commaIndent = if (lambdaLines.column(child.startOffset) > lambdaLine.column) bodyIndent else lambdaLineIndent
        return Indent.getSpaceIndent(commaIndent)
    }

    // An ancestor block can anchor the call line. A chain step owns that line at its leading dot.
    // Without an anchor, the line starts outside this expression. Use the body column to keep the tail within its limit.
    return if (lambdaLines.callLineHasAnchor) Indent.getNoneIndent() else Indent.getSpaceIndent(bodyIndent)
}

/** Caches source line information for one list block during a formatting pass. */
internal class LambdaTailLines(private val block: GdASTBlock, private val text: CharSequence, private val tabSize: Int) {
    data class Line(val startOffset: Int, val firstCodeOffset: Int, val column: Int)

    private val sourceLines = HashMap<Int, Line>()
    val callLine: Line by lazy { line(block.node.startOffset) }
    val callLineHasAnchor: Boolean by lazy {
        val callOffset = callLine.firstCodeOffset
        text[callOffset] == '.' || generateSequence(block) { it.parent }.any { it.textRange.startOffset == callOffset }
    }

    fun startsLine(offset: Int): Boolean {
        var start = offset
        while (start > 0) {
            when (text[start - 1]) {
                ' ', '\t' -> start--
                '\n' -> return true
                else -> return false
            }
        }
        return true
    }

    fun line(offset: Int): Line = sourceLines.getOrPut(offset) {
        val start = lineStart(offset)
        Line(start, firstCodeOffset(start), column(start))
    }

    private fun lineStart(offset: Int): Int {
        var start = offset
        while (start > 0 && text[start - 1] != '\n') start--
        return start
    }

    private fun firstCodeOffset(startOffset: Int): Int {
        var start = startOffset
        while (start < text.length && (text[start] == ' ' || text[start] == '\t')) start++
        return start
    }

    fun column(offset: Int): Int {
        var column = 0
        var index = lineStart(offset)
        while (index < text.length) {
            when (text[index]) {
                ' ' -> column++
                '\t' -> column = (column / tabSize + 1) * tabSize
                else -> break
            }
            index++
        }
        return column
    }
}

private fun isContinuationIndent(indent: Indent): Boolean =
    indent == Indent.getContinuationIndent(false) ||
        indent == Indent.getContinuationIndent(true) ||
        indent == Indent.getContinuationWithoutFirstIndent(false) ||
        indent == Indent.getContinuationWithoutFirstIndent(true)

private fun isChildOfKeyValuePair(node: ASTNode): Boolean =
    node.treeParent.let { parent -> parent != null && parent.elementType == GdTypes.KEY_VALUE }

/** Mirrors PyBlock#findPrevNonSpaceNode: previous sibling, skipping whitespace-like tokens. */
private fun ASTNode.prevNonSpaceSibling(): ASTNode? {
    var prev = treePrev
    while (prev != null && prev.elementType.isWhiteSpace()) {
        prev = prev.treePrev
    }
    return prev
}

private fun isValueOfKeyValuePair(node: ASTNode): Boolean =
    isChildOfKeyValuePair(node) && node.treeParent.getPsi(GdKeyValue::class.java).value == node.getPsi()

private fun ASTNode.isAfterStatementList(): Boolean {
    val prev = psi.prevSibling
    if (prev !is GdStmt && prev !is GdMethodDeclTl) // TODO: Python's PyAstStatement has methods classes maybe etc
        return false
    val lastChild = PsiTreeUtil.getDeepestLast(prev)
    return lastChild.parent is GdStmtOrSuite || lastChild.parent is GdMethodDeclTl
}

private fun useContinuationIndent(parentType: IElementType, settings: GdCodeStyleSettings): Boolean =
    when (parentType) {
        GdTypes.PARAM_LIST -> settings.USE_CONTINUATION_INDENT_FOR_PARAMETERS
        GdTypes.ARG_LIST -> settings.USE_CONTINUATION_INDENT_FOR_ARGUMENTS
        GdTypes.ARRAY_PATTERN, GdTypes.DICT_PATTERN -> false
        // ARRAY_DECL, DICT_DECL, PARENTHESIZED_EX
        else -> settings.USE_CONTINUATION_INDENT_FOR_COLLECTIONS
    }

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