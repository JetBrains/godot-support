package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdStmt
import gdscript.psi.GdTypes
import gdscript.utils.PsiElementUtil.precedingNewLines

abstract class GdAbstractBlock : AbstractBlock {

    companion object {
        val EMPTY_TOKENS = arrayOf(
            TokenType.WHITE_SPACE,
            GdTypes.NEW_LINE,
//            GdTypes.NEW_LINE_END,
            GdTypes.INDENT,
            GdTypes.DEDENT,
        )

        val SKIP_TOKENS = arrayOf(
            GdTypes.SETGET_DECL,
            GdTypes.SUITE,
            GdTypes.STMT_OR_SUITE,
            GdTypes.FUNC_DECL_EX,
        )

        val NONE_INDENT: Indent = Indent.getIndent(Indent.Type.NONE, true, false);
    }

    val settings: GdCodeStyleSettings;
    val spacing: SpacingBuilder;
    val myIndent: Indent;

    constructor(
        node: ASTNode,
        wrap: Wrap,
        alignment: Alignment,
        settings: GdCodeStyleSettings,
        spacing: SpacingBuilder,
        indent: Indent = NONE_INDENT,
    ) : super(node, wrap, alignment) {
        this.settings = settings;
        this.spacing = spacing;
        this.myIndent = indent;
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>();
        val children: MutableList<ASTNode> = node.getChildren(null).toMutableList();

        while (!children.isEmpty()) {
            val child = children.removeFirstOrNull()!!;
            val t = child.elementType;
            val nextBlock =
                if (EMPTY_TOKENS.contains(t)) {
                    null;
                } else if (SKIP_TOKENS.contains(t)) {
                    children.addAll(child.getChildren(null));
                    null;
                } else if (GdBlock.BLOCKS.contains(t) || child.psi is GdStmt) {
                    GdBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        settings,
                        spacing,
                        Indent.getNormalIndent(),
                    );
                } else if (GdLineBlock.BLOCKS.contains(t)) {
                    GdLineBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        settings,
                        spacing,
                        Indent.getNoneIndent(),
                    );
                } else {
                    this.self(child)
                };

            if (nextBlock != null) {
                blocks.add(nextBlock);
            }
        }

        return blocks;
    }

    protected open fun self(child: ASTNode, indent: Indent = NONE_INDENT): GdAbstractBlock {
        return GdBlock(
            child,
            Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(),
            settings,
            spacing,
            indent,
        );
    }

    protected fun indentNextBlock(newChildIndex: Int): ChildAttributes? {
        var children = subBlocks;
        var index = newChildIndex;

        // If at root level, check what is prev sibling (for var get/set f.e.)
        if (node.elementType is IFileElementType) {
            if (newChildIndex <= 0) return null;
            val pos = subBlocks[newChildIndex - 1] as GdAbstractBlock;
            index = pos.subBlocks.size;
            children = pos.subBlocks;
        }

        var prev: GdBlock? = null;
        while (index > 0) {
            val current = children[index - 1];
            if (current is GdBlock && current.node.elementType != TokenType.ERROR_ELEMENT) {
                prev = current;
                break;
            }
            index -= 1;
        }
        if (prev == null && this is GdBlock) {
            prev = this;
        }
        if (prev == null) return null;

        when (prev.node.elementType) {
            GdTypes.COLON -> {
                if (prev.node.psi.precedingNewLines() < 3) {
                    return ChildAttributes(
                        Indent.getNormalIndent(),
                        Alignment.createAlignment(),
                    );
                }

                return ChildAttributes(
                    Indent.getNoneIndent(),
                    Alignment.createAlignment(),
                );
            }
            GdTypes.GET_DECL, GdTypes.SET_DECL -> {
                val definitions = children.filter {
                    block -> block is AbstractBlock
                        && arrayOf(GdTypes.GET_DECL, GdTypes.SET_DECL).contains(block.node.elementType)
                };

                if (definitions.size < 2 && prev.node.psi.precedingNewLines() < 2) {
                    return ChildAttributes(
                        Indent.getNormalIndent(),
                        Alignment.createAlignment(),
                    );
                }
            }
        }

        return null;
    }

    override fun getIndent(): Indent = myIndent;

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = this.spacing.getSpacing(this, child1, child2);

    override fun isLeaf(): Boolean = myNode.firstChildNode == null;

}
