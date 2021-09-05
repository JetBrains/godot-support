extends RefCounted
class_name EditorFeatureProfile

const FEATURE_3D = 0;
const FEATURE_SCRIPT = 1;
const FEATURE_ASSET_LIB = 2;
const FEATURE_SCENE_TREE = 3;
const FEATURE_NODE_DOCK = 4;
const FEATURE_FILESYSTEM_DOCK = 5;
const FEATURE_IMPORT_DOCK = 6;
const FEATURE_MAX = 7;


func get_feature_name(feature: int) -> String:
    pass;

func is_class_disabled(class_name: StringName) -> bool:
    pass;

func is_class_editor_disabled(class_name: StringName) -> bool:
    pass;

func is_class_property_disabled(class_name: StringName, property: StringName) -> bool:
    pass;

func is_feature_disabled(feature: int) -> bool:
    pass;

func load_from_file(path: String) -> int:
    pass;

func save_to_file(path: String) -> int:
    pass;

func set_disable_class(class_name: StringName, disable: bool) -> void:
    pass;

func set_disable_class_editor(class_name: StringName, disable: bool) -> void:
    pass;

func set_disable_class_property(class_name: StringName, property: StringName, disable: bool) -> void:
    pass;

func set_disable_feature(feature: int, disable: bool) -> void:
    pass;

