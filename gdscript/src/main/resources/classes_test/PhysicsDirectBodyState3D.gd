extends Object
class_name PhysicsDirectBodyState3D


var angular_velocity: Vector3 setget set_angular_velocity, get_angular_velocity;
var center_of_mass: Vector3 setget , get_center_of_mass;
var inverse_inertia: Vector3 setget , get_inverse_inertia;
var inverse_mass: float setget , get_inverse_mass;
var linear_velocity: Vector3 setget set_linear_velocity, get_linear_velocity;
var principal_inertia_axes: Basis setget , get_principal_inertia_axes;
var sleeping: bool setget set_sleep_state, is_sleeping;
var step: float setget , get_step;
var total_angular_damp: float setget , get_total_angular_damp;
var total_gravity: Vector3 setget , get_total_gravity;
var total_linear_damp: float setget , get_total_linear_damp;
var transform: Transform3D setget set_transform, get_transform;

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

func get_contact_collider(contact_idx: int) -> RID:
    pass;

func get_contact_collider_id(contact_idx: int) -> int:
    pass;

func get_contact_collider_object(contact_idx: int) -> Object:
    pass;

func get_contact_collider_position(contact_idx: int) -> Vector3:
    pass;

func get_contact_collider_shape(contact_idx: int) -> int:
    pass;

func get_contact_collider_velocity_at_position(contact_idx: int) -> Vector3:
    pass;

func get_contact_count() -> int:
    pass;

func get_contact_impulse(contact_idx: int) -> float:
    pass;

func get_contact_local_normal(contact_idx: int) -> Vector3:
    pass;

func get_contact_local_position(contact_idx: int) -> Vector3:
    pass;

func get_contact_local_shape(contact_idx: int) -> int:
    pass;

func get_space_state() -> PhysicsDirectSpaceState3D:
    pass;

func integrate_forces() -> void:
    pass;

