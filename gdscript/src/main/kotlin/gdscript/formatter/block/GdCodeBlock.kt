package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

class GdCodeBlock : GdAbstractBlock {

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, settings: GdCodeStyleSettings, spacing: SpacingBuilder) :
            super(node, wrap, alignment, settings, spacing);

    override fun getIndent(): Indent? {
        if (node.elementType == GdTypes.METHOD_DECL_TL) {
            return Indent.getNoneIndent();
        }

        return Indent.getNormalIndent();
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = ArrayList<Block>();
        var child = myNode.firstChildNode
        while (child != null) {
            when (child.elementType) {
                TokenType.WHITE_SPACE -> null;
                else ->
                    GdCodeBlock(
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
