package gdscript.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import gdscript.GdIcon
import javax.swing.Icon

class GdColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon {
        return GdIcon.FILE
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

enum Named {THING_1, THING_2, ANOTHER_THING = -1}

var v2 = Vector2(1, 2)


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
        return null
    }

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
            AttributesDescriptor("Keywords", GdHighlighterColors.KEYWORD),
            AttributesDescriptor("Method declaration", GdHighlighterColors.METHOD_DECLARATION),
            AttributesDescriptor("Method call", GdHighlighterColors.METHOD_CALL),
            AttributesDescriptor("Flow control", GdHighlighterColors.FLOW_KEYWORDS),
            AttributesDescriptor("Comments", GdHighlighterColors.COMMENT),
            AttributesDescriptor("Strings", GdHighlighterColors.STRING),
            AttributesDescriptor("Annotations", GdHighlighterColors.ANNOTATION),
            AttributesDescriptor("Node path", GdHighlighterColors.NODE_STRING_PATH),
            AttributesDescriptor("Node reference", GdHighlighterColors.NODE_PATH),
            AttributesDescriptor("BaseType (Vector3, Node3D)", GdHighlighterColors.BASE_TYPE),
            AttributesDescriptor("EngineType (Vector3, Node3D)", GdHighlighterColors.ENGINE_TYPE),
            AttributesDescriptor("User type (class_name)", GdHighlighterColors.CLASS_TYPE),
            AttributesDescriptor("Numbers", GdHighlighterColors.NUMBER),


            AttributesDescriptor("Variables", GdHighlighterColors.IDENTIFIER),
            AttributesDescriptor("Member reference", GdHighlighterColors.MEMBER),
            AttributesDescriptor("Errors", GdHighlighterColors.BAD_CHARACTER),
        )
    }
}
