package gdscript.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock

class GdBlock : AbstractBlock {

    // TODO chapter 15 formatting
    val spacingBuilder: SpacingBuilder;

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment) {
        this.spacingBuilder = spacingBuilder;
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent();
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    override fun isLeaf(): Boolean {
        return myNode.firstChildNode == null;
    }

    override fun buildChildren(): MutableList<Block> {
        TODO("Not yet implemented")
    }
}