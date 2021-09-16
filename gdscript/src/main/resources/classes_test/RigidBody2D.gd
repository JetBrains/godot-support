extends PhysicsBody2D
class_name RigidBody2D
const MODE_DYNAMIC = 0;
const MODE_STATIC = 1;
const MODE_DYNAMIC_LOCKED = 2;
const MODE_KINEMATIC = 3;
const CCD_MODE_DISABLED = 0;
const CCD_MODE_CAST_RAY = 1;
const CCD_MODE_CAST_SHAPE = 2;

var angular_damp: float;
var angular_velocity: float;
var applied_force: Vector2;
var applied_torque: float;
var can_sleep: bool;
var contact_monitor: bool;
var contacts_reported: int;
var continuous_cd: int;
var custom_integrator: bool;
var gravity_scale: float;
var inertia: float;
var linear_damp: float;
var linear_velocity: Vector2;
var mass: float;
var mode: int;
var physics_material_override: PhysicsMaterial;
var sleeping: bool;

func _integrate_forces(state: PhysicsDirectBodyState2D) -> void:
    pass;
func add_central_force(force: Vector2) -> void:
    pass;
func add_force(force: Vector2, position: Vector2) -> void:
    pass;
func add_torque(torque: float) -> void:
    pass;
func apply_central_impulse(impulse: Vector2) -> void:
    pass;
func apply_impulse(impulse: Vector2, position: Vector2) -> void:
    pass;
func apply_torque_impulse(torque: float) -> void:
    pass;
func get_colliding_bodies() -> Array[Node2D]:
    pass;
func set_axis_velocity(axis_velocity: Vector2) -> void:
    pass;
