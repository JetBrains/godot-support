package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

class GdLineBlock : GdAbstractBlock {

    companion object {
        val BLOCKS = arrayOf(
            GdTypes.INHERITANCE,
            GdTypes.CLASS_NAMING,
            GdTypes.ANNOTATION_TL,
            GdTypes.CONST_DECL_TL,
            GdTypes.CLASS_VAR_DECL_TL,
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

    // TODO když už je i get i set... nebo jen jedno + mezera, tak NoneIndent
    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val next = indentNextBlock(newChildIndex);
        if (next != null) return next;
//        if (subBlocks[newChildIndex - 1] is GdBlock) { // f.e. var losos: get/set
//            return ChildAttributes(
//                Indent.getNormalIndent(),
//                Alignment.createAlignment(),
//            );
//        }

        return ChildAttributes(
            Indent.getNoneIndent(),
            Alignment.createAlignment(),
        );
    }

    override fun self(child: ASTNode, indent: Indent): GdAbstractBlock {
        return GdLineBlock(
            child,
            Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(),
            settings,
            spacing,
            Indent.getNoneIndent(),
        );
    }

}
