extends Resource
class_name AnimationNode

const FILTER_IGNORE = 0;
const FILTER_PASS = 1;
const FILTER_STOP = 2;
const FILTER_BLEND = 3;

var filter_enabled: bool setget set_filter_enabled, is_filter_enabled;

func _get_caption() -> String:
    pass;

func _get_child_by_name(name: String) -> Object:
    pass;

func _get_child_nodes() -> Dictionary:
    pass;

func _get_parameter_default_value(name: StringName) -> Variant:
    pass;

func _get_parameter_list() -> Array:
    pass;

func _has_filter() -> bool:
    pass;

func _process(time: float, seek: bool) -> void:
    pass;

func add_input(name: String) -> void:
    pass;

func blend_animation(animation: StringName, time: float, delta: float, seeked: bool, blend: float) -> void:
    pass;

func blend_input(input_index: int, time: float, seek: bool, blend: float, filter: int, optimize: bool) -> float:
    pass;

func blend_node(name: StringName, node: AnimationNode, time: float, seek: bool, blend: float, filter: int, optimize: bool) -> float:
    pass;

func get_input_count() -> int:
    pass;

func get_input_name(input: int) -> String:
    pass;

func get_parameter(name: StringName) -> Variant:
    pass;

func is_path_filtered(path: NodePath) -> bool:
    pass;

func remove_input(index: int) -> void:
    pass;

func set_filter_path(path: NodePath, enable: bool) -> void:
    pass;

func set_parameter(name: StringName, value: Variant) -> void:
    pass;

