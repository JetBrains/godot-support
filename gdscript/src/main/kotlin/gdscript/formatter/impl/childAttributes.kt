package gdscript.formatter.impl

import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.lang.tree.util.children
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.TokenType
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.util.PsiTreeUtil
import gdscript.formatter.block.GdASTBlock
import gdscript.formatter.block.GdBlocks
import gdscript.formatter.isWhiteSpace
import gdscript.psi.GdArgList
import gdscript.psi.GdArrayDecl
import gdscript.psi.GdCallEx
import gdscript.psi.GdDictDecl
import gdscript.psi.GdFile
import gdscript.psi.GdKeyValue
import gdscript.psi.GdParenthesizedEx
import gdscript.psi.GdStmt
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdSuite
import gdscript.psi.GdTypes

fun GdASTBlock.customIsIncomplete(): Boolean {
    if (!PsiTreeUtil.hasErrorElements(node.psi)) {
        if (node.psi is PsiFile) {
            return false
        }
        var element = node.psi.nextSibling
        while (element is PsiWhiteSpace || (element != null && element.node.elementType.isWhiteSpace())) {
            element = element.nextSibling
        }
        if (element != null) {
            return false
        }
    }
    if (node.elementType == GdTypes.STMT_OR_SUITE && node.children().count() == 0)
        return true
    val lastChild = getLastMeaningfulChild(node)
    if (lastChild != null) {
        if (lastChild.elementType == GdTypes.STMT_OR_SUITE &&
            lastChild.firstChildNode?.elementType == GdTypes.SUITE
        ) return true
        if (lastChild.elementType == GdTypes.MATCH_BLOCK) return true
        if (lastChild.elementType == GdTypes.COLON &&
            node.elementType in GdBlocks.NEW_LINE_AFTER_COLON_PARENTS
        ) return true
        if (lastChild.isIncompleteBinaryExpression()) return true
        if (lastChild.isIncompleteCall()) return true
    }

    (node.psi as? GdArgList)?.let { argList -> return argList.getClosingParen() == null }

    return node.isIncompleteCall() || isIncompleteExpressionWithBrackets(node.psi)
}

private fun isIncompleteExpressionWithBrackets(element: PsiElement): Boolean {
    if (element !is GdArrayDecl && element !is GdDictDecl && element !is GdParenthesizedEx) return false
    val firstChildType = element.firstChild?.node?.elementType ?: return false
    val lastChildType = element.lastChild?.node?.elementType ?: return false
    return firstChildType in GdBlocks.LEFT_BRACES && lastChildType !in GdBlocks.RIGHT_BRACES
}


private fun ASTNode.isIncompleteBinaryExpression(): Boolean {
    if (elementType !in GdBlocks.BINARY_EXPRESSIONS) return false
    // A complete binary expression has an odd number of meaningful children:
    // operand (operator operand)+. Missing right operand → even count.
    val count = children().count { it.elementType !in GdBlocks.EMPTY_TOKENS }
    return count % 2 == 0
}


private fun ASTNode.isIncompleteCall(): Boolean {
    if (elementType == GdTypes.CALL_EX) {
        val callExpression = psi as GdCallEx
        val argList = callExpression.argList
        if (argList == null || argList.closingParen == null) {
            return true
        }
    }
    return false
}

fun GdASTBlock.doGetChildAttributes(newChildIndex: Int): ChildAttributes {
    if (newChildIndex <= 0) {
        return ChildAttributes(getChildIndent(newChildIndex), null)
    }

    // Always pass the decision to a sane block from top level — file or definition.
    if (node.psi is GdFile || node.elementType == GdTypes.COLON) {
        return ChildAttributes.DELEGATE_TO_PREV_CHILD
    }

    val insertedAfterBlock: GdASTBlock = subBlocks[newChildIndex - 1]
    val prevNode = insertedAfterBlock.node
    val prevElement = prevNode.elementType

    // Statement lists (STMT_OR_SUITE/SUITE), statement parts (MATCH_BLOCK) and other colon-headed
    // definitions should think for themselves
    when (prevElement) {
        GdTypes.SUITE -> {
            if (dedentAfterLastStatement(prevNode.psi as GdSuite)) {
                return ChildAttributes(Indent.getNoneIndent(), null)
            }
            return ChildAttributes.DELEGATE_TO_PREV_CHILD
        }
        GdTypes.STMT_OR_SUITE -> {
            if (dedentAfterLastStatement(prevNode.psi as GdStmtOrSuite)) {
                return ChildAttributes(Indent.getNoneIndent(), null)
            }
            return ChildAttributes.DELEGATE_TO_PREV_CHILD
        }
        in GdBlocks.NEW_LINE_AFTER_COLON_PARENTS -> {
            return ChildAttributes.DELEGATE_TO_PREV_CHILD
        }
    }

    var statementListsBelow = 0
    var lastChild: ASTNode? = insertedAfterBlock.node
    while (lastChild != null) {
        val lastType = lastChild.elementType
        if (lastType == GdTypes.STMT_OR_SUITE && hasLineBreaksBeforeInSameParent(lastChild, 1)) {
            if (dedentAfterLastStatement(lastChild.psi as GdStmtOrSuite)) {
                break
            }
            statementListsBelow++
        } else if (statementListsBelow > 0 && lastChild.psi is PsiErrorElement) {
            statementListsBelow++
        }
        if (node.elementType == GdTypes.STMT_OR_SUITE && lastChild.psi is PsiErrorElement) {
            return ChildAttributes.DELEGATE_TO_PREV_CHILD
        }
        lastChild = getLastNonSpaceChild(lastChild, true)
    }

    if (statementListsBelow > 0) {
        val indent = ctx.commonSettings.indentOptions?.INDENT_SIZE ?: 4
        return ChildAttributes(Indent.getSpaceIndent(indent * statementListsBelow), null)
    }

    return ChildAttributes(getChildIndent(newChildIndex), null)
}

private fun dedentAfterLastStatement(suite: GdStmtOrSuite): Boolean {
    val statements: List<GdStmt> = suite.stmt?.let { listOf(it) } ?: suite.suiteList.flatMap { it.stmtList }
    return dedentAfterLastStatement(statements)
}

private fun dedentAfterLastStatement(suite: GdSuite): Boolean = dedentAfterLastStatement(suite.stmtList)

private fun dedentAfterLastStatement(statements: List<GdStmt>): Boolean {
    if (statements.isEmpty()) return false

    val last = statements.last()
    val lastType = last.node.elementType
    return lastType == GdTypes.RETURN || lastType == GdTypes.FLOW_ST
}


private fun GdASTBlock.getChildIndent(newChildIndex: Int): Indent {
    val parentType = node.elementType
    val afterNode = getAfterNode(newChildIndex)
    val lastChild = getLastNonSpaceChild(node, false)
    if (lastChild != null && lastChild.elementType == GdTypes.STMT_OR_SUITE && subBlocks.size >= newChildIndex) {
        if (afterNode == null)
            return Indent.getNoneIndent()

        // handle pressing Enter after colon and before first statement in existing statement list
        if (afterNode.elementType == GdTypes.STMT_OR_SUITE || afterNode.elementType == GdTypes.COLON)
            return Indent.getNormalIndent()

        // handle pressing Enter after colon when there is nothing in the statement list
        val lastFirstChild = lastChild.firstChildNode
        if (lastFirstChild != null && lastFirstChild == lastChild.lastChildNode && lastFirstChild.psi is PsiErrorElement)
            return Indent.getNormalIndent()
    }
    if (afterNode != null && afterNode.elementType == GdTypes.COLON &&
        parentType in GdBlocks.NEW_LINE_AFTER_COLON_PARENTS
    ) {
        return Indent.getNormalIndent()
    }
    if (parentType == GdTypes.MATCH_ST && afterNode != null && afterNode.elementType == GdTypes.COLON)
        return Indent.getNormalIndent()

    if (afterNode != null && afterNode.elementType == GdTypes.KEY_VALUE) {
        val keyValue = afterNode.psi as GdKeyValue?
        if (keyValue != null && keyValue.value == null)
            return Indent.getContinuationIndent()
    }

    // constructs that imply dedent for their children
    val gdSettings = ctx.gdSettings
    if ((parentType == GdTypes.PARAM_LIST && gdSettings.USE_CONTINUATION_INDENT_FOR_PARAMETERS) ||
        (parentType == GdTypes.ARG_LIST && gdSettings.USE_CONTINUATION_INDENT_FOR_ARGUMENTS)
    )
        return Indent.getContinuationIndent()

    if (parentType in GdBlocks.BRACKETED_INDENT_PARENTS || node.elementType in GdBlocks.STATEMENT_LIST_PARTS) {
        return if (gdSettings.USE_CONTINUATION_INDENT_FOR_COLLECTIONS) Indent.getContinuationIndent() else Indent.getNormalIndent()
    }

    if (parentType == GdTypes.ENUM_DECL_TL) {
        return Indent.getNormalIndent()
    }

    if (parentType in GdBlocks.BINARY_EXPRESSIONS) {
        return Indent.getContinuationIndent()
    }

    if (afterNode != null) {
        var wsAfter = afterNode.treeNext
        while (wsAfter != null && wsAfter.elementType.isWhiteSpace()) {
            if (wsAfter.text.indexOf('\\') >= 0) {
                return Indent.getNormalIndent()
            }
            wsAfter = wsAfter.treeNext
        }
    }
    return Indent.getNoneIndent()
}

private fun GdASTBlock.getAfterNode(newChildIndex: Int): ASTNode? {
    if (newChildIndex == 0) {  // block text contains backslash line wrappings, child block list not built
        return null
    }
    var prevIndex = newChildIndex - 1
    while (prevIndex > 0 && subBlocks[prevIndex].node.elementType === GdTypes.COMMENT) {
        prevIndex--
    }
    return subBlocks[prevIndex].getNode()
}

private fun getLastNonSpaceChild(node: ASTNode, acceptError: Boolean): ASTNode? {
    var lastChild = node.getLastChildNode()
    while (lastChild != null &&
        (lastChild.isWhitespaceOnly() || (!acceptError && lastChild.getPsi() is PsiErrorElement))
    ) {
        lastChild = lastChild.getTreePrev()
    }
    return lastChild
}

private fun ASTNode.isWhitespaceOnly(): Boolean {
    if (psi is PsiErrorElement) return false
    if (firstChildNode == null) return textLength == 0 || elementType.isWhiteSpace()
    var c: ASTNode? = firstChildNode
    while (c != null) {
        if (!c.isWhitespaceOnly()) return false
        c = c.treeNext
    }
    return true
}

private fun getLastMeaningfulChild(node: ASTNode): ASTNode? {
    var c = node.lastChildNode
    while (c != null) {
        if (!c.elementType.isWhiteSpace() && !c.isGhostBlock()) return c
        c = c.treePrev
    }
    return null
}

private fun ASTNode.isGhostBlock(): Boolean {
    if (psi is PsiErrorElement) return true
    val t = elementType
    if (t != GdTypes.STMT_OR_SUITE && t != GdTypes.SUITE) return false
    var c = firstChildNode
    while (c != null) {
        if (!c.elementType.isWhiteSpace() && c.psi !is PsiErrorElement) return false
        c = c.treeNext
    }
    return true
}

fun hasLineBreaksBeforeInSameParent(node: ASTNode, minCount: Int): Boolean {
    if (TreeUtil.findFirstLeaf(node).isWhitespaceWithLineBreaks(minCount)) return true
    val parentStart = (node.treeParent ?: return false).startOffset
    var count = 0
    var leaf: ASTNode? = TreeUtil.prevLeaf(node)
    while (leaf != null && leaf.startOffset >= parentStart) {
        if (!leaf.elementType.isWhiteSpace()) return false
        count += leaf.text.count { it == '\n' }
        if (count >= minCount) return true
        leaf = TreeUtil.prevLeaf(leaf)
    }
    return false
}

private fun ASTNode?.isWhitespaceWithLineBreaks(minCount: Int): Boolean =
    this != null && elementType.isWhiteSpace() && text.count { it == '\n' } >= minCount
