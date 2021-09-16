extends PhysicsBody2D
class_name CharacterBody2D

var collision/safe_margin: float;
var floor_max_angle: float;
var infinite_inertia: bool;
var linear_velocity: Vector2;
var max_slides: int;
var snap: Vector2;
var stop_on_slope: bool;
var up_direction: Vector2;

func get_floor_normal() -> Vector2:
    pass;
func get_floor_velocity() -> Vector2:
    pass;
func get_slide_collision(slide_idx: int) -> KinematicCollision2D:
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
