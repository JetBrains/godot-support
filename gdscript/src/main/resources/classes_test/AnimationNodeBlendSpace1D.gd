extends AnimationRootNode
class_name AnimationNodeBlendSpace1D

var max_space: float;
var min_space: float;
var snap: float;
var value_label: String;

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
