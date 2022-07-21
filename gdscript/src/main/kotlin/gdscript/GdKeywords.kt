package gdscript

object GdKeywords {

    const val EXTENDS = "extends"
    const val CLASS_NAME = "class_name"
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

    val ANNOTATIONS = arrayOf("export", "onready", "icon", "tool", "master", "puppet", "remote", "mastersync", "puppetsync", "remotesync",
        "export_enum", "export_file", "export_dir", "export_global_file", "export_global_dir", "export_multiline", "export_placeholder",
        "export_range", "export_exp_range", "export_exp_easing", "export_color_no_alpha", "export_node_path", "export_flags",
        "export_flags_2d_render", "export_flags_2d_physics", "export_flags_3d_render", "export_flags_3d_physics");

    val ANNOTATIONS_PARAMETRIZED = arrayOf("icon");

    val LITERALS = arrayOf(TRUE, FALSE, NULL, NAN, INF, TAU, PI);
    val LITERAL_TYPES = arrayOf(BOOL, BOOL, NULL, FLOAT, FLOAT, FLOAT, FLOAT);
}