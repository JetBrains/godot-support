package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

class GdRootLineBlock : GdAbstractBlock {

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, settings: GdCodeStyleSettings, spacing: SpacingBuilder) :
            super(node, wrap, alignment, settings, spacing);

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        var indent = Indent.getNoneIndent();
        if (node.elementType == GdTypes.METHOD_DECL_TL) {
            indent = Indent.getNormalIndent();
        }

        return ChildAttributes(indent, null);
    }

    override fun getIndent(): Indent? {
        return when(node.treeParent?.elementType) {
            GdTypes.SUITE -> Indent.getNormalIndent();
            else -> Indent.getNoneIndent();
        }
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>();
        var child = myNode.firstChildNode
        while (child != null) {
            when (child.elementType) {
                TokenType.WHITE_SPACE -> null;
//                GdTypes.METHOD_DECL_TL ->
//                    GdCodeBlock(
//                        child,
//                        Wrap.createWrap(WrapType.NONE, false),
//                        Alignment.createAlignment(),
//                        settings,
//                        spacing,
//                    );
                else ->
                    GdRootLineBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        settings,
                        spacing,
                    );
            }?.let { blocks.add(it) }

            child = child.treeNext
        }

        return blocks;
    }

}
