package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import gdscript.GdLanguage
import gdscript.formatter.GdCodeStyleSettings

abstract class GdAbstractBlock : AbstractBlock {

    val settings: GdCodeStyleSettings;
    val spacing: SpacingBuilder;

    constructor(node: ASTNode, wrap: Wrap, alignment: Alignment, settings: GdCodeStyleSettings, spacing: SpacingBuilder) :
            super(node, wrap, alignment) {
        this.settings = settings;
        this.spacing = spacing;
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent();

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = this.spacing.getSpacing(this, child1, child2);

    override fun isLeaf(): Boolean = myNode.firstChildNode == null;

}
