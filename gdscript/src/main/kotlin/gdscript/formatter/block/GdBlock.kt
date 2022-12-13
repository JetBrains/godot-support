package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiEditorUtil
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.GdFormattingModelBuilder
import gdscript.psi.*
import gdscript.utils.PsiElementUtil.precedingNewLines

class GdBlock : GdAbstractBlock {

    companion object {
        val BLOCKS = arrayOf(
            GdTypes.COLON,
            GdTypes.GET_DECL,
            GdTypes.SET_DECL,
            GdTypes.ENUM_VALUE,
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
        inner: Boolean = false,
    ) : super(node, wrap, alignment, settings, spacing, indent, inner);

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val next = indentNextBlock(newChildIndex);
        if (next != null) return next;

        if (newChildIndex > 0 && subBlocks[newChildIndex - 1] is GdLineBlock) {
            return ChildAttributes(
                Indent.getNoneIndent(),
                if (node.elementType == GdTypes.ASSIGN_TYPED || node.elementType == GdTypes.ASSIGN || node.elementType == GdTypes.EQ) EQ_ALIGN else Alignment.createAlignment(),
            );
        }

        when (node.psi) {
            is GdStmt, is GdClassVarDeclTl,
            is GdMethodDeclTl,
            -> {
                if (node.psi.precedingNewLines() < 3) {
                    return ChildAttributes(
                        Indent.getNormalIndent(),
                        if (node.elementType == GdTypes.ASSIGN_TYPED || node.elementType == GdTypes.ASSIGN || node.elementType == GdTypes.EQ) EQ_ALIGN else Alignment.createAlignment(),
                    );
                }

                return ChildAttributes(
                    Indent.getNoneIndent(),
                    if (node.elementType == GdTypes.ASSIGN_TYPED || node.elementType == GdTypes.ASSIGN || node.elementType == GdTypes.EQ) EQ_ALIGN else Alignment.createAlignment(),
                );
            }
            is GdFile -> {
                val editor = PsiEditorUtil.findEditor(node.psi)!!;
                val caretOffset = editor.caretModel.offset;
                val local = node.psi.findElementAt(caretOffset - 1);

                var text = local?.text ?: "";
                text = text.trimEnd('\n');
                var count = maxOf(0, text.length);
                if (text.startsWith(' ')) {
                    count /= GdFormattingModelBuilder.INDENT_SIZE;
                }
                if ((local?.precedingNewLines() ?: 0) >= 3) {
                    count -= 1;
                }

                return ChildAttributes(
                    Indent.getSpaceIndent(count * GdFormattingModelBuilder.INDENT_SIZE),
                    if (node.elementType == GdTypes.ASSIGN_TYPED || node.elementType == GdTypes.ASSIGN || node.elementType == GdTypes.EQ) EQ_ALIGN else Alignment.createAlignment(),
                );
            }
        }

        if (node.psi.precedingNewLines() < 3) {
            return ChildAttributes(
                Indent.getNoneIndent(),
                if (node.elementType == GdTypes.ASSIGN_TYPED || node.elementType == GdTypes.ASSIGN || node.elementType == GdTypes.EQ) EQ_ALIGN else Alignment.createAlignment(),
            );
        }

        return super.getChildAttributes(newChildIndex);
    }

}
