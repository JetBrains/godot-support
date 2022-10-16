package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.*
import gdscript.utils.PsiElementUtil.precedingNewLines

class GdBlock : GdAbstractBlock {

    companion object {
        val BLOCKS = arrayOf(
            GdTypes.COLON,
            GdTypes.GET_DECL,
            GdTypes.SET_DECL,
            // statements are added via "is" check
        );
    }

    constructor(
        node: ASTNode,
        wrap: Wrap,
        alignment: Alignment,
        settings: GdCodeStyleSettings,
        spacing: SpacingBuilder,
        indent: Indent = Indent.getIndent(Indent.Type.NONE, true, false),
    ) : super(node, wrap, alignment, settings, spacing, indent);

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val next = indentNextBlock(newChildIndex);
        if (next != null) return next;

        if (newChildIndex > 0 && subBlocks[newChildIndex - 1] is GdLineBlock) {
            return ChildAttributes(
                Indent.getNoneIndent(),
                Alignment.createAlignment(),
            );
        }

        when (node.psi) {
            is GdStmt, is GdClassVarDeclTl,
            is GdMethodDeclTl, is GdFile -> {
                if (node.psi.precedingNewLines() < 3) {
                    return ChildAttributes(
                        Indent.getNormalIndent(),
                        Alignment.createAlignment(),
                    );
                }

                return ChildAttributes(
                    Indent.getNoneIndent(),
                    Alignment.createAlignment(),
                );
            }
        }

        if (node.psi.precedingNewLines() < 3) {
            return ChildAttributes(
                Indent.getNoneIndent(),
                Alignment.createAlignment(),
            );
        }

        return super.getChildAttributes(newChildIndex);
    }

}
