package gdscript.formatter.settings

import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import gdscript.formatter.GdCodeStyleSettings

object GdSettings {

    val SPACING: HashMap<String, HashMap<String, String>> = hashMapOf(
        Pair(
            "Spacing", hashMapOf(
                Pair("SPACE_AFTER_COMMA", "After ,"),
                Pair("SPACE_BEFORE_COMMA", "Before ,"),
                Pair("SPACE_AFTER_COLON", "After :"),
                Pair("SPACE_BEFORE_COLON", "Before :"),
            )
        ),
    );

    val BLANK_LINES: HashMap<String, HashMap<String, String>> = hashMapOf(
        Pair(
            "Keep maximum blank lines", hashMapOf(
                Pair("LINES_AFTER_HEADER", "After class_name/extends header"),
                Pair("LINES_IN_BETWEEN_VARIABLE_GROUP", "Between variables, constants, etc."),
                Pair("LINES_AFTER_VARIABLE_GROUP", "Below variables, constants, etc."),
                Pair("LINES_BEFORE_FUNC", "Before func declaration"),
            )
        ),
    );

    val WRAPPING_AND_BRACES: HashMap<String, HashMap<String, String>> = hashMapOf(
        Pair(
            "Code", hashMapOf(
                Pair("ALIGN_ASSIGNMENTS", "Align assignments"),
                Pair("ALIGN_AFTER_ASSIGNMENTS", "Align after assignments"),
            ),
        ),
        Pair(
            "Comments", hashMapOf(
                Pair("ALIGN_COMMENTS", "Align"),
            ),
        ),
    );

    fun HashMap<String, HashMap<String, String>>.consume(consumer: CodeStyleSettingsCustomizable) {
        this.forEach { (group, fields) ->
            fields.forEach { (field, title) ->
                consumer.showCustomOption(GdCodeStyleSettings::class.java, field, title, group)
            }
        }
    }

}
