extends RefCounted
class_name AStar2D


func _compute_cost(from_id: int, to_id: int) -> float:
    pass;
func _estimate_cost(from_id: int, to_id: int) -> float:
    pass;
func add_point(id: int, position: Vector2, weight_scale: float) -> void:
    pass;
func are_points_connected(id: int, to_id: int) -> bool:
    pass;
func clear() -> void:
    pass;
func connect_points(id: int, to_id: int, bidirectional: bool) -> void:
    pass;
func disconnect_points(id: int, to_id: int) -> void:
    pass;
func get_available_point_id() -> int:
    pass;
func get_closest_point(to_position: Vector2, include_disabled: bool) -> int:
    pass;
func get_closest_position_in_segment(to_position: Vector2) -> Vector2:
    pass;
func get_id_path(from_id: int, to_id: int) -> PackedInt32Array:
    pass;
func get_point_capacity() -> int:
    pass;
func get_point_connections(id: int) -> PackedInt32Array:
    pass;
func get_point_count() -> int:
    pass;
func get_point_path(from_id: int, to_id: int) -> PackedVector2Array:
    pass;
func get_point_position(id: int) -> Vector2:
    pass;
func get_point_weight_scale(id: int) -> float:
    pass;
func get_points() -> Array:
    pass;
func has_point(id: int) -> bool:
    pass;
func is_point_disabled(id: int) -> bool:
    pass;
func remove_point(id: int) -> void:
    pass;
func reserve_space(num_nodes: int) -> void:
    pass;
func set_point_disabled(id: int, disabled: bool) -> void:
    pass;
func set_point_position(id: int, position: Vector2) -> void:
    pass;
func set_point_weight_scale(id: int, weight_scale: float) -> void:
    pass;
