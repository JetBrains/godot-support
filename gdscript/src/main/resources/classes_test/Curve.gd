extends Resource
class_name Curve
const TANGENT_FREE = 0;
const TANGENT_LINEAR = 1;
const TANGENT_MODE_COUNT = 2;

var bake_resolution: int;
var max_value: float;
var min_value: float;

func add_point(position: Vector2, left_tangent: float, right_tangent: float, left_mode: int, right_mode: int) -> int:
    pass;
func bake() -> void:
    pass;
func clean_dupes() -> void:
    pass;
func clear_points() -> void:
    pass;
func get_point_count() -> int:
    pass;
func get_point_left_mode(index: int) -> int:
    pass;
func get_point_left_tangent(index: int) -> float:
    pass;
func get_point_position(index: int) -> Vector2:
    pass;
func get_point_right_mode(index: int) -> int:
    pass;
func get_point_right_tangent(index: int) -> float:
    pass;
func interpolate(offset: float) -> float:
    pass;
func interpolate_baked(offset: float) -> float:
    pass;
func remove_point(index: int) -> void:
    pass;
func set_point_left_mode(index: int, mode: int) -> void:
    pass;
func set_point_left_tangent(index: int, tangent: float) -> void:
    pass;
func set_point_offset(index: int, offset: float) -> int:
    pass;
func set_point_right_mode(index: int, mode: int) -> void:
    pass;
func set_point_right_tangent(index: int, tangent: float) -> void:
    pass;
func set_point_value(index: int, y: float) -> void:
    pass;
