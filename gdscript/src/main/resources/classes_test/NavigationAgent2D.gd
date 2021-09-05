extends Node
class_name NavigationAgent2D


var max_neighbors: int setget set_max_neighbors, get_max_neighbors;
var max_speed: float setget set_max_speed, get_max_speed;
var neighbor_dist: float setget set_neighbor_dist, get_neighbor_dist;
var path_max_distance: float setget set_path_max_distance, get_path_max_distance;
var radius: float setget set_radius, get_radius;
var target_desired_distance: float setget set_target_desired_distance, get_target_desired_distance;
var time_horizon: float setget set_time_horizon, get_time_horizon;

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

