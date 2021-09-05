extends AnimationRootNode
class_name AnimationNodeBlendSpace1D


var max_space: float setget set_max_space, get_max_space;
var min_space: float setget set_min_space, get_min_space;
var snap: float setget set_snap, get_snap;
var value_label: String setget set_value_label, get_value_label;

func add_blend_point(node: AnimationRootNode, pos: float, at_index: int) -> void:
    pass;

func get_blend_point_count() -> int:
    pass;

func get_blend_point_node(point: int) -> AnimationRootNode:
    pass;

func get_blend_point_position(point: int) -> float:
    pass;

func remove_blend_point(point: int) -> void:
    pass;

func set_blend_point_node(point: int, node: AnimationRootNode) -> void:
    pass;

func set_blend_point_position(point: int, pos: float) -> void:
    pass;

