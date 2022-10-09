extends PhysicsDirectBodyState2D
class_name PhysicsDirectBodyState2DExtension




func _add_constant_central_force(force: Vector2) -> void:
	pass;

func _add_constant_force(force: Vector2, position: Vector2) -> void:
	pass;

func _add_constant_torque(torque: float) -> void:
	pass;

func _apply_central_force(force: Vector2) -> void:
	pass;

func _apply_central_impulse(impulse: Vector2) -> void:
	pass;

func _apply_force(force: Vector2, position: Vector2) -> void:
	pass;

func _apply_impulse(impulse: Vector2, position: Vector2) -> void:
	pass;

func _apply_torque(torque: float) -> void:
	pass;

func _apply_torque_impulse(impulse: float) -> void:
	pass;

func _get_angular_velocity() -> float:
	pass;

func _get_center_of_mass() -> Vector2:
	pass;

func _get_center_of_mass_local() -> Vector2:
	pass;

func _get_constant_force() -> Vector2:
	pass;

func _get_constant_torque() -> float:
	pass;

func _get_contact_collider(contact_idx: int) -> RID:
	pass;

func _get_contact_collider_id(contact_idx: int) -> int:
	pass;

func _get_contact_collider_object(contact_idx: int) -> Object:
	pass;

func _get_contact_collider_position(contact_idx: int) -> Vector2:
	pass;

func _get_contact_collider_shape(contact_idx: int) -> int:
	pass;

func _get_contact_collider_velocity_at_position(contact_idx: int) -> Vector2:
	pass;

func _get_contact_count() -> int:
	pass;

func _get_contact_local_normal(contact_idx: int) -> Vector2:
	pass;

func _get_contact_local_position(contact_idx: int) -> Vector2:
	pass;

func _get_contact_local_shape(contact_idx: int) -> int:
	pass;

func _get_inverse_inertia() -> float:
	pass;

func _get_inverse_mass() -> float:
	pass;

func _get_linear_velocity() -> Vector2:
	pass;

func _get_space_state() -> PhysicsDirectSpaceState2D:
	pass;

func _get_step() -> float:
	pass;

func _get_total_angular_damp() -> float:
	pass;

func _get_total_gravity() -> Vector2:
	pass;

func _get_total_linear_damp() -> float:
	pass;

func _get_transform() -> Transform2D:
	pass;

func _get_velocity_at_local_position(local_position: Vector2) -> Vector2:
	pass;

func _integrate_forces() -> void:
	pass;

func _is_sleeping() -> bool:
	pass;

func _set_angular_velocity(velocity: float) -> void:
	pass;

func _set_constant_force(force: Vector2) -> void:
	pass;

func _set_constant_torque(torque: float) -> void:
	pass;

func _set_linear_velocity(velocity: Vector2) -> void:
	pass;

func _set_sleep_state(enabled: bool) -> void:
	pass;

func _set_transform(transform: Transform2D) -> void:
	pass;


