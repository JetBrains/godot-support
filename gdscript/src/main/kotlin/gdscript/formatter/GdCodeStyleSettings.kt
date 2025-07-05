package gdscript.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class GdCodeStyleSettings(settings: CodeStyleSettings) : CustomCodeStyleSettings("GdCodeStyleSettings", settings) {

    /* Black lines */
    @JvmField var LINES_BEFORE_FUNC_MIN: Int = 2
    @JvmField var LINES_BEFORE_FUNC_MAX: Int = 2
    @JvmField var LINES_AFTER_HEADER: Int = 1
    @JvmField var LINES_IN_BETWEEN_VARIABLE_GROUP: Int = 0
    @JvmField var LINES_AFTER_VARIABLE_GROUP: Int = 0
    @JvmField var LINES_BETWEEN_EXPORT_GROUPS: Int = 1
    @JvmField var LINES_WITHIN_SUITE: Int = 1
    @JvmField var LINES_AROUND_MULTILINE_VAR: Int = 1

    /* Spacing */
    @JvmField var SPACE_BEFORE_COMMA: Boolean = false
    @JvmField var SPACE_AFTER_COMMA: Boolean = true
    @JvmField var SPACE_BEFORE_COLON: Boolean = false
    @JvmField var SPACE_AFTER_COLON: Boolean = true

    /* Wrapping and Spaces */
    @JvmField var ALIGN_COMMENTS: Boolean = true
    @JvmField var ALIGN_ASSIGNMENTS: Boolean = true
    @JvmField var ALIGN_AFTER_ASSIGNMENTS: Boolean = true
}
