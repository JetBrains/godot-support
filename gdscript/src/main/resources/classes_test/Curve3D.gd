extends Resource
class_name Curve3D


var bake_interval: float setget set_bake_interval, get_bake_interval;
var up_vector_enabled: bool setget set_up_vector_enabled, is_up_vector_enabled;

func add_point(position: Vector3, in: Vector3, out: Vector3, at_position: int) -> void:
    pass;

func clear_points() -> void:
    pass;

func get_baked_length() -> float:
    pass;

func get_baked_points() -> PackedVector3Array:
    pass;

func get_baked_tilts() -> PackedFloat32Array:
    pass;

func get_baked_up_vectors() -> PackedVector3Array:
    pass;

func get_closest_offset(to_point: Vector3) -> float:
    pass;

func get_closest_point(to_point: Vector3) -> Vector3:
    pass;

func get_point_count() -> int:
    pass;

func get_point_in(idx: int) -> Vector3:
    pass;

func get_point_out(idx: int) -> Vector3:
    pass;

func get_point_position(idx: int) -> Vector3:
    pass;

func get_point_tilt(idx: int) -> float:
    pass;

func interpolate(idx: int, t: float) -> Vector3:
    pass;

func interpolate_baked(offset: float, cubic: bool) -> Vector3:
    pass;

func interpolate_baked_up_vector(offset: float, apply_tilt: bool) -> Vector3:
    pass;

func interpolatef(fofs: float) -> Vector3:
    pass;

func remove_point(idx: int) -> void:
    pass;

func set_point_in(idx: int, position: Vector3) -> void:
    pass;

func set_point_out(idx: int, position: Vector3) -> void:
    pass;

func set_point_position(idx: int, position: Vector3) -> void:
    pass;

func set_point_tilt(idx: int, tilt: float) -> void:
    pass;

func tessellate(max_stages: int, tolerance_degrees: float) -> PackedVector3Array:
    pass;

