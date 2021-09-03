package gdscript.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import gdscript.GdLanguage
import gdscript.formatter.block.GdFileBlock
import gdscript.psi.GdTypes

class GdFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings;
        // TODO tohle je pro bloky
        val customSettings = settings.getCustomSettings(GdCodeStyleSettings::class.java);

        return FormattingModelProvider
            .createFormattingModelForPsiFile(formattingContext.containingFile,
                GdFileBlock(formattingContext.node,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    createSpaceBuilder(settings)),
                settings)
    }

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val custom = settings.getCustomSettings(GdCodeStyleSettings::class.java);

        // TODO tohle předělat -> mezery mezi var/const různé + grouping
        return SpacingBuilder(settings, GdLanguage.INSTANCE)
            .before(GdTypes.CLASS_NAME_NM).spaces(1)
            .before(GdTypes.INHERITANCE_ID_NMI).spaces(1)
            .before(GdTypes.CLASS_NAMING).spacing(0, 0, 1, false, 0)
            .before(GdTypes.TOOLLINE).spacing(0, 0, 1, false, 0)

            .between(GdTypes.CONST_DECL_TL, GdTypes.CLASS_VAR_DECL_TL).spacing(0, Int.MAX_VALUE, 0, false, 1)
            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.CONST_DECL_TL).spacing(0, Int.MAX_VALUE, 0, false, 1)
            .between(GdTypes.CONST_DECL_TL, GdTypes.CONST_DECL_TL).spacing(0, Int.MAX_VALUE, 1, false, 0)
            .between(GdTypes.CLASS_VAR_DECL_TL, GdTypes.CLASS_VAR_DECL_TL).spacing(0, Int.MAX_VALUE, 1, false, 0)

            .between(GdTypes.METHOD_DECL_TL, GdTypes.METHOD_DECL_TL).spacing(0, Int.MAX_VALUE, 1, false, 2)
//            .before(GdTypes.CONST_DECL_TL).spacing(0, 0, 1, false, 0)
//            .before(GdTypes.CLASS_VAR_DECL_TL).spacing(0, 0, 1, false, 0)
    }

}