extends AnimationRootNode
class_name AnimationNodeBlendSpace2D
const BLEND_MODE_INTERPOLATED = 0;
const BLEND_MODE_DISCRETE = 1;
const BLEND_MODE_DISCRETE_CARRY = 2;

var auto_triangles: bool;
var blend_mode: int;
var max_space: Vector2;
var min_space: Vector2;
var snap: Vector2;
var x_label: String;
var y_label: String;

func add_blend_point(node: AnimationRootNode, pos: Vector2, at_index: int) -> void:
    pass;
func add_triangle(x: int, y: int, z: int, at_index: int) -> void:
    pass;
func get_blend_point_count() -> int:
    pass;
func get_blend_point_node(point: int) -> AnimationRootNode:
    pass;
func get_blend_point_position(point: int) -> Vector2:
    pass;
func get_triangle_count() -> int:
    pass;
func get_triangle_point(triangle: int, point: int) -> int:
    pass;
func remove_blend_point(point: int) -> void:
    pass;
func remove_triangle(triangle: int) -> void:
    pass;
func set_blend_point_node(point: int, node: AnimationRootNode) -> void:
    pass;
func set_blend_point_position(point: int, pos: Vector2) -> void:
    pass;
