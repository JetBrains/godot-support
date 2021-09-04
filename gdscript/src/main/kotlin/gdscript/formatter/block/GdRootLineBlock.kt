package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType

class GdRootLineBlock : GdAbstractBlock {

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment, spacingBuilder);

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>();
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                val block: Block =
                    GdRootLineBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder,
                    )
                blocks.add(block)
            }
            child = child.treeNext
        }

        return blocks;
    }

}
