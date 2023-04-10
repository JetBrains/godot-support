package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.impl.source.tree.FileElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.jetbrains.rd.util.forEachReversed
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdTypes
import gdscript.psi.utils.PsiGdClassVarUtil
import gdscript.utils.GdSettingsUtil.calculateSpaceIndents
import gdscript.utils.PsiElementUtil.getCaretOffsetIfSingle
import gdscript.utils.PsiElementUtil.nextNonWhiteCommentToken
import gdscript.utils.PsiElementUtil.precedingNewLines

class GdBlock : AbstractBlock {

    val settings: CodeStyleSettings
    val myIndent: Indent
    val spacing: SpacingBuilder
    val alignments: Alignments
    var nextBlock: GdBlock? = null

    constructor(
        node: ASTNode,
        wrap: Wrap,
        alignment: Alignment?,
        settings: CodeStyleSettings,
        spacing: SpacingBuilder,
        indent: Indent,
        alignments: Alignments,
    ) : super(node, wrap, alignment) {
        this.settings = settings;
        this.spacing = spacing;
        this.myIndent = indent;
        this.alignments = alignments;
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        val children: MutableList<ASTNode> = node.getChildren(null).toMutableList()

        var suited = false
        var indented = false
        var lastBlock: GdBlock? = null
        val rootPosition = this.node is FileElement

        while (!children.isEmpty()) {
            val child = children.removeFirstOrNull()!!
            val type = child.elementType
            if (suited || rootPosition) { // Roots consume new_line instead of passing it as white_space
                alignments.reset(type, if (rootPosition) 1 else 2)
            }

            // Due to elif & else being siblings and not children
            if (GdBlocks.DEDENT_TOKENS.contains(type)) indented = false

            if (GdBlocks.EMPTY_TOKENS.contains(type)) {
                if (type == GdTypes.INDENT) {
                    indented = true
                }
            } else if (GdBlocks.SKIP_TOKENS.contains(type)) {
                if (type == GdTypes.SUITE) {
                    suited = true
                    alignments.initialize()
                }

                child.getChildren(null).forEachReversed { children.add(0, it) }
            } else {
                var toIndent = indented || GdBlocks.ALWAYS_INDENTED_TOKENS.contains(type)
                // Unique case of comment before Indentation
                if (!toIndent && type == GdTypes.COMMENT && child.psi.nextNonWhiteCommentToken().elementType == GdTypes.INDENT) {
                    toIndent = true
                }

                val currentBlock = GdBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    alignments.getAlignment(type),
                    settings,
                    spacing,
                    if (toIndent) Indent.getNormalIndent() else Indent.getNoneIndent(),
                    alignments.clone(type),
                )
                if (lastBlock != null) {
                    lastBlock.nextBlock = currentBlock
                }

                blocks.add(currentBlock)
                lastBlock = currentBlock
            }
        }

        return blocks
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val index = newChildIndex - 1
        val previousBlock = if (index >= 0) this.subBlocks[index] else null
        val preceding = if (previousBlock is GdBlock) previousBlock.node.psi else null
        val atEndOfStmt = node is FileElement

        // Check is it is right after COLON
        if (previousBlock is GdBlock) {
            val lastNode = PsiTreeUtil.getDeepestVisibleLast(previousBlock.node.psi)
            if (lastNode?.elementType == GdTypes.COLON) {
                if (atEndOfStmt && preceding != null) {
                    return ChildAttributes(settings.calculateSpaceIndents(preceding, 1), null)
                }
                return ChildAttributes(Indent.getNormalIndent(), null)
            }
        }

        val caretOffset = node.psi.getCaretOffsetIfSingle()
        if (previousBlock is GdBlock && preceding != null && previousBlock.node.lastChildNode?.elementType == GdTypes.STMT_OR_SUITE) {
            if (caretOffset != null) {
                val emptyLines = PsiTreeUtil.getDeepestLast(preceding).precedingNewLines(caretOffset)
                if (emptyLines > 2) {
                    return ChildAttributes(settings.calculateSpaceIndents(preceding, 2 - emptyLines), null)
                }
            }

            if (atEndOfStmt) {
                return ChildAttributes(settings.calculateSpaceIndents(preceding), null)
            }

            return ChildAttributes(Indent.getContinuationIndent(), null)
        }

        // Inside indented blocks directly
        if (GdBlocks.INDENT_CHILDREN_ATTRIBUTE.contains(node.elementType)) {
            if (caretOffset != null) {
                val emptyLines = PsiTreeUtil.getDeepestLast(node.psi).precedingNewLines(caretOffset)
                if (emptyLines > 2) {
                    return ChildAttributes(settings.calculateSpaceIndents(node.psi, 2 - emptyLines), null)
                }
            }

            return ChildAttributes(Indent.getNormalIndent(), null)
        }


        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 == null || child1 !is GdBlock || child2 !is GdBlock) {
            return this.spacing.getSpacing(this, child1, child2);
        }

        if (child1.node.elementType == GdTypes.COMMENT) {
            return null;
        }

        var block2: GdBlock? = child2
        while (block2 != null && block2.node.elementType == GdTypes.COMMENT) {
            block2 = block2.nextBlock
        }
        if (block2 == null) return null

        // Separation of @onready & @export variables
        val split = splitByAnnotation(child1, block2)
        if (split != null) return split

        if (child1.node.elementType == GdTypes.CLASS_VAR_DECL_TL && block2.node.elementType == GdTypes.CLASS_VAR_DECL_TL && PsiGdClassVarUtil.isAnnotated(child1.node.psi as GdClassVarDeclTl)) {
            val customSettings = settings.getCustomSettings(GdCodeStyleSettings::class.java)
            return Spacing.createSpacing(0, 0, customSettings.LINES_BETWEEN_EXPORT_GROUPS + 1, false, 0)
        }

        return this.spacing.getSpacing(this, child1, block2);
    }

    override fun getIndent(): Indent {
        return myIndent;
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null;
    }

    private fun splitByAnnotation(block1: GdBlock?, block2: GdBlock?): Spacing? {
        if (block1 == null || block2 == null) return null
        if (block1.node.elementType == GdTypes.CLASS_VAR_DECL_TL && block2.node.elementType == GdTypes.ANNOTATION_TL) {
            val node1 = block1.node.psi as GdClassVarDeclTl
            var node2 = block2.node.psi
            val annotations = mutableListOf<String>()
            while (node2 is GdAnnotationTl) {
                annotations.add(node2.annotationType.text)
                node2 = node2.nextNonWhiteCommentToken()
            }

            for (annotator in GdBlocks.SEPARATE_ANNOTATOR_GROUPS) {
                if (PsiGdClassVarUtil.isAnnotatedContains(node1, annotator).xor(annotations.contains(annotator))) {
                    val customSettings = settings.getCustomSettings(GdCodeStyleSettings::class.java)
                    return Spacing.createSpacing(0, 0, customSettings.LINES_BETWEEN_EXPORT_GROUPS + 1, false, 0)
                }
            }
        }

        return null
    }

}
