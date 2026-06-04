package gdscript.formatter.settings

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.openapi.application.ApplicationBundle
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.WRAP_VALUES
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizableOptions
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.GdScriptBundle
import gdscript.formatter.GdCodeStyleSettings

private val SETTINGS_CLASS = GdCodeStyleSettings::class.java
private val CLASS_HEADER_STYLE_LABELS: Array<String> =
    GdCodeStyleSettings.ClassHeaderStyle.entries.map { it.presentableText }.toTypedArray()
private val CLASS_HEADER_STYLE_VALUES: IntArray =
    GdCodeStyleSettings.ClassHeaderStyle.entries.map { it.ordinal }.toIntArray()

class GdLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language {
        return GdLanguage
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return IndentOptionsEditor()
    }

    override fun customizeDefaults(commonSettings: CommonCodeStyleSettings, indentOptions: CommonCodeStyleSettings.IndentOptions) {
        indentOptions.USE_TAB_CHARACTER = true
        indentOptions.INDENT_SIZE = 4
        commonSettings.METHOD_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_AS_NEEDED
        commonSettings.CALL_PARAMETERS_WRAP = CommonCodeStyleSettings.WRAP_AS_NEEDED
        super.customizeDefaults(commonSettings, indentOptions)
    }

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings =
        GdCodeStyleSettings(settings)

    override fun customizeSettings(
        consumer: CodeStyleSettingsCustomizable,
        settingsType: SettingsType,
    ) {
        val options = CodeStyleSettingsCustomizableOptions.getInstance()
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_BEFORE_METHOD_PARENTHESES",
                    "SPACE_BEFORE_METHOD_CALL_PARENTHESES",
                    "SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES",
                    "SPACE_WITHIN_METHOD_CALL_PARENTHESES",
                    "SPACE_WITHIN_EMPTY_METHOD_PARENTHESES",
                    "SPACE_WITHIN_METHOD_PARENTHESES",
                    "SPACE_WITHIN_BRACES",
                    "SPACE_WITHIN_BRACKETS",
                    "SPACE_AFTER_COMMA",
                    "SPACE_BEFORE_COMMA",
                    "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                    "SPACE_AROUND_ADDITIVE_OPERATORS",
                    "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
                    "SPACE_AROUND_SHIFT_OPERATORS",
                    "SPACE_AROUND_BITWISE_OPERATORS",
                    "SPACE_AROUND_RELATIONAL_OPERATORS",
                )
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_BEFORE_SEMICOLON", GdScriptBundle.message("settings.formatter.space.before.semicolon"), options.SPACES_OTHER)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_BEFORE_COLON", GdScriptBundle.message("settings.formatter.space.before.colon"), options.SPACES_OTHER)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_AFTER_COLON", GdScriptBundle.message("settings.formatter.space.after.colon"), options.SPACES_OTHER)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_AROUND_BACKSLASH", GdScriptBundle.message("settings.formatter.space.around.backslash"), options.SPACES_OTHER)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_BEFORE_LAMBDA_PARENTHESES", GdScriptBundle.message("settings.formatter.space.lambda.parentheses"), options.SPACES_BEFORE_PARENTHESES)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_BEFORE_LBRACKET", GdScriptBundle.message("settings.formatter.space.left.bracket"), options.SPACES_BEFORE_PARENTHESES)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_AROUND_EQ_IN_NAMED_PARAMETER", GdScriptBundle.message("settings.formatter.space.around.eq.named.parameter"), options.SPACES_AROUND_OPERATORS)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_AROUND_ARROW_IN_METHOD_RETURN", GdScriptBundle.message("settings.formatter.space.around.arrow.method.return"), options.SPACES_AROUND_OPERATORS)
                consumer.showCustomOption(SETTINGS_CLASS, "SPACE_AROUND_EQ_IN_LUA_STYLE_DICTS", GdScriptBundle.message("settings.formatter.space.around.eq.lua.dicts"), options.SPACES_AROUND_OPERATORS)
            }

            SettingsType.INDENT_SETTINGS -> {
                consumer.showAllStandardOptions()
            }

            SettingsType.BLANK_LINES_SETTINGS -> {
                consumer.showCustomOption(SETTINGS_CLASS, "MIN_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS", GdScriptBundle.message("settings.formatter.blank.lines.around.top.level"), options.BLANK_LINES)
                consumer.showCustomOption(SETTINGS_CLASS, "MIN_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS", GdScriptBundle.message("settings.formatter.blank.lines.around.inner"), options.BLANK_LINES)
                consumer.showCustomOption(SETTINGS_CLASS, "MIN_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS", GdScriptBundle.message("settings.formatter.blank.lines.before.first.method"), options.BLANK_LINES)
                consumer.showCustomOption(SETTINGS_CLASS, "MIN_LINES_AFTER_HEADER", GdScriptBundle.message("settings.formatter.blank.lines.min.after.header"), options.BLANK_LINES)
                consumer.showCustomOption(SETTINGS_CLASS, "MAX_BLANK_LINES_AROUND_TOP_LEVEL_CLASSES_FUNCTIONS", GdScriptBundle.message("settings.formatter.blank.lines.around.top.level"), options.BLANK_LINES_KEEP)
                consumer.showCustomOption(SETTINGS_CLASS, "MAX_BLANK_LINES_AROUND_INNER_CLASSES_FUNCTIONS", GdScriptBundle.message("settings.formatter.blank.lines.around.inner"), options.BLANK_LINES_KEEP)
                consumer.showCustomOption(SETTINGS_CLASS, "MAX_BLANK_LINES_BEFORE_FIRST_METHOD_IN_CLASS", GdScriptBundle.message("settings.formatter.blank.lines.before.first.method"), options.BLANK_LINES_KEEP)
                consumer.showCustomOption(SETTINGS_CLASS, "MAX_LINES_AFTER_HEADER", GdScriptBundle.message("settings.formatter.blank.lines.max.after.header"), options.BLANK_LINES_KEEP)
                consumer.showCustomOption(SETTINGS_CLASS, "MAX_LINES_IN_BETWEEN_VARIABLE_GROUPS", GdScriptBundle.message("settings.formatter.blank.lines.between.variable.groups"), options.BLANK_LINES_KEEP)
                consumer.showCustomOption(SETTINGS_CLASS, "LINES_WITHIN_SUITE", GdScriptBundle.message("settings.formatter.blank.lines.within.suite"), options.BLANK_LINES_KEEP)
            }

            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                    "CALL_PARAMETERS_WRAP",
                    "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE",
                    "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE",
                    "METHOD_PARAMETERS_WRAP",
                    "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
                    "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE",
                )
                consumer.showCustomOption(
                    SETTINGS_CLASS,
                    "CLASS_HEADER_STYLE",
                    GdScriptBundle.message("settings.formatter.wrap.class.header"),
                    GdScriptBundle.message("settings.formatter.group.class.header"),
                    CLASS_HEADER_STYLE_LABELS,
                    CLASS_HEADER_STYLE_VALUES,
                )
                val dictionaryLiterals = GdScriptBundle.message("settings.formatter.group.dictionary.literals")
                consumer.showCustomOption(SETTINGS_CLASS, "DICT_WRAPPING", dictionaryLiterals, null, options.WRAP_OPTIONS, WRAP_VALUES)
                consumer.showCustomOption(SETTINGS_CLASS, "DICT_NEW_LINE_AFTER_LEFT_BRACE", ApplicationBundle.message("wrapping.new.line.after.lbrace"), dictionaryLiterals)
                consumer.showCustomOption(SETTINGS_CLASS, "DICT_NEW_LINE_BEFORE_RIGHT_BRACE", ApplicationBundle.message("wrapping.rbrace.on.new.line"), dictionaryLiterals)
                val arrayLiterals = GdScriptBundle.message("settings.formatter.group.array.literals")
                consumer.showCustomOption(SETTINGS_CLASS, "ARRAY_WRAPPING", arrayLiterals, null, options.WRAP_OPTIONS, WRAP_VALUES)
                consumer.showCustomOption(SETTINGS_CLASS, "ARRAY_NEW_LINE_AFTER_LEFT_BRACKET", ApplicationBundle.message("wrapping.new.line.after.lbracket"), arrayLiterals)
                consumer.showCustomOption(SETTINGS_CLASS, "ARRAY_NEW_LINE_BEFORE_RIGHT_BRACKET", ApplicationBundle.message("wrapping.rbracket.on.new.line"), arrayLiterals)
                consumer.showCustomOption(SETTINGS_CLASS, "NEW_LINE_AFTER_COLON", GdScriptBundle.message("settings.formatter.wrap.new.line.after.colon"), GdScriptBundle.message("settings.formatter.group.single.clause.statements"))
                consumer.showCustomOption(SETTINGS_CLASS, "HANG_CLOSING_BRACKETS", GdScriptBundle.message("settings.formatter.wrap.hang.closing.bracket"), GdScriptBundle.message("settings.formatter.group.code"))
            }

            SettingsType.LANGUAGE_SPECIFIC -> {
                // TODO: Updates to GdEnterHandler have to be made for this to work
                // consumer.showCustomOption(SETTINGS_CLASS, "PARENTHESISE_ON_ENTER", GdScriptBundle.message("settings.formatter.code.insight.parenthesise.on.enter"), GdScriptBundle.message("settings.formatter.group.code.insight"))
                val continuationIndents = GdScriptBundle.message("settings.formatter.group.continuation.indents")
                consumer.showCustomOption(SETTINGS_CLASS, "USE_CONTINUATION_INDENT_FOR_COLLECTIONS", GdScriptBundle.message("settings.formatter.continuation.indent.collections"), continuationIndents)
                consumer.showCustomOption(SETTINGS_CLASS, "USE_CONTINUATION_INDENT_FOR_PARAMETERS", GdScriptBundle.message("settings.formatter.continuation.indent.parameters"), continuationIndents)
                consumer.showCustomOption(SETTINGS_CLASS, "USE_CONTINUATION_INDENT_FOR_ARGUMENTS", GdScriptBundle.message("settings.formatter.continuation.indent.arguments"), continuationIndents)
            }

            SettingsType.COMMENTER_SETTINGS -> {}
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String = when (settingsType) {
        SettingsType.SPACING_SETTINGS -> SPACING_SAMPLE
        SettingsType.BLANK_LINES_SETTINGS -> BLANK_LINES_SAMPLE
        SettingsType.WRAPPING_AND_BRACES_SETTINGS -> WRAPPING_SAMPLE
        SettingsType.INDENT_SETTINGS -> INDENT_SAMPLE
        SettingsType.LANGUAGE_SPECIFIC -> LANGUAGE_SPECIFIC_SAMPLE
        SettingsType.COMMENTER_SETTINGS -> GENERAL_SAMPLE
    }
}

private val SPACING_SAMPLE = """extends Node
    |
    |func compute(a: int, b: int = 2) -> int:
    |	var total = a + b * 2 - 1 << 3 & 1
    |	var flag := total > 0 and total < 10
    |	var values := [1, 2, 3]
    |	var first = values[0]
    |	var size := {"width": 800, "height": 600}
    |	var config := {scale = 1.5, visible = true}
    |	var doubled = values.map(func(x): return x * 2)
    |	var wide = a +\
    |		b
    |	compute(a, b)
    |	empty()
    |	total += 1;flag = false
    |	return total
    |
    |	func empty(): pass
    |
""".trimMargin()

private val BLANK_LINES_SAMPLE = """class_name Demo
    |extends Node
    |
    |const MAX = 10
    |const MIN = 0
    |
    |var health := 100
    |var mana := 50
    |
    |func _ready():
    |	var x = 1
    |	var y = 2
    |
    |
    |
    |	print(x + y)
    |
    |func update():
    |	pass
    |
    |class Inner:
    |	var value := 0
    |
    |	func reset():
    |		value = 0
    |	func tick():
    |		value += 1
""".trimMargin()

private val WRAPPING_SAMPLE = """class_name Wrapping
    |extends Node
    |
    |func configure(fst_arg: int, second_argument: int, third_argument: int, fourth_argument: int) -> void:
    |	pass
    |
    |func _ready():
    |	configure(fst_arg, second_argument, third_argument, fourth_argument)
    |	var data := {"alpha": 1, "beta": 2, "gamma": 3, "delta": 4, "epsilon": 5}
    |	var items := [100, 200, 300, 400, 500, 600, 700, 800]
    |	if items.is_empty(): return
    |	for item in items: print(item)
""".trimMargin()

private val INDENT_SAMPLE = """extends Node
    |
    |func process(items):
    |	for item in items:
    |		if item > 0:
    |			print(item)
    |	var matrix := [
    |		[1, 2, 3],
    |		[4, 5, 6],
    |	]
    |	var config := {
    |		"width": 800,
    |		"height": 600,
    |	}
""".trimMargin()

private val LANGUAGE_SPECIFIC_SAMPLE = """extends Node
    |
    |func build(first_argument: int, second_argument: int,
    |		third_argument: int) -> Array:
    |	return [
    |		first_argument,
    |		second_argument,
    |		third_argument,
    |	]
    |
    |func _ready():
    |	build(
    |		1,
    |		2,
    |		3,
    |	)
""".trimMargin()

private val GENERAL_SAMPLE = """class_name MyClass
    |extends Node2D
    |
    |@export
    |var vari: int = 1 # Comment
    |var var2: String = "" # Comment
    |const cc = "cc"
    |
    |func fn1():
    |	pass
    |
    |
    |func fn2():
    |	pass
""".trimMargin()
