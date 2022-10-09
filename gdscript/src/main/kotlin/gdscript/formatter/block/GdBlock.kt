package gdscript.formatter.block

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.util.PsiEditorUtil
import gdscript.formatter.GdCodeStyleSettings
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdFile
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdStmt
import gdscript.psi.GdTypes


class GdBlock : AbstractBlock {

    val settings: GdCodeStyleSettings;
    val spacing: SpacingBuilder;
    val myIndent: Indent;

    constructor(
        node: ASTNode,
        wrap: Wrap,
        alignment: Alignment,
        settings: GdCodeStyleSettings,
        spacing: SpacingBuilder,
        indent: Indent = Indent.getIndent(Indent.Type.NONE, true, false),
    ) : super(node, wrap, alignment) {
        this.settings = settings;
        this.spacing = spacing;
        this.myIndent = indent;
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>();
        val children: MutableList<ASTNode> = node.getChildren(null).toMutableList();

        while (!children.isEmpty()) {
            val child = children.removeFirstOrNull()!!;
            val nextBlock = when (child.elementType) {
                TokenType.WHITE_SPACE -> null
                GdTypes.SETGET_DECL -> {
                    children.addAll(child.getChildren(null));
                    null;
                }

                GdTypes.GET_DECL, GdTypes.SET_DECL, GdTypes.INDENT, GdTypes.SUITE -> GdBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    settings,
                    spacing,
                    Indent.getNormalIndent(),
                )

                else -> GdBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    settings,
                    spacing,
                    Indent.getNoneIndent(),
                )
            }

            if (nextBlock != null) {
                blocks.add(nextBlock);
            }
        }

        return blocks;
    }

    override fun getIndent(): Indent = myIndent;

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = this.spacing.getSpacing(this, child1, child2);

    override fun isLeaf(): Boolean = myNode.firstChildNode == null;

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        when (node.psi) {
            is GdStmt, is GdClassVarDeclTl,
            is GdMethodDeclTl, is GdFile,
            -> {
                val editor = PsiEditorUtil.findEditor(node.psi)!!;
                val offset = editor.caretModel.offset;

                val text = node.psi.containingFile.text
                    .substring(0, offset)
                    .replace(" ", "")
                    .replace("\t", "");

                var whitespaces = 0;
                var index = text.length - 1;
                while (index >= 0 && text.get(index) == '\n') {
                    whitespaces += 1;
                    index -= 1;
                }

                if (whitespaces < 2) {
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

        return super.getChildAttributes(newChildIndex);
    }

}
