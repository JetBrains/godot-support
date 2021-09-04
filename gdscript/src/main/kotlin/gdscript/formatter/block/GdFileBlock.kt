package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import gdscript.psi.GdTypes

class GdFileBlock : GdAbstractBlock {

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment, spacingBuilder);

    override fun buildChildren(): MutableList<Block> {
        val blocks: MutableList<Block> = ArrayList();
        var child = myNode.firstChildNode
        while (child != null) {
            when {
                child.elementType == GdTypes.INHERITANCE
                || child.elementType == GdTypes.CLASS_NAMING
                || child.elementType == GdTypes.TOOLLINE -> {
                    GdRootLineBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder,
                    )
                }




                child.elementType !== TokenType.WHITE_SPACE -> {
                    GdRootVarBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder,
                    );
                }
                else -> {
                    null
                }
            }?.let {
                blocks.add(it)
            }

            child = child.treeNext
        }

        return blocks;
    }
}