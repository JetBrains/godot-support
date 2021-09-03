package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import gdscript.psi.GdTypes

class GdFileBlock : AbstractBlock {

    val spacingBuilder: SpacingBuilder;

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder) :
            super(node, wrap, alignment) {
        this.spacingBuilder = spacingBuilder;
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent();

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2);

    override fun isLeaf(): Boolean = false;

    override fun buildChildren(): MutableList<Block> {
        val blocks: MutableList<Block> = ArrayList();
        var child = myNode.firstChildNode
        while (child != null) {
            when {
                child.elementType == GdTypes.INHERITANCE ||
                child.elementType == GdTypes.CLASS_NAMING -> {
                    GdSimpleLeafBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
                    )
                }
                child.elementType !== TokenType.WHITE_SPACE -> {
                    GdRootVarBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
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