package gdscript.formatter.block

import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet
import gdscript.psi.GdTypes

object GdBlocks {

    val LEFT_BRACES: TokenSet = TokenSet.create(
        GdTypes.LRBR,
        GdTypes.LCBR,
        GdTypes.LSBR,
    )

    val RIGHT_BRACES: TokenSet = TokenSet.create(
        GdTypes.RRBR,
        GdTypes.RCBR,
        GdTypes.RSBR,
    )

    val BINARY_EXPRESSIONS: TokenSet = TokenSet.create(
        GdTypes.PLUS_EX,
        GdTypes.FACTOR_EX,
        GdTypes.COMPARISON_EX,
        GdTypes.SHIFT_EX,
        GdTypes.IN_EX,
        GdTypes.CAST_EX,
        GdTypes.IS_EX,
        GdTypes.BIT_AND_EX,
        GdTypes.LOGIC_EX,
    )

    val BRACKETED_INDENT_PARENTS: TokenSet = TokenSet.create(
        GdTypes.ARRAY_DECL,
        GdTypes.DICT_DECL,
        GdTypes.PARENTHESIZED_EX,
        GdTypes.ARG_LIST,
        GdTypes.PARAM_LIST,
        GdTypes.ARRAY_PATTERN,
        GdTypes.DICT_PATTERN,
        GdTypes.ANNOTATION_PARAMS,
    )

    val LIST_ELEMENT_TYPES: TokenSet = TokenSet.create(
        GdTypes.ARRAY_DECL,
        GdTypes.DICT_DECL,
        GdTypes.ARG_LIST,
        GdTypes.PARAM_LIST,
        GdTypes.PARENTHESIZED_EX,
        GdTypes.ARR_EX,
        GdTypes.ARRAY_PATTERN,
        GdTypes.DICT_PATTERN,
        GdTypes.PATTERN_LIST,
    )

    val CLASS_CHILDREN_INDENT: TokenSet = TokenSet.create(
        GdTypes.CLASS_DECL_TL,
        GdTypes.COMMENT,
        GdTypes.METHOD_DECL_TL,
        GdTypes.CLASS_VAR_DECL_TL,
        GdTypes.FLOW_ST,
    )

    val NEW_LINE_AFTER_COLON_PARENTS: TokenSet = TokenSet.create(
        GdTypes.METHOD_DECL_TL,
        GdTypes.CLASS_DECL_TL,
        GdTypes.IF_ST,
        GdTypes.ELIF_ST,
        GdTypes.ELSE_ST,
        GdTypes.FOR_ST,
        GdTypes.WHILE_ST,
        GdTypes.GET_DECL,
        GdTypes.SET_DECL,
        GdTypes.MATCH_BLOCK,
        // Additional colon-introducing headers whose parser does NOT produce a STMT_OR_SUITE
        // wrapper for an empty body — the trailing `:` ends up as the last leaf of the header
        // block itself, so the formatter's colon fallback (and `isIndentNext` for backslash
        // continuations) must recognise them.
        GdTypes.SETGET_DECL,
        GdTypes.FUNC_DECL_EX,
        GdTypes.MATCH_ST,
    )

    val EMPTY_TOKENS: TokenSet = TokenSet.create(
        TokenType.WHITE_SPACE,
        GdTypes.NEW_LINE,
        GdTypes.INDENT,
        GdTypes.DEDENT,
    )

    val STATEMENT_LIST_PARTS: TokenSet = TokenSet.create(
        GdTypes.IF_ST,
        GdTypes.ELIF_ST,
        GdTypes.ELSE_ST,
        GdTypes.FOR_ST,
        GdTypes.WHILE_ST,
        GdTypes.MATCH_BLOCK,
    )

    /** These tokens don't become blocks */
    val SKIP_TOKENS: TokenSet = TokenSet.create(
        // GdTypes.STMT_OR_SUITE, // Flattening STMT or SUITE here, makes spacing easier
        GdTypes.NEW_LINE_END,
        GdTypes.END_STMT,
        // PRIMARY_EX is just a parser wrapper around ARRAY_DECL / DICT_DECL / PARENTHESIZED_EX
        GdTypes.PRIMARY_EX,
        GdTypes.EXPR_ST,
    )

    val WHITE_SPACE: TokenSet = TokenSet.create(
        TokenType.WHITE_SPACE,
        GdTypes.NEW_LINE,
        GdTypes.NEW_LINE_END,
        GdTypes.INDENT,
        GdTypes.DEDENT,
    )

    val ALWAYS_INDENTED_TOKENS: TokenSet = TokenSet.create(
        GdTypes.ENUM_VALUE,
        GdTypes.KEY_VALUE,
        GdTypes.GET_DECL,
        GdTypes.SET_DECL,
        GdTypes.MATCH_BLOCK,
    )
}
