extends Node
class_name NavigationAgent2D

var max_neighbors: int;
var max_speed: float;
var neighbor_dist: float;
var path_max_distance: float;
var radius: float;
var target_desired_distance: float;
var time_horizon: float;

func distance_to_target() -> float:
    pass;
func get_final_location() -> Vector2:
    pass;
func get_nav_path() -> PackedVector2Array:
    pass;
func get_nav_path_index() -> int:
    pass;
func get_next_location() -> Vector2:
    pass;
func get_rid() -> RID:
    pass;
func get_target_location() -> Vector2:
    pass;
func is_navigation_finished() -> bool:
    pass;
func is_target_reachable() -> bool:
    pass;
func is_target_reached() -> bool:
    pass;
func set_target_location(location: Vector2) -> void:
    pass;
func set_velocity(velocity: Vector2) -> void:
    pass;
