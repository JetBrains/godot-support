package gdscript.formatter.block

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
        GdTypes.NEW_LINE_END,
        GdTypes.END_STMT,
    )

    val ALWAYS_INDENTED_TOKENS = arrayOf(
        GdTypes.PARAM_LIST,
        GdTypes.ARG_LIST,
        GdTypes.ENUM_VALUE,
        GdTypes.ARRAY_DECL,
        GdTypes.DICT_DECL,
        GdTypes.KEY_VALUE,
        GdTypes.GET_DECL,
        GdTypes.SET_DECL,
    )

    val DEDENT_TOKEN_PARENTS = arrayOf(
        GdTypes.IF_ST,
    )
    val DEDENT_TOKENS = arrayOf(
        GdTypes.ELIF_ST,
        GdTypes.ELSE_ST,
        GdTypes.PATTERN_LIST,
    )

    val INDENT_CHILDREN_ATTRIBUTE = arrayOf(
        GdTypes.IF_ST,
        GdTypes.FOR_ST,
        GdTypes.WHILE_ST,
        GdTypes.ELIF_ST,
        GdTypes.ELSE_ST,
        GdTypes.SIGNAL_DECL_TL,
        GdTypes.ENUM_DECL_TL,
        GdTypes.ARRAY_DECL,
        GdTypes.DICT_DECL,
        GdTypes.CLASS_DECL_TL,
        GdTypes.METHOD_DECL_TL,
        GdTypes.GET_DECL,
        GdTypes.SET_DECL,
        GdTypes.SUITE,
    )

    val SEPARATE_ANNOTATOR_GROUPS = arrayOf(
        "export",
        "onready",
    )

}
