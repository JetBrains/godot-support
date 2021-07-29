package gdscript

object GdKeywords {

    const val EXTENDS = "extends"
    const val CLASS_NAME = "class_name"
    const val TOOL = "tool"
    const val FUNC = "func"
    const val CONST = "const"
    const val VAR = "var"
    const val SETGET = "setget"
    const val SELF = "self"

    const val TRUE = "true"
    const val FALSE = "false"
    const val NULL = "null"
    val LITERALS = arrayOf(TRUE, FALSE, NULL);

    const val INT = "int"
    const val STR = "String"
    val BUILT_TYPES = arrayOf(INT, STR);
    const val VOID = "void"

    val ANNOTATIONS = arrayOf("export", "onready");

}