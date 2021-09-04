package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock

abstract class GdAbstractBlock : AbstractBlock {

    val spacingBuilder: SpacingBuilder;

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment) {
        this.spacingBuilder = spacingBuilder;
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent();

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = this.spacingBuilder.getSpacing(this, child1, child2);

    override fun isLeaf(): Boolean = myNode.firstChildNode == null;

}
