package gdscript.formatter

import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.WRAP_AS_NEEDED
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.util.ui.PresentableEnum
import gdscript.GdScriptBundle

class GdCodeStyleSettings(container: CodeStyleSettings) :
    CustomCodeStyleSettings(GdCodeStyleSettings::class.java.getSimpleName(), container) {

    enum class ClassHeaderStyle(private val key: String) : PresentableEnum {
        NONE("settings.formatter.class.header.style.none"),
        FORCE_SEPARATE("settings.formatter.class.header.style.force.separate"),
        FORCE_COMBINED("settings.formatter.class.header.style.force.combined")
        ;

        override fun getPresentableText(): @NlsContexts.Label String {
            return GdScriptBundle.message(key)
        }
    }

    companion object {
        val CLASS_HEADER_STYLE_NONE: Int = ClassHeaderStyle.NONE.ordinal
        val CLASS_HEADER_STYLE_FORCE_SEPARATE: Int = ClassHeaderStyle.FORCE_SEPARATE.ordinal
        val CLASS_HEADER_STYLE_FORCE_COMBINED: Int = ClassHeaderStyle.FORCE_COMBINED.ordinal
    }

    /* Black lines */
    @JvmField
    var MIN_LINES_AFTER_HEADER: Int = 1
    @JvmField
    var MAX_LINES_AFTER_HEADER: Int = 2
    @JvmField
    var MAX_LINES_IN_BETWEEN_VARIABLE_GROUPS: Int = 1
    @JvmField
    var LINES_WITHIN_SUITE: Int = 1
    @JvmField
    var CLASS_HEADER_STYLE: Int = CLASS_HEADER_STYLE_NONE
    @JvmField
    var MAX_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS: Int = 2
    @JvmField
    var MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS: Int = 0
    @JvmField
    var MAX_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS: Int = 1
    @JvmField
    var MIN_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS: Int = 0
    @JvmField
    var MAX_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS: Int = 0
    @JvmField
    var MIN_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS: Int = 0

    /* Spacing */
    @JvmField
    var SPACE_BEFORE_SEMICOLON: Boolean = false
    @JvmField
    var SPACE_BEFORE_COLON: Boolean = false
    @JvmField
    var SPACE_AFTER_COLON: Boolean = true
    @JvmField
    var SPACE_BEFORE_LAMBDA_PARENTHESES: Boolean = false
    @JvmField
    var SPACE_BEFORE_LBRACKET: Boolean = false
    @JvmField
    var SPACE_AROUND_EQ_IN_NAMED_PARAMETER: Boolean = false
    @JvmField
    var SPACE_AROUND_EQ_IN_LUA_STYLE_DICTS: Boolean = true
    @JvmField
    var SPACE_AROUND_BACKSLASH: Boolean = true

    @JvmField
    var SPACE_AROUND_ARROW_IN_METHOD_RETURN: Boolean = true

    @CommonCodeStyleSettings.WrapConstant
    @JvmField
    var DICT_WRAPPING: Int = WRAP_AS_NEEDED
    @JvmField
    var DICT_NEW_LINE_AFTER_LEFT_BRACE: Boolean = false
    @JvmField
    var DICT_NEW_LINE_BEFORE_RIGHT_BRACE: Boolean = false

    @CommonCodeStyleSettings.WrapConstant
    @JvmField
    var ARRAY_WRAPPING: Int = WRAP_AS_NEEDED
    @JvmField
    var ARRAY_NEW_LINE_AFTER_LEFT_BRACKET: Boolean = false
    @JvmField
    var ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET: Boolean = false

    @JvmField
    var HANG_CLOSING_BRACKETS: Boolean = false
    @JvmField
    var NEW_LINE_AFTER_COLON: Boolean = false
    @JvmField
    var USE_CONTINUATION_INDENT_FOR_COLLECTIONS: Boolean = false
    @JvmField
    var USE_CONTINUATION_INDENT_FOR_PARAMETERS: Boolean = true
    @JvmField
    var USE_CONTINUATION_INDENT_FOR_ARGUMENTS: Boolean = true

    /* Code-insight (Enter handler), does not work at the moment */
    @JvmField
    var PARENTHESISE_ON_ENTER: Boolean = false
}