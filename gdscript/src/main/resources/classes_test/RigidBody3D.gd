extends PhysicsBody3D
class_name RigidBody3D
const MODE_DYNAMIC = 0;
const MODE_STATIC = 1;
const MODE_DYNAMIC_LOCKED = 2;
const MODE_KINEMATIC = 3;

var angular_damp: float;
var angular_velocity: Vector3;
var can_sleep: bool;
var contact_monitor: bool;
var contacts_reported: int;
var continuous_cd: bool;
var custom_integrator: bool;
var gravity_scale: float;
var linear_damp: float;
var linear_velocity: Vector3;
var mass: float;
var mode: int;
var physics_material_override: PhysicsMaterial;
var sleeping: bool;

func _integrate_forces(state: PhysicsDirectBodyState3D) -> void:
    pass;
func add_central_force(force: Vector3) -> void:
    pass;
func add_force(force: Vector3, position: Vector3) -> void:
    pass;
func add_torque(torque: Vector3) -> void:
    pass;
func apply_central_impulse(impulse: Vector3) -> void:
    pass;
func apply_impulse(impulse: Vector3, position: Vector3) -> void:
    pass;
func apply_torque_impulse(impulse: Vector3) -> void:
    pass;
func get_colliding_bodies() -> Array:
    pass;
func get_inverse_inertia_tensor() -> Basis:
    pass;
func set_axis_velocity(axis_velocity: Vector3) -> void:
    pass;
