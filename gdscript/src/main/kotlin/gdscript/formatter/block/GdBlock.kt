package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

class GdBlock : AbstractBlock {

    companion object {
        val EMPTY_TOKENS = arrayOf(
            TokenType.WHITE_SPACE,
            GdTypes.NEW_LINE,
            GdTypes.INDENT,
            GdTypes.DEDENT,
        )

        val SKIP_TOKENS = arrayOf(
            GdTypes.SETGET_DECL,
            GdTypes.SUITE,
            GdTypes.STMT_OR_SUITE,
            GdTypes.FUNC_DECL_EX,
        )

        val ALWAYS_INDENTED_TOKENS = arrayOf(
            GdTypes.PARAM_LIST,
        )

        val INNER_SKIP_TOKENS = arrayOf(
            GdTypes.CLASS_VAR_DECL_TL,
        )

        val INDENT_CHILDREN_ATTRIBUTE = arrayOf(
            GdTypes.SIGNAL_DECL_TL,
        )

        val NONE_INDENT: Indent = Indent.getIndent(Indent.Type.NONE, true, false);

        val EQ_ALIGN = Alignment.createAlignment(true);
    }

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

            if (EMPTY_TOKENS.contains(type)) {
                if (type == GdTypes.INDENT) {
                    indented = true;
                }
            } else if (SKIP_TOKENS.contains(type)) {
                children.addAll(child.getChildren(null));
            } else {
                val toIndent = indented || ALWAYS_INDENTED_TOKENS.contains(type);

                blocks.add(
                    GdBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        null,
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

        if (INDENT_CHILDREN_ATTRIBUTE.contains(node.elementType)) {
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
