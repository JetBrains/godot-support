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
    const val NAN = "NAN"
    const val INF = "INF"
    const val TAU = "TAU"
    const val PI = "PI"

    const val INT = "int"
    const val FLOAT = "float"
    const val BOOL = "bool"
    const val STR = "String"
    val BUILT_TYPES = arrayOf(INT, STR, FLOAT, BOOL);
    const val VOID = "void"

    val ANNOTATIONS = arrayOf("export", "onready");

    val LITERALS = arrayOf(TRUE, FALSE, NULL, NAN, INF, TAU, PI);
    val LITERAL_TYPES = arrayOf(BOOL, BOOL, NULL, FLOAT, FLOAT, FLOAT, FLOAT);
}