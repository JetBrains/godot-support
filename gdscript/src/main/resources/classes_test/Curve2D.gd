extends Resource
class_name Curve2D

var bake_interval: float;

func add_point(position: Vector2, in: Vector2, out: Vector2, at_position: int) -> void:
    pass;
func clear_points() -> void:
    pass;
func get_baked_length() -> float:
    pass;
func get_baked_points() -> PackedVector2Array:
    pass;
func get_closest_offset(to_point: Vector2) -> float:
    pass;
func get_closest_point(to_point: Vector2) -> Vector2:
    pass;
func get_point_count() -> int:
    pass;
func get_point_in(idx: int) -> Vector2:
    pass;
func get_point_out(idx: int) -> Vector2:
    pass;
func get_point_position(idx: int) -> Vector2:
    pass;
func interpolate(idx: int, t: float) -> Vector2:
    pass;
func interpolate_baked(offset: float, cubic: bool) -> Vector2:
    pass;
func interpolatef(fofs: float) -> Vector2:
    pass;
func remove_point(idx: int) -> void:
    pass;
func set_point_in(idx: int, position: Vector2) -> void:
    pass;
func set_point_out(idx: int, position: Vector2) -> void:
    pass;
func set_point_position(idx: int, position: Vector2) -> void:
    pass;
func tessellate(max_stages: int, tolerance_degrees: float) -> PackedVector2Array:
    pass;
