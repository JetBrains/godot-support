package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

class GdSimpleLeafBlock : AbstractBlock {

    val spacingBuilder: SpacingBuilder;

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment) {
        this.spacingBuilder = spacingBuilder;
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent();

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2);

    override fun isLeaf(): Boolean = myNode.firstChildNode == null;

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>();
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.elementType !== TokenType.WHITE_SPACE) {
                val block: Block =
                    GdSimpleLeafBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
                    )
                blocks.add(block)
            }
            child = child.treeNext
        }

        return blocks;
    }

}
