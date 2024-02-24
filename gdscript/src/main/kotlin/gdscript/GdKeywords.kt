package gdscript

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
    const val MASTER_SYNC = "mastersync"
    const val PUPPET_SYNC = "puppetsync"
    const val CONST = "const"
    const val VAR = "var"
    const val SELF = "self"
    const val SUPER = "super"
    const val VARIANT = "Variant"

    /** Constants */
    const val TRUE = "true"
    const val FALSE = "false"
    const val NULL = "null"
    const val NAN = "NAN"
    const val INF = "INF"
    const val TAU = "TAU"
    const val PI = "PI"
    val MATH_CONSTANTS = setOf(PI, TAU, INF, NAN);

    /** Return types */
    const val INT = "int"
    const val FLOAT = "float"
    const val BOOL = "bool"
    const val STR = "String"
    const val ARRAY = "Array"
    const val DICTIONARY = "Dictionary"
    const val STR_NAME = "StringName"
    const val NODE_PATH = "NodePath"
    val BUILT_TYPES = setOf(INT, STR, FLOAT, BOOL, ARRAY, DICTIONARY);
    const val VOID = "void"
    const val CALLABLE = "Callable"

    /** Prefixes */
    const val RESOURCE_PREFIX = "res://"

    const val GLOBAL_SCOPE = "_GlobalScope"
    const val GLOBAL_GD_SCRIPT = "_GDScript"

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

    // TODO promazat - použít?
    val ANNOTATIONS_ROOT_ONLY = arrayOf(
        ANNOTATION_TOOL,
        ANNOTATION_ICON,
    );

}
