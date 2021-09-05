extends AnimationRootNode
class_name AnimationNodeBlendSpace2D

const BLEND_MODE_INTERPOLATED = 0;
const BLEND_MODE_DISCRETE = 1;
const BLEND_MODE_DISCRETE_CARRY = 2;

var auto_triangles: bool setget set_auto_triangles, get_auto_triangles;
var blend_mode: int setget set_blend_mode, get_blend_mode;
var max_space: Vector2 setget set_max_space, get_max_space;
var min_space: Vector2 setget set_min_space, get_min_space;
var snap: Vector2 setget set_snap, get_snap;
var x_label: String setget set_x_label, get_x_label;
var y_label: String setget set_y_label, get_y_label;

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

