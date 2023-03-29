package gdscript

import gdscript.model.AnnotationParam

object GdKeywords {

    const val EXTENDS = "extends"
    const val CLASS = "class"
    const val SIGNAL = "signal"
    const val CLASS_NAME = "class_name"
    const val FUNC = "func"
    const val STATIC = "static"
    const val VARARG = "vararg"
    const val MASTER = "master"
    const val PUPPET = "puppet"
    const val REMOTE = "remote"
    const val REMOTE_SYNC = "remotesync"
    const val CONST = "const"
    const val VAR = "var"
    const val SELF = "self"
    const val SUPER = "super"

    /** Constants */
    const val TRUE = "true"
    const val FALSE = "false"
    const val NULL = "null"
    const val NAN = "NAN"
    const val INF = "INF"
    const val TAU = "TAU"
    const val PI = "PI"

    /** Return types */
    const val INT = "int"
    const val FLOAT = "float"
    const val BOOL = "bool"
    const val STR = "String"
    const val STR_NAME = "StringName"
    val BUILT_TYPES = arrayOf(INT, STR, FLOAT, BOOL);
    const val VOID = "void"
    const val CALLABLE = "Callable"

    /** Prefixes */
    const val RESOURCE_PREFIX = "res://"

    const val GLOBAL_SCOPE = "_GlobalScope"
    const val GLOBAL_GD_SCRIPT = "_GdScript"

    const val INIT_METHOD = "_init";

    /** Flow Types */
    const val FLOW_RETURN = "return";

    val LITERALS = arrayOf(TRUE, FALSE, NULL);
    val LITERAL_TYPES = arrayOf(BOOL, BOOL, NULL);

    /** Annotations */
    val ANNOTATION_TOOL = "tool";
    val ANNOTATION_EXPORT = "export";
    val ANNOTATION_ONREADY = "onready";
    val ANNOTATION_ICON = "icon";
    val ANNOTATION_EXPORT_ENUM = "export_enum";
    val ANNOTATION_EXPORT_FILE = "export_file";
    val ANNOTATION_EXPORT_DIR = "export_dir";
    val ANNOTATION_EXPORT_GLOBAL_FILE = "export_global_file";
    val ANNOTATION_EXPORT_GLOBAL_DIR = "export_global_dir";
    val ANNOTATION_EXPORT_MULTILINE = "export_multiline";
    val ANNOTATION_EXPORT_PLACEHOLDER = "export_placeholder";
    val ANNOTATION_EXPORT_RANGE = "export_range";
    val ANNOTATION_EXPORT_EXP_RANGE = "export_exp_range";
    val ANNOTATION_EXPORT_EXP_EASING = "export_exp_easing";
    val ANNOTATION_EXPORT_COLOR_NO_ALPHA = "export_color_no_alpha";
    val ANNOTATION_EXPORT_NODE_PATH = "export_node_path";
    val ANNOTATION_EXPORT_FLAGS = "export_flags";
    val ANNOTATION_EXPORT_FLAGS_2D_RENDER = "export_flags_2d_render";
    val ANNOTATION_EXPORT_FLAGS_2D_PHYSICS = "export_flags_2d_physics";
    val ANNOTATION_EXPORT_FLAGS_3D_RENDER = "export_flags_3d_render";
    val ANNOTATION_EXPORT_FLAGS_3D_PHYSICS = "export_flags_3d_physics";

    val ANNOTATIONS_ALL = arrayOf(
        ANNOTATION_TOOL,
        ANNOTATION_EXPORT,
        ANNOTATION_ONREADY,
        ANNOTATION_ICON,
        ANNOTATION_EXPORT_ENUM,
        ANNOTATION_EXPORT_FILE,
        ANNOTATION_EXPORT_DIR,
        ANNOTATION_EXPORT_GLOBAL_FILE,
        ANNOTATION_EXPORT_GLOBAL_DIR,
        ANNOTATION_EXPORT_MULTILINE,
        ANNOTATION_EXPORT_PLACEHOLDER,
        ANNOTATION_EXPORT_RANGE,
        ANNOTATION_EXPORT_EXP_RANGE,
        ANNOTATION_EXPORT_EXP_EASING,
        ANNOTATION_EXPORT_COLOR_NO_ALPHA,
        ANNOTATION_EXPORT_NODE_PATH,
        ANNOTATION_EXPORT_FLAGS,
        ANNOTATION_EXPORT_FLAGS_2D_RENDER,
        ANNOTATION_EXPORT_FLAGS_2D_PHYSICS,
        ANNOTATION_EXPORT_FLAGS_3D_RENDER,
        ANNOTATION_EXPORT_FLAGS_3D_PHYSICS,
//        "master",
//        "puppet",
//        "remote",
//        "mastersync",
//        "puppetsync",
//        "remotesync",
    );

    val ANNOTATIONS_ROOT_ONLY = arrayOf(
        ANNOTATION_TOOL,
        ANNOTATION_ICON,
    );

    val ANNOTATIONS_STAND_ALONE: HashMap<String, Array<AnnotationParam>> = hashMapOf(
        ANNOTATION_TOOL to arrayOf(AnnotationParam("path", STR))
    );

    val ANNOTATIONS_PARAMETRIZED: HashMap<String, Array<AnnotationParam>> = hashMapOf(
        ANNOTATION_ICON to arrayOf(AnnotationParam("path", STR))
    );

}
