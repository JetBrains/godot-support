package gdscript.formatter.block

import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.psi.TokenType
import gdscript.psi.GdTypes

object GdBlocks {

    val EMPTY_TOKENS = arrayOf(
        TokenType.WHITE_SPACE,
        GdTypes.NEW_LINE,
        GdTypes.INDENT,
        GdTypes.DEDENT,
    )

    val COMMENT_TOKENS = arrayOf(
        GdTypes.COMMENT,
    )

    val SKIP_TOKENS = arrayOf(
        GdTypes.SETGET_DECL,
        GdTypes.SUITE,
        GdTypes.STMT_OR_SUITE,
        GdTypes.FUNC_DECL_EX,
    )

    val ALWAYS_INDENTED_TOKENS = arrayOf(
        GdTypes.PARAM_LIST,
        GdTypes.ENUM_VALUE,
        GdTypes.ARRAY_DECL,
        GdTypes.DICT_DECL,
        GdTypes.KEY_VALUE,
    )

    val INDENT_CHILDREN_ATTRIBUTE = arrayOf(
        GdTypes.SIGNAL_DECL_TL,
        GdTypes.ENUM_DECL_TL,
        GdTypes.ARRAY_DECL,
        GdTypes.DICT_DECL,
    )

    // TODO clear up
    val INNER_SKIP_TOKENS = arrayOf(
        GdTypes.CLASS_VAR_DECL_TL,
    )

    val NONE_INDENT: Indent = Indent.getIndent(Indent.Type.NONE, true, false);

    val EQ_ALIGN = Alignment.createAlignment(true);

}
