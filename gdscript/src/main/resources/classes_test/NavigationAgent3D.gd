extends Node
class_name NavigationAgent3D

var agent_height_offset: float;
var ignore_y: bool;
var max_neighbors: int;
var max_speed: float;
var neighbor_dist: float;
var path_max_distance: float;
var radius: float;
var target_desired_distance: float;
var time_horizon: float;

func distance_to_target() -> float:
    pass;
func get_final_location() -> Vector3:
    pass;
func get_nav_path() -> PackedVector3Array:
    pass;
func get_nav_path_index() -> int:
    pass;
func get_next_location() -> Vector3:
    pass;
func get_rid() -> RID:
    pass;
func get_target_location() -> Vector3:
    pass;
func is_navigation_finished() -> bool:
    pass;
func is_target_reachable() -> bool:
    pass;
func is_target_reached() -> bool:
    pass;
func set_target_location(location: Vector3) -> void:
    pass;
func set_velocity(velocity: Vector3) -> void:
    pass;
