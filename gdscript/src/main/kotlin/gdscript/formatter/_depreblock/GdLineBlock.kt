package gdscript.formatter.depreblock

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdTypes

@Deprecated("depre")
class GdLineBlock : GdAbstractBlock {

    companion object {
        val BLOCKS = arrayOf(
            GdTypes.INHERITANCE,
            GdTypes.CLASS_NAMING,
            GdTypes.ANNOTATION_TL,
            GdTypes.CONST_DECL_TL,
            GdTypes.CLASS_VAR_DECL_TL,
            GdTypes.SIGNAL_DECL_TL,
            GdTypes.ENUM_DECL_TL,
        );
    }

    constructor(
        node: ASTNode,
        wrap: Wrap,
        alignment: Alignment,
        settings: GdCodeStyleSettings,
        spacing: SpacingBuilder,
        indent: Indent = Indent.getIndent(Indent.Type.NONE, true, false),
        inner: Boolean = false,
    ) : super(node, wrap, alignment, settings, spacing, indent, inner);

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val next = indentNextBlock(newChildIndex);
        if (next != null) return next;

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
            Indent.getNoneIndent() //this.indent,
        );
    }

}
