extends PhysicsBody3D
class_name CharacterBody3D


var collision/safe_margin: float setget set_safe_margin, get_safe_margin;
var floor_max_angle: float setget set_floor_max_angle, get_floor_max_angle;
var infinite_inertia: bool setget set_infinite_inertia_enabled, is_infinite_inertia_enabled;
var linear_velocity: Vector3 setget set_linear_velocity, get_linear_velocity;
var max_slides: int setget set_max_slides, get_max_slides;
var snap: Vector3 setget set_snap, get_snap;
var stop_on_slope: bool setget set_stop_on_slope_enabled, is_stop_on_slope_enabled;
var up_direction: Vector3 setget set_up_direction, get_up_direction;

func get_floor_normal() -> Vector3:
    pass;

func get_floor_velocity() -> Vector3:
    pass;

func get_slide_collision(slide_idx: int) -> KinematicCollision3D:
    pass;

func get_slide_count() -> int:
    pass;

func is_on_ceiling() -> bool:
    pass;

func is_on_floor() -> bool:
    pass;

func is_on_wall() -> bool:
    pass;

func move_and_slide() -> void:
    pass;

