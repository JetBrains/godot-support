package gdscript.formatter.impl

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.tree.TokenSet
import gdscript.formatter.GdCodeStyleSettings
import gdscript.formatter.GdFmtContext
import gdscript.formatter.block.GdASTBlock
import gdscript.formatter.block.GdBlock
import gdscript.formatter.block.GdBlocks
import gdscript.formatter.block.GdSyntheticBlock
import gdscript.formatter.emptyLines
import gdscript.formatter.forcedLines
import gdscript.formatter.minMaxLines
import gdscript.psi.GdArrayPattern
import gdscript.psi.GdTypes
import gdscript.psi.GdTypes.AND
import gdscript.psi.GdTypes.ANDAND
import gdscript.psi.GdTypes.ANNOTATION_TL
import gdscript.psi.GdTypes.ARG_LIST
import gdscript.psi.GdTypes.ARRAY_DECL
import gdscript.psi.GdTypes.ARRAY_PATTERN
import gdscript.psi.GdTypes.AS
import gdscript.psi.GdTypes.ASSIGN_SIGN
import gdscript.psi.GdTypes.ASSIGN_ST
import gdscript.psi.GdTypes.ASSIGN_TYPED
import gdscript.psi.GdTypes.AWAIT
import gdscript.psi.GdTypes.BACKSLASH
import gdscript.psi.GdTypes.CLASS
import gdscript.psi.GdTypes.CLASS_DECL_TL
import gdscript.psi.GdTypes.CLASS_NAME
import gdscript.psi.GdTypes.CLASS_NAME_NMI
import gdscript.psi.GdTypes.CLASS_NAMING
import gdscript.psi.GdTypes.CLASS_VAR_DECL_TL
import gdscript.psi.GdTypes.COLON
import gdscript.psi.GdTypes.COMMA
import gdscript.psi.GdTypes.COMMENT
import gdscript.psi.GdTypes.CONST
import gdscript.psi.GdTypes.CONST_DECL_ST
import gdscript.psi.GdTypes.CONST_DECL_TL
import gdscript.psi.GdTypes.DICT_DECL
import gdscript.psi.GdTypes.DOT
import gdscript.psi.GdTypes.ELIF
import gdscript.psi.GdTypes.ELIF_ST
import gdscript.psi.GdTypes.ELSE
import gdscript.psi.GdTypes.ELSE_ST
import gdscript.psi.GdTypes.ENUM
import gdscript.psi.GdTypes.ENUM_DECL_TL
import gdscript.psi.GdTypes.EQ
import gdscript.psi.GdTypes.EXPR_ST
import gdscript.psi.GdTypes.EXTENDS
import gdscript.psi.GdTypes.FACTOR_SIGN
import gdscript.psi.GdTypes.FLOW_ST
import gdscript.psi.GdTypes.FOR
import gdscript.psi.GdTypes.FOR_ST
import gdscript.psi.GdTypes.FUNC
import gdscript.psi.GdTypes.FUNC_DECL_EX
import gdscript.psi.GdTypes.GET_DECL
import gdscript.psi.GdTypes.IF
import gdscript.psi.GdTypes.IF_ST
import gdscript.psi.GdTypes.IN
import gdscript.psi.GdTypes.INHERITANCE
import gdscript.psi.GdTypes.IS
import gdscript.psi.GdTypes.KEY_VALUE
import gdscript.psi.GdTypes.KEY_VALUE_PATTERN
import gdscript.psi.GdTypes.LBSHIFT
import gdscript.psi.GdTypes.LCBR
import gdscript.psi.GdTypes.LRBR
import gdscript.psi.GdTypes.LSBR
import gdscript.psi.GdTypes.MATCH
import gdscript.psi.GdTypes.MATCH_BLOCK
import gdscript.psi.GdTypes.MATCH_ST
import gdscript.psi.GdTypes.METHOD_DECL_TL
import gdscript.psi.GdTypes.NEGATE
import gdscript.psi.GdTypes.NOT
import gdscript.psi.GdTypes.OPERATOR
import gdscript.psi.GdTypes.OR
import gdscript.psi.GdTypes.OROR
import gdscript.psi.GdTypes.PARAM
import gdscript.psi.GdTypes.PARAM_LIST
import gdscript.psi.GdTypes.PARENTHESIZED_EX
import gdscript.psi.GdTypes.PATTERN
import gdscript.psi.GdTypes.RBSHIFT
import gdscript.psi.GdTypes.RCBR
import gdscript.psi.GdTypes.RET
import gdscript.psi.GdTypes.RETURN
import gdscript.psi.GdTypes.RETURN_HINT
import gdscript.psi.GdTypes.RRBR
import gdscript.psi.GdTypes.RSBR
import gdscript.psi.GdTypes.SEMICON
import gdscript.psi.GdTypes.SETGET_DECL
import gdscript.psi.GdTypes.SET_DECL
import gdscript.psi.GdTypes.SIGN
import gdscript.psi.GdTypes.SIGNAL
import gdscript.psi.GdTypes.SIGNAL_DECL_TL
import gdscript.psi.GdTypes.STATIC
import gdscript.psi.GdTypes.STMT_OR_SUITE
import gdscript.psi.GdTypes.SUITE
import gdscript.psi.GdTypes.SUPER
import gdscript.psi.GdTypes.TYPED
import gdscript.psi.GdTypes.TYPED_VAL
import gdscript.psi.GdTypes.TYPE_HINT
import gdscript.psi.GdTypes.VAR
import gdscript.psi.GdTypes.VAR_DECL_ST
import gdscript.psi.GdTypes.VAR_NMI
import gdscript.psi.GdTypes.WHEN
import gdscript.psi.GdTypes.WHILE
import gdscript.psi.GdTypes.WHILE_ST
import gdscript.psi.GdTypes.XOR

fun createSpacingBuilder(commonSettings: CommonCodeStyleSettings, gdSettings: GdCodeStyleSettings): SpacingBuilder {
    return SpacingBuilder(commonSettings)
        .around(BACKSLASH).spaceIf(gdSettings.SPACE_AROUND_BACKSLASH)
        // ----- Comments -----
        .before(COMMENT).spacing(2, 0, 0, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_IN_CODE)
        .after(COMMENT).spacing(0, 0, 1, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_IN_CODE)

        // ----- Class header: `class_name X extends Y` -----
        .before(CLASS_NAMING).forcedLines(0)
        .apply {
            between(CLASS_NAMING, INHERITANCE).let {
                when (gdSettings.CLASS_HEADER_STYLE) {
                    GdCodeStyleSettings.CLASS_HEADER_STYLE_NONE -> it.spacing(1, 1, 0, true, 0)
                    GdCodeStyleSettings.CLASS_HEADER_STYLE_FORCE_COMBINED -> it.spacing(1, 1, 0, false, 0)
                    GdCodeStyleSettings.CLASS_HEADER_STYLE_FORCE_SEPARATE -> it.forcedLines(0, 1)
                }
            }
        }
        .afterInside(INHERITANCE, CLASS_DECL_TL).spaces(0)

        // ----- Blank lines around classes & methods -----
        .betweenInside(COLON, METHOD_DECL_TL, CLASS_DECL_TL)
        .minMaxLines(
            gdSettings.MIN_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS,
            gdSettings.MAX_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS
        )
        .betweenInside(TokenSet.create(COLON), TokenSet.create(CLASS_VAR_DECL_TL, CLASS_DECL_TL), CLASS_DECL_TL).forcedLines(0)
        .aroundInside(TokenSet.create(CLASS_DECL_TL, METHOD_DECL_TL), CLASS_DECL_TL)
        .minMaxLines(
            gdSettings.MIN_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS,
            gdSettings.MAX_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS
        )
        .around(TokenSet.create(CLASS_DECL_TL, METHOD_DECL_TL))
        .minMaxLines(
            gdSettings.MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS,
            gdSettings.MAX_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS
        )
        .after(INHERITANCE).minMaxLines(gdSettings.MIN_LINES_AFTER_HEADER, gdSettings.MAX_LINES_AFTER_HEADER)

        // ----- Blank lines between members and statements -----
        .after(ANNOTATION_TL).spacing(1, 1, 0, true, 0)
        .between(ROOT_VARIABLES, ROOT_VARIABLES).emptyLines(gdSettings.MAX_LINES_IN_BETWEEN_VARIABLE_GROUPS)
        .between(STATEMENTS, STATEMENTS).emptyLines(gdSettings.LINES_WITHIN_SUITE)
        // .before(STATEMENTS).emptyLines(gdSettings.LINES_WITHIN_SUITE)
        // SUITE is its own block post-migration — clamp blanks before it the same way
        // we clamp them before any naked STATEMENT in a single-statement body.
        .before(STMT_OR_SUITE).emptyLines(gdSettings.LINES_WITHIN_SUITE)

        // ----- Method / lambda parentheses -----
        // before `(` - `func name_(...)`, lambda `func_(...)`, call `f_(2)`
        .beforeInside(PARAM_LIST, METHOD_DECL_TL).spaceIf(commonSettings.SPACE_BEFORE_METHOD_PARENTHESES)
        .beforeInside(PARAM_LIST, FUNC_DECL_EX).spaceIf(gdSettings.SPACE_BEFORE_LAMBDA_PARENTHESES)
        .before(ARG_LIST).spaceIf(commonSettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES)
        // within method-call `(_)`
        .betweenInside(LRBR, RRBR, ARG_LIST).spaceIf(commonSettings.SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES)
        .afterInside(LRBR, ARG_LIST)
        .spaceIf(commonSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, commonSettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE)
        .beforeInside(RRBR, ARG_LIST)
        .spaceIf(commonSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, commonSettings.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE)
        // within method-decl `(_)`
        .betweenInside(LRBR, RRBR, PARAM_LIST).spaceIf(commonSettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES)
        .afterInside(LRBR, PARAM_LIST)
        .spaceIf(commonSettings.SPACE_WITHIN_METHOD_PARENTHESES, commonSettings.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE)
        .beforeInside(RRBR, PARAM_LIST)
        .spaceIf(commonSettings.SPACE_WITHIN_METHOD_PARENTHESES, commonSettings.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE)
        // parenthesized expression `(a + b)`
        .withinPairInside(LRBR, RRBR, PARENTHESIZED_EX).spaces(0)

        // ----- Brackets `[]` and braces `{}` -----
        // dict `{k: v}`
        .betweenInside(COMMA, RCBR, DICT_DECL).spaceIf(
            commonSettings.SPACE_WITHIN_BRACES || commonSettings.SPACE_AFTER_COMMA,
            gdSettings.DICT_NEW_LINE_BEFORE_RIGHT_BRACE,
        )
        .afterInside(LCBR, DICT_DECL).spaceIf(commonSettings.SPACE_WITHIN_BRACES, gdSettings.DICT_NEW_LINE_AFTER_LEFT_BRACE)
        .beforeInside(RCBR, DICT_DECL).spaceIf(commonSettings.SPACE_WITHIN_BRACES, gdSettings.DICT_NEW_LINE_BEFORE_RIGHT_BRACE)
        // lua-style dict override `{k = v}`
        .aroundInside(EQ, KEY_VALUE).spaceIf(gdSettings.SPACE_AROUND_EQ_IN_LUA_STYLE_DICTS)
        // array `[a, b]`
        .betweenInside(COMMA, RSBR, ARRAY_DECL).spaceIf(
            commonSettings.SPACE_WITHIN_BRACKETS || commonSettings.SPACE_AFTER_COMMA,
            gdSettings.ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET,
        )
        .afterInside(LSBR, ARRAY_DECL).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS, gdSettings.ARRAY_NEW_LINE_AFTER_LEFT_BRACKET)
        .beforeInside(RSBR, ARRAY_DECL).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS, gdSettings.ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET)
        // typed value `Dictionary[int,_int]`
        .betweenInside(COMMA, TYPE_HINT, TYPED_VAL).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS || commonSettings.SPACE_AFTER_COMMA)
        // indexer `arr_[0]`
        .beforeInside(LSBR, GdTypes.ARR_EX).spaceIf(gdSettings.SPACE_BEFORE_LBRACKET)
        // generic fallbacks (trailing comma, withinPair)
        .between(COMMA, RSBR).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS || commonSettings.SPACE_AFTER_COMMA)
        .between(COMMA, RCBR).spaceIf(commonSettings.SPACE_WITHIN_BRACES || commonSettings.SPACE_AFTER_COMMA)
        .withinPair(LSBR, RSBR).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS)
        .withinPair(LCBR, RCBR).spaceIf(commonSettings.SPACE_WITHIN_BRACES)

        // ----- Type annotations and colons -----
        // var x_: int (local)
        .betweenInside(VAR_NMI, TYPED, VAR_DECL_ST).spaces(0)
        // var x_: int (top level)
        .betweenInside(VAR_NMI, TYPED, CLASS_VAR_DECL_TL).spaces(0)
        // const x_: int
        .betweenInside(VAR_NMI, TYPED, CONST_DECL_TL).spaces(0)
        // func f(x_: int)
        .betweenInside(VAR_NMI, TYPED, PARAM).spaces(0)
        // space after `:` — `var x:_int`, `{1:_2}`
        .afterInside(COLON, TYPED).spaceIf(gdSettings.SPACE_AFTER_COLON)
        .afterInside(COLON, EXPRESSIONS_WITH_COLON).spaceIf(gdSettings.SPACE_AFTER_COLON)
        // generic before `:` — `func hello()_:`
        .before(COLON).spaceIf(gdSettings.SPACE_BEFORE_COLON)

        // ----- Return arrow `->` -----
        .afterInside(RET, RETURN_HINT).spaceIf(gdSettings.SPACE_AROUND_ARROW_IN_METHOD_RETURN)
        .beforeInside(RETURN_HINT, METHOD_DECL_TL).spaceIf(gdSettings.SPACE_AROUND_ARROW_IN_METHOD_RETURN)

        // ----- Punctuation -----
        .after(COMMA).spaceIf(commonSettings.SPACE_AFTER_COMMA)
        .before(COMMA).spaceIf(commonSettings.SPACE_BEFORE_COMMA)
        .around(DOT).spaces(0)
        .before(SEMICON).spaceIf(gdSettings.SPACE_BEFORE_SEMICOLON)

        // ----- Operators -----
        // assignment `a_=_5`
        .aroundInside(ASSIGN_SIGN, ASSIGN_ST).spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
        .aroundInside(ASSIGN_TYPED, PARAM).spaceIf(gdSettings.SPACE_AROUND_EQ_IN_NAMED_PARAMETER)
        .aroundInside(ASSIGN_TYPED, VAR_DECL_ST).spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
        .aroundInside(ASSIGN_TYPED, CLASS_VAR_DECL_TL).spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
        .aroundInside(ASSIGN_TYPED, CONST_DECL_TL).spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)

        // binary — PLUS_EX covers `+` and `-`; FACTOR_EX covers `* / % **`
        .around(SIGN).spaceIf(commonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
        .around(FACTOR_SIGN).spaceIf(commonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
        .around(SHIFT_OPERATIONS).spaceIf(commonSettings.SPACE_AROUND_SHIFT_OPERATORS)
        .around(GdTypes.BIT_AND_SIGN).spaceIf(commonSettings.SPACE_AROUND_BITWISE_OPERATORS)
        // equality + relational collapsed (would need lexer split to separate)
        .around(OPERATOR).spaceIf(commonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)

        // ----- Properties -----
        .before(SETGET_DECL).spaceIf(gdSettings.SPACE_BEFORE_COLON)
        .betweenInside(LRBR, VAR_NMI, SET_DECL).spaces(0)
        .betweenInside(VAR_NMI, RRBR, SET_DECL).spaces(0)
        .aroundInside(EQ, TokenSet.create(GET_DECL, SET_DECL)).spaces(1)

        // ----- Keywords -----
        .around(SINGLE_SPACE_KEYWORDS).spaces(1)
}


fun GdASTBlock.computeSpacing(child1: Block?, child2: Block, ctx: GdFmtContext): Spacing? {
    val gdSettings = ctx.gdSettings
    val commonSettings = ctx.commonSettings

    if (child1 !is GdBlock || child2 !is GdBlock) {
        // Synthetic chain-step blocks report their representative's element type (ATTRIBUTE_EX),
        // which is enough for most SpacingBuilder rules. Try that first.
        ctx.spacingBuilder.getSpacing(this, child1, child2)?.let { return it }

        // Fall back to leaf-based matching so unscoped rules like `.around(DOT)` and
        // `.around(BACKSLASH)` still apply across a synthetic-block boundary — e.g. between
        // a base block / BACKSLASH and the synth step whose first inner leaf is DOT.
        val leftLeaf = if (child1 is GdSyntheticBlock) descendToLastGdBlock(child1) else child1 as? GdBlock
        val rightLeaf = if (child2 is GdSyntheticBlock) descendToFirstGdBlock(child2) else child2 as? GdBlock
        if ((leftLeaf !== child1 || rightLeaf !== child2) && leftLeaf != null && rightLeaf != null) {
            return ctx.spacingBuilder.getSpacing(this, leftLeaf, rightLeaf)
        }
        return null
    }
    // Special case for `[ ]` inside `match`, because the structure is
    //      ARRAY_PATTERN
    //     /     |    \
    // LSBR - PATTERN - RSBR
    if (node.elementType == ARRAY_PATTERN
        && (this.node.psi as GdArrayPattern).patternList.first().children.size == 0
    ) {

        if ( child1.node.elementType == LSBR
            && child2.node.elementType == PATTERN) {
            if (commonSettings.SPACE_WITHIN_BRACKETS) {
                return Spacing.createSpacing(1, 1, 0, false, 0)
            } else {
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
        } else if (child1.node.elementType == PATTERN && child2.node.elementType == RSBR) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }
    }

    firstMethodInClassAfterInlineHeaderComment(child1, child2, ctx)?.let { return it }
    if (child1.node.elementType == COMMENT) {
        val maxBlankLines = if (parent == null) 2 else gdSettings.LINES_WITHIN_SUITE
        return Spacing.createSpacing(0, 0, 1, true, maxBlankLines)
    }

    // Inline (trailing) comments — keep them attached to child1 with the configured "before(COMMENT)"
    // spacing rule. Skipping over a comment via effectiveNextBlock would otherwise apply the
    // surrounding rule (e.g. `afterInside(LSBR, ARRAY_DECL)` → no space), collapsing `[  # foo` to `[# foo`.
    // Mirrors PyBlock.getSpacing which only skips comments preceded by a line break.
    if (child2.node.elementType == COMMENT && !hasLineBreakBefore(child2.node)) {
        return ctx.spacingBuilder.getSpacing(this, child1, child2)
    }

    // Decide spacing between the "real" blocks - skipping comments, annotations
    val effectiveBlock = effectiveNextBlock(child2) ?: return null

    if (gdSettings.NEW_LINE_AFTER_COLON
        && child1.node.elementType == COLON
        && GdBlocks.NEW_LINE_AFTER_COLON_PARENTS.contains(child1.node.treeParent.elementType)
    ) {
        return Spacing.createSpacing(0, 0, 1, true, gdSettings.LINES_WITHIN_SUITE)
    }
    if (child1.node.elementType == CLASS_NAMING && child2.node.elementType != INHERITANCE ) {
        return Spacing.createSpacing(0, 0, gdSettings.MIN_LINES_AFTER_HEADER + 1, true, gdSettings.MAX_LINES_AFTER_HEADER)
    }

    // Blank lines between consecutive statements in a suite. EXPR_ST is flattened out
    // of the block tree (GdBlocks.SKIP_TOKENS), so adjacent expression statements
    // surface as their inner expression blocks (e.g. CALL_EX) and never match the
    // .between(STATEMENTS, STATEMENTS) rule. Clamp here at the SUITE container instead.
    if (node.elementType == SUITE && child2.node.elementType != COMMENT) {
        return Spacing.createSpacing(1, 0, 0, true, gdSettings.LINES_WITHIN_SUITE)
    }

    return ctx.spacingBuilder.getSpacing(this, child1, effectiveBlock)
}


/**
 * Walks forward through this block's children starting at [start], skipping COMMENTs and treating
 * an ANNOTATION_TL chain that ends in METHOD_DECL_TL as if it were the METHOD_DECL_TL block.
 * Returns null when there is no non-comment block at or after [start].
 */
private fun GdASTBlock.effectiveNextBlock(start: GdBlock): GdASTBlock? {
    val siblings = subBlocks
    val startIdx = siblings.indexOf(start)
    if (startIdx < 0) return start

    var idx = startIdx
    while (idx < siblings.size && siblings[idx].node.elementType == COMMENT) idx++
    if (idx >= siblings.size) return null

    var i = idx
    var sawAnnotation = false
    while (i < siblings.size && siblings[i].node.elementType == ANNOTATION_TL) {
        sawAnnotation = true
        i++
    }
    return if (sawAnnotation && i < siblings.size && siblings[i].node.elementType == METHOD_DECL_TL) {
        siblings[i]
    } else {
        siblings[idx]
    }
}

/**
 * Whether [node] is preceded by whitespace containing a `\n` (i.e. starts on a fresh line).
 * Reads the source text directly to be robust against GDScript's mix of whitespace token
 * types (TokenType.WHITE_SPACE, NEW_LINE, INDENT, DEDENT, NEW_LINE_END, END_STMT, ...).
 */
private fun hasLineBreakBefore(node: ASTNode): Boolean {
    val text = node.psi?.containingFile?.text ?: return false
    val before = text.substring(0, node.startOffset).trimEnd(' ', '\t')
    return before.endsWith('\n') || before.endsWith('\r')
}

/**
 * Enforce MIN/MAX_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS between a trailing-inline COMMENT
 * sitting on the class header line and the first METHOD_DECL_TL of the class body.
 *
 * Returns null when not applicable (so callers fall through to the existing rules):
 *  - parent block is not CLASS_DECL_TL,
 *  - child1 is not a COMMENT,
 *  - child1 starts on its own line (a leading `# comment`, not an inline trailing one),
 *  - the first non-comment sibling at or after child2 is not METHOD_DECL_TL (e.g. CLASS_VAR_DECL_TL).
 */
private fun GdASTBlock.firstMethodInClassAfterInlineHeaderComment(
    child1: GdBlock,
    child2: GdBlock,
    ctx: GdFmtContext,
): Spacing? {
    if (node.elementType != CLASS_DECL_TL) return null
    if (child1.node.elementType != COMMENT) return null
    if (hasLineBreakBefore(child1.node)) return null
    val effective = effectiveNextBlock(child2) ?: return null
    if (effective.node.elementType != METHOD_DECL_TL) return null

    val gdSettings = ctx.gdSettings
    val min = gdSettings.MIN_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS + 1
    val max = (gdSettings.MAX_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS + 1).coerceAtLeast(min)
    return Spacing.createSpacing(0, 0, min, true, max)
}

/**
 * Keywords that should always be surrounded by a single space
 * (mirrors PythonFormattingModelBuilder.SINGLE_SPACE_KEYWORDS).
 */
private val SINGLE_SPACE_KEYWORDS: TokenSet = TokenSet.create(
    NEGATE,
    IN,
    AND,
    ANDAND,
    OR,
    OROR,
    IS,
    NOT,
    IF,
    ELIF,
    ELSE,
    FOR,
    WHILE,
    RETURN,
    AS,
    MATCH,
    WHEN,
    CLASS,
    CLASS_NAME,
    EXTENDS,
    FUNC,
    VAR,
    CONST,
    ENUM,
    SIGNAL,
    STATIC,
    AWAIT,
    SUPER,
    CLASS_NAME_NMI,
)

private val EXPRESSIONS_WITH_COLON: TokenSet = TokenSet.create(KEY_VALUE, KEY_VALUE_PATTERN, FUNC_DECL_EX)

private val ROOT_VARIABLES: TokenSet = TokenSet.create(
    CONST_DECL_TL,
    CLASS_VAR_DECL_TL,
    SIGNAL_DECL_TL,
    ANNOTATION_TL,
    ENUM_DECL_TL,
)

private val STATEMENTS: TokenSet = TokenSet.create(
    ASSIGN_ST,
    VAR_DECL_ST,
    CONST_DECL_ST,
    IF_ST,
    ELIF_ST,
    ELSE_ST,
    WHILE_ST,
    FOR_ST,
    MATCH_ST,
    FLOW_ST,
    EXPR_ST,
    MATCH_BLOCK,
)

private val SHIFT_OPERATIONS: TokenSet = TokenSet.create(
    LBSHIFT,
    RBSHIFT
)

/** Walk into a synthetic block's first sub-block until a non-synthetic GdBlock surfaces. */
private fun descendToFirstGdBlock(block: GdASTBlock): GdBlock? = when (block) {
    is GdBlock -> block
    is GdSyntheticBlock -> block.subBlocks.firstOrNull()?.let { descendToFirstGdBlock(it) }
    else -> null
}

/** Walk into a synthetic block's last sub-block until a non-synthetic GdBlock surfaces. */
private fun descendToLastGdBlock(block: GdASTBlock): GdBlock? = when (block) {
    is GdBlock -> block
    is GdSyntheticBlock -> block.subBlocks.lastOrNull()?.let { descendToLastGdBlock(it) }
    else -> null
}
