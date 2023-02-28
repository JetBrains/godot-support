package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

class GdBlock : AbstractBlock {

    val settings: GdCodeStyleSettings;
    val myIndent: Indent;
    val spacing: SpacingBuilder;

    constructor(
        node: ASTNode,
        wrap: Wrap,
        alignment: Alignment?,
        settings: GdCodeStyleSettings,
        spacing: SpacingBuilder,
        indent: Indent,
    ) : super(node, wrap, alignment) {
        this.settings = settings;
        this.spacing = spacing;
        this.myIndent = indent;
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>();
        val children: MutableList<ASTNode> = node.getChildren(null).toMutableList();

        var indented = false;
        while (!children.isEmpty()) {
            val child = children.removeFirstOrNull()!!;
            val type = child.elementType;

            if (GdBlocks.EMPTY_TOKENS.contains(type)) {
                if (type == GdTypes.INDENT) {
                    indented = true;
                }
            } else if (GdBlocks.SKIP_TOKENS.contains(type)) {
                children.addAll(child.getChildren(null));
            } else {
                val toIndent = indented || GdBlocks.ALWAYS_INDENTED_TOKENS.contains(type);

                blocks.add(
                    GdBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        GdAlignments.getAlignment(type, settings),
                        settings,
                        spacing,
                        if (toIndent) Indent.getNormalIndent() else Indent.getNoneIndent(),
                    )
                );
            }
        }

        return blocks;
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
//        val next = indentNextBlock(newChildIndex);
//        if (next != null) return next;
//            if (node.elementType == GdTypes.ASSIGN_TYPED || node.elementType == GdTypes.ASSIGN || node.elementType == GdTypes.EQ) GdAbstractBlock.EQ_ALIGN else Alignment.createAlignment(),

        if (
            GdBlocks.INDENT_CHILDREN_ATTRIBUTE.contains(node.elementType)
            || this.node.treeParent?.elementType == GdTypes.SUITE
        ) {
            // TODO double line space


            return ChildAttributes(
                Indent.getNormalIndent(),
                null,
            );
        }

        return ChildAttributes(
            Indent.getNoneIndent(),
            null,
        );
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return this.spacing.getSpacing(this, child1, child2);
    }

    override fun getIndent(): Indent {
        return myIndent;
    };

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null;
    }

}
