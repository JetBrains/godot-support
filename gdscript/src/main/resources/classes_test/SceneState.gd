extends RefCounted
class_name SceneState

const GEN_EDIT_STATE_DISABLED = 0;
const GEN_EDIT_STATE_INSTANCE = 1;
const GEN_EDIT_STATE_MAIN = 2;


func get_connection_binds(idx: int) -> Array:
    pass;

func get_connection_count() -> int:
    pass;

func get_connection_flags(idx: int) -> int:
    pass;

func get_connection_method(idx: int) -> StringName:
    pass;

func get_connection_signal(idx: int) -> StringName:
    pass;

func get_connection_source(idx: int) -> NodePath:
    pass;

func get_connection_target(idx: int) -> NodePath:
    pass;

func get_node_count() -> int:
    pass;

func get_node_groups(idx: int) -> PackedStringArray:
    pass;

func get_node_index(idx: int) -> int:
    pass;

func get_node_instance(idx: int) -> PackedScene:
    pass;

func get_node_instance_placeholder(idx: int) -> String:
    pass;

func get_node_name(idx: int) -> StringName:
    pass;

func get_node_owner_path(idx: int) -> NodePath:
    pass;

func get_node_path(idx: int, for_parent: bool) -> NodePath:
    pass;

func get_node_property_count(idx: int) -> int:
    pass;

func get_node_property_name(idx: int, prop_idx: int) -> StringName:
    pass;

func get_node_property_value(idx: int, prop_idx: int) -> Variant:
    pass;

func get_node_type(idx: int) -> StringName:
    pass;

func is_node_instance_placeholder(idx: int) -> bool:
    pass;

