package gdscript.formatter

import com.intellij.formatting.FormattingMode
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import gdscript.formatter.impl.createSpacingBuilder


// Taken over from RsFmtContext.kt
@ConsistentCopyVisibility
data class GdFmtContext private constructor(
    val codeStyleSettings: CodeStyleSettings,
    val commonSettings: CommonCodeStyleSettings,
    val gdSettings: GdCodeStyleSettings,
    val spacingBuilder: SpacingBuilder,
    val formattingMode: FormattingMode,
) {
    companion object {
        fun create(settings: CodeStyleSettings, formattingMode: FormattingMode): GdFmtContext {
            val commonSettings = settings.gdCommonSettings
            val gdSettings = settings.gdCustomSettings
            return GdFmtContext(
                settings,
                commonSettings,
                gdSettings,
                createSpacingBuilder(commonSettings, gdSettings),
                formattingMode,
            )
        }
    }
}
