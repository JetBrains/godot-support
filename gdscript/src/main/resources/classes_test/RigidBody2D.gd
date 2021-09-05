extends PhysicsBody2D
class_name RigidBody2D

const MODE_DYNAMIC = 0;
const MODE_STATIC = 1;
const MODE_DYNAMIC_LOCKED = 2;
const MODE_KINEMATIC = 3;
const CCD_MODE_DISABLED = 0;
const CCD_MODE_CAST_RAY = 1;
const CCD_MODE_CAST_SHAPE = 2;

var angular_damp: float setget set_angular_damp, get_angular_damp;
var angular_velocity: float setget set_angular_velocity, get_angular_velocity;
var applied_force: Vector2 setget set_applied_force, get_applied_force;
var applied_torque: float setget set_applied_torque, get_applied_torque;
var can_sleep: bool setget set_can_sleep, is_able_to_sleep;
var contact_monitor: bool setget set_contact_monitor, is_contact_monitor_enabled;
var contacts_reported: int setget set_max_contacts_reported, get_max_contacts_reported;
var continuous_cd: int setget set_continuous_collision_detection_mode, get_continuous_collision_detection_mode;
var custom_integrator: bool setget set_use_custom_integrator, is_using_custom_integrator;
var gravity_scale: float setget set_gravity_scale, get_gravity_scale;
var inertia: float setget set_inertia, get_inertia;
var linear_damp: float setget set_linear_damp, get_linear_damp;
var linear_velocity: Vector2 setget set_linear_velocity, get_linear_velocity;
var mass: float setget set_mass, get_mass;
var mode: int setget set_mode, get_mode;
var physics_material_override: PhysicsMaterial setget set_physics_material_override, get_physics_material_override;
var sleeping: bool setget set_sleeping, is_sleeping;

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

func get_colliding_bodies() -> Node2D[]:
    pass;

func set_axis_velocity(axis_velocity: Vector2) -> void:
    pass;

