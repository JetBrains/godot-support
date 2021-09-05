extends PhysicsBody3D
class_name RigidBody3D

const MODE_DYNAMIC = 0;
const MODE_STATIC = 1;
const MODE_DYNAMIC_LOCKED = 2;
const MODE_KINEMATIC = 3;

var angular_damp: float setget set_angular_damp, get_angular_damp;
var angular_velocity: Vector3 setget set_angular_velocity, get_angular_velocity;
var can_sleep: bool setget set_can_sleep, is_able_to_sleep;
var contact_monitor: bool setget set_contact_monitor, is_contact_monitor_enabled;
var contacts_reported: int setget set_max_contacts_reported, get_max_contacts_reported;
var continuous_cd: bool setget set_use_continuous_collision_detection, is_using_continuous_collision_detection;
var custom_integrator: bool setget set_use_custom_integrator, is_using_custom_integrator;
var gravity_scale: float setget set_gravity_scale, get_gravity_scale;
var linear_damp: float setget set_linear_damp, get_linear_damp;
var linear_velocity: Vector3 setget set_linear_velocity, get_linear_velocity;
var mass: float setget set_mass, get_mass;
var mode: int setget set_mode, get_mode;
var physics_material_override: PhysicsMaterial setget set_physics_material_override, get_physics_material_override;
var sleeping: bool setget set_sleeping, is_sleeping;

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

