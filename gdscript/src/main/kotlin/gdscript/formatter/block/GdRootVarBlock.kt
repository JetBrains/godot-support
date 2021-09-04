package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock

class GdRootVarBlock : GdAbstractBlock {

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment, spacingBuilder);

    override fun isLeaf(): Boolean = true;

    override fun buildChildren(): MutableList<Block> = ArrayList();

}
