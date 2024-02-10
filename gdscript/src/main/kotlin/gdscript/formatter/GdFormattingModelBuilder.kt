package gdscript.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import gdscript.GdFileType
import gdscript.GdLanguage
import gdscript.formatter.block.Alignments
import gdscript.formatter.block.GdBlock
import gdscript.formatter.settings.GdSpacingUtil.emptyLines
import gdscript.formatter.settings.GdSpacingUtil.forcedLines
import gdscript.psi.GdTypes

class GdFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val customSettings = settings.getCustomSettings(GdCodeStyleSettings::class.java)
        val alignments = Alignments(customSettings)
        alignments.initialize()

        val initialBlock = GdBlock(
            formattingContext.node,
            null, // Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(),
            settings,
            createSpaceBuilder(settings),
            Indent.getNoneIndent(),
            alignments,
        )

        return FormattingModelProvider
            .createFormattingModelForPsiFile(
                formattingContext.containingFile,
                initialBlock,
                settings
            )
    }

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val custom = settings.getCustomSettings(GdCodeStyleSettings::class.java)
        INDENT_SIZE =
            settings.getIndentSize(GdFileType); // TODO u tabů se to posere, když je za stmt volné odsazení tak se převede na mezery a konec

        val builder = SpacingBuilder(settings, GdLanguage)
            /* Spacings */
            .before(GdTypes.COMMA).spaceIf(custom.SPACE_BEFORE_COMMA)
            .after(GdTypes.COMMA).spaceIf(custom.SPACE_AFTER_COMMA)
            .before(GdTypes.COLON).spaceIf(custom.SPACE_BEFORE_COLON)
            .before(GdTypes.TYPED).spaceIf(custom.SPACE_BEFORE_COLON)
            .after(GdTypes.COLON).spaceIf(custom.SPACE_AFTER_COLON)
            .after(GdTypes.ANNOTATION_TL).spaces(1)

            /* Extends & ClassName */
            .between(GdTypes.CLASS_NAME_NMI, GdTypes.INHERITANCE).spacing(1, -1, 0, true, custom.LINES_AFTER_HEADER)
            .before(NAMINGS).forcedLines(0)
            .after(NAMINGS).emptyLines(custom.LINES_AFTER_HEADER)

            /* Method & Classes */
            .between(GdTypes.ANNOTATION_TL, GdTypes.CLASS_VAR_DECL_TL).forcedLines(0, 1)
            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.ANNOTATION_TL)
            .emptyLines(custom.LINES_IN_BETWEEN_VARIABLE_GROUP)

            /* Stmt */
            .before(STATEMENTS).emptyLines(custom.LINES_WITHIN_SUITE)
            .before(ROOT_BLOCKS).emptyLines(custom.LINES_BEFORE_FUNC)

        // Separate groups
        ROOT_VARIABLES.types.forEachIndexed { iLeft, left ->
            ROOT_VARIABLES.types.forEachIndexed { iRight, right ->
                if (iLeft != iRight) {
                    builder.between(left, right).emptyLines(custom.LINES_AFTER_VARIABLE_GROUP)
                }
            }
        }

        // Then within group
        builder.between(ROOT_VARIABLES, ROOT_VARIABLES).emptyLines(custom.LINES_IN_BETWEEN_VARIABLE_GROUP)

        // operators
        builder.around(TokenSet.create(GdTypes.TEST_OPERATOR, GdTypes.ASSIGN, GdTypes.EQ, GdTypes.ASSIGN_TYPED))
            .spaces(1)

        return builder
    }

    companion object {
        val NAMINGS = TokenSet.create(GdTypes.INHERITANCE, GdTypes.CLASS_NAMING)
        val ROOT_VARIABLES = TokenSet.create(
            GdTypes.CONST_DECL_TL,
            GdTypes.CLASS_VAR_DECL_TL,
            GdTypes.SIGNAL_DECL_TL,
            GdTypes.ANNOTATION_TL,
            GdTypes.ENUM_DECL_TL,
        )
        val STATEMENTS = TokenSet.create(
            GdTypes.ASSIGN_ST,
            GdTypes.VAR_DECL_ST,
            GdTypes.CONST_DECL_ST,
            GdTypes.IF_ST,
            GdTypes.ELIF_ST,
            GdTypes.ELSE_ST,
            GdTypes.WHILE_ST,
            GdTypes.FOR_ST,
            GdTypes.MATCH_ST,
            GdTypes.FLOW_ST,
            GdTypes.EXPR_ST,
        )
        val ROOT_BLOCKS = TokenSet.create(GdTypes.METHOD_DECL_TL, GdTypes.CLASS_DECL_TL)
        var INDENT_SIZE = 4
    }

}
