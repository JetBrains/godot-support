package gdscript.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import gdscript.GdScriptBundle
import javax.swing.Icon

class GdColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon {
        return RiderPluginsGodotCommunityIcons.GDScript
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return GdSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """extends BaseClass
class_name MyClass, "res://path/to/optional/icon.svg"


var a = ${"$"}NodePath
var s = "Hello"
var arr = [1, 2, 3]
var dict = {"key": "value", 2: 3}
@export var typed_var: int
@export var inferred_type := "String"

const ANSWER = 42
const THE_NAME = "Charly"

var from_autoload = <global_var_autoload>Autoloaded</global_var_autoload>.foo()
var from_global = <global_var_builtin>Engine</global_var_builtin>.max_fps

enum Named {THING_1, THING_2, ANOTHER_THING = -1}

var v2 = Vector2(1, 2)

# DANGER: danger comment
# WARN: warning comment
# NOTE: note comment
## Documentation comment
func _init():
    print("Constructed!")
    var local_var = 5

    if param1 < local_var:
        print(param1)
    elif param2 > 5:
        print(param2)
    else:
        pass

    for i in range(20):
        print(i)

    while param2 != 0:
        param2 -= 1

    var local_var2 = param1 + 3
    return local_var2


class Something:
    var a = 10

"""
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        return TAGS
    }

    private val TAGS = mapOf(
        "global_var_autoload" to GdHighlighterColors.GLOBAL_VARIABLE_AUTOLOAD,
        "global_var_builtin" to GdHighlighterColors.GLOBAL_VARIABLE_BUILT_IN,
    )

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "GdScript"
    }

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.keywords"), GdHighlighterColors.KEYWORD),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.method.declaration"), GdHighlighterColors.METHOD_DECLARATION),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.method.call"), GdHighlighterColors.METHOD_CALL),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.static.method.call"), GdHighlighterColors.STATIC_METHOD_CALL),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.flow.control"), GdHighlighterColors.FLOW_KEYWORDS),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.comments"), GdHighlighterColors.COMMENT),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.doc.comments"), GdHighlighterColors.DOC_COMMENT),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.critical.comments"), GdHighlighterColors.DANGER),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.warning.comments"), GdHighlighterColors.WARNING),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.note.comments"), GdHighlighterColors.NOTE),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.strings"), GdHighlighterColors.STRING),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.annotations"), GdHighlighterColors.ANNOTATION),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.node.path"), GdHighlighterColors.NODE_STRING_PATH),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.node.reference"), GdHighlighterColors.NODE_PATH),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.base.type"), GdHighlighterColors.BASE_TYPE),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.engine.type"), GdHighlighterColors.ENGINE_TYPE),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.user.type"), GdHighlighterColors.CLASS_TYPE),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.numbers"), GdHighlighterColors.NUMBER),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.global.function"), GdHighlighterColors.GLOBAL_FUNCTION),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.global.variable.autoload"), GdHighlighterColors.GLOBAL_VARIABLE_AUTOLOAD),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.global.variable.built-in"), GdHighlighterColors.GLOBAL_VARIABLE_BUILT_IN),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.string.names"), GdHighlighterColors.STRING_NAME),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.string.format.marks"), GdHighlighterColors.STRING_FORMAT),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.variable.reference"), GdHighlighterColors.MEMBER),
            AttributesDescriptor(GdScriptBundle.message("settings.highlighter.errors"), GdHighlighterColors.BAD_CHARACTER),
        )
    }
}
