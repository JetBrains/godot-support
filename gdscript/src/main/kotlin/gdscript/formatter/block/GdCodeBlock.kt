package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode

class GdCodeBlock : GdAbstractBlock {

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment, spacingBuilder);

    override fun buildChildren(): MutableList<Block> {
        TODO("Not yet implemented")
    }

}
