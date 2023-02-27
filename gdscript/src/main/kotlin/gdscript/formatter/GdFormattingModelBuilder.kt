package gdscript.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import gdscript.GdFileType
import gdscript.GdLanguage
import gdscript.formatter.block.GdBlock
import gdscript.formatter.settings.GdSpacingUtil.lines
import gdscript.psi.GdTypes

class GdFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings;
        val customSettings = settings.getCustomSettings(GdCodeStyleSettings::class.java);

        return FormattingModelProvider
            .createFormattingModelForPsiFile(formattingContext.containingFile,
                GdBlock(formattingContext.node,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    customSettings,
                    createSpaceBuilder(settings),
                    Indent.getNoneIndent(),
                ),
                settings)
    }

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val custom = settings.getCustomSettings(GdCodeStyleSettings::class.java);
        INDENT_SIZE = settings.getIndentSize(GdFileType); // TODO u tabů se to posere, když je za stmt volné odsazení tak se převede na mezery a konec

        var builder = SpacingBuilder(settings, GdLanguage)
            /* Spacings */
            .before(GdTypes.COMMA).spaceIf(custom.SPACE_BEFORE_COMMA)
            .after(GdTypes.COMMA).spaceIf(custom.SPACE_AFTER_COMMA)
            .before(GdTypes.COLON).spaceIf(custom.SPACE_BEFORE_COLON)
            .before(GdTypes.TYPED).spaceIf(custom.SPACE_BEFORE_COLON)
            .after(GdTypes.COLON).spaceIf(custom.SPACE_AFTER_COLON)

            /* Extends & ClassName */
//            .before(NAMINGS).blankLines(0)
//            .after(NAMINGS).blankLines(custom.LINES_AFTER_HEADER)
            // TODO keep blank lines??
            .before(NAMINGS).lines(0)
            .after(NAMINGS).lines(custom.LINES_AFTER_HEADER)

            /* Method & Classes */
            .between(GdTypes.ANNOTATION_TL, GdTypes.CLASS_VAR_DECL_TL).blankLines(0)
            .before(ROOT_BLOCKS).blankLines(2);

            // Separate groups
            ROOT_VARIABLES.types.forEachIndexed { iLeft, left ->
                ROOT_VARIABLES.types.forEachIndexed { iRight, right ->
                    if (iLeft != iRight) {
                        builder.between(left, right).blankLines(custom.LINES_AFTER_VARIABLE_GROUP);
                    }
                }
            }

            // Then within group
            builder = builder.between(ROOT_VARIABLES, ROOT_VARIABLES).blankLines(custom.LINES_IN_BETWEEN_VARIABLE_GROUP)

            /* Const & Vars */
//            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.CLASS_VAR_DECL_TL).spacing(0, Int.MAX_VALUE, custom.LINES_IN_BETWEEN_VARIABLE_GROUP, false, custom.LINES_IN_BETWEEN_VARIABLE_GROUP)
//            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.ANNOTATION_TL).spacing(0, Int.MAX_VALUE, custom.LINES_IN_BETWEEN_VARIABLE_GROUP, false, custom.LINES_IN_BETWEEN_VARIABLE_GROUP)
//            .between(GdTypes.CONST_DECL_TL, GdTypes.CONST_DECL_TL).spacing(0, Int.MAX_VALUE, custom.LINES_IN_BETWEEN_VARIABLE_GROUP, false, custom.LINES_IN_BETWEEN_VARIABLE_GROUP)
//            .between(GdTypes.SIGNAL_DECL_TL, GdTypes.SIGNAL_DECL_TL).spacing(0, Int.MAX_VALUE, custom.LINES_IN_BETWEEN_VARIABLE_GROUP, false, custom.LINES_IN_BETWEEN_VARIABLE_GROUP)
//            .after(ROOT_VARIABLES).spacing(0, Int.MAX_VALUE, custom.LINES_AFTER_VARIABLE_GROUP, false, custom.LINES_AFTER_VARIABLE_GROUP)


            /** Root lines */
//            .before(TokenSet.create(GdTypes.INHERITANCE_ID_NM, GdTypes.CLASS_NAME_NMI)).spaces(1)
//            .beforeInside(GdTypes.DEDENT, GdTypes.SUITE).spacing(0, Int.MAX_VALUE, 1, false, 0)
//            .between(NAMINGS, ROOTS).spacing(0, 0, 1, false, 1)
//
//            .between(GdTypes.ENUM_DECL_TL, GdTypes.ENUM_DECL_TL).spacing(0, Int.MAX_VALUE, 1, false, 0)
//            .between(GdTypes.CONST_DECL_TL, GdTypes.CLASS_VAR_DECL_TL).spacing(0, Int.MAX_VALUE, 2, false, 1)
//            .between(GdTypes.CONST_DECL_TL, GdTypes.ENUM_DECL_TL).spacing(0, Int.MAX_VALUE, 2, false, 1)
//            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.CONST_DECL_TL).spacing(0, Int.MAX_VALUE, 2, false, 1)
//            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.ENUM_DECL_TL).spacing(0, Int.MAX_VALUE, 2, false, 1)
//            .between(GdTypes.ENUM_DECL_TL, GdTypes.CONST_DECL_TL).spacing(0, Int.MAX_VALUE, 2, false, 1)
//            .between(GdTypes.ENUM_DECL_TL, GdTypes.CLASS_VAR_DECL_TL).spacing(0, Int.MAX_VALUE, 2, false, 1)
//
//            .before(GdTypes.METHOD_DECL_TL).spacing(0, Int.MAX_VALUE, custom.LINES_BEFORE_FUNC + 1, false, custom.LINES_BEFORE_FUNC)

            /** Operators */
//            .around(TokenSet.create(GdTypes.TEST_OPERATOR, GdTypes.ASSIGN, GdTypes.EQ, GdTypes.ASSIGN_TYPED)).spacing(1, Int.MAX_VALUE, 0, false, 1)

        return builder;
    }

    companion object {
        val NAMINGS = TokenSet.create(GdTypes.INHERITANCE, GdTypes.CLASS_NAMING)
        val ROOT_VARIABLES = TokenSet.create(
            GdTypes.CONST_DECL_TL,
            GdTypes.CLASS_VAR_DECL_TL,
            GdTypes.SIGNAL_DECL_TL,
            GdTypes.ANNOTATION_TL,
        )
        val ROOT_BLOCKS = TokenSet.create(GdTypes.METHOD_DECL_TL, GdTypes.CLASS_DECL_TL)
        var INDENT_SIZE = 4;
    }

}
