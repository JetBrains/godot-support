extends Object
class_name PhysicsDirectBodyState2D


var angular_velocity: float setget set_angular_velocity, get_angular_velocity;
var inverse_inertia: float setget , get_inverse_inertia;
var inverse_mass: float setget , get_inverse_mass;
var linear_velocity: Vector2 setget set_linear_velocity, get_linear_velocity;
var sleeping: bool setget set_sleep_state, is_sleeping;
var step: float setget , get_step;
var total_angular_damp: float setget , get_total_angular_damp;
var total_gravity: Vector2 setget , get_total_gravity;
var total_linear_damp: float setget , get_total_linear_damp;
var transform: Transform2D setget set_transform, get_transform;

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

func apply_torque_impulse(impulse: float) -> void:
    pass;

func get_contact_collider(contact_idx: int) -> RID:
    pass;

func get_contact_collider_id(contact_idx: int) -> int:
    pass;

func get_contact_collider_object(contact_idx: int) -> Object:
    pass;

func get_contact_collider_position(contact_idx: int) -> Vector2:
    pass;

func get_contact_collider_shape(contact_idx: int) -> int:
    pass;

func get_contact_collider_shape_metadata(contact_idx: int) -> Variant:
    pass;

func get_contact_collider_velocity_at_position(contact_idx: int) -> Vector2:
    pass;

func get_contact_count() -> int:
    pass;

func get_contact_local_normal(contact_idx: int) -> Vector2:
    pass;

func get_contact_local_position(contact_idx: int) -> Vector2:
    pass;

func get_contact_local_shape(contact_idx: int) -> int:
    pass;

func get_space_state() -> PhysicsDirectSpaceState2D:
    pass;

func integrate_forces() -> void:
    pass;

