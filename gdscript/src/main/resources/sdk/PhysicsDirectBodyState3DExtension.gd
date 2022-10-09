extends PhysicsDirectBodyState3D
class_name PhysicsDirectBodyState3DExtension




func _add_constant_central_force(force: Vector3) -> void:
	pass;

func _add_constant_force(force: Vector3, position: Vector3) -> void:
	pass;

func _add_constant_torque(torque: Vector3) -> void:
	pass;

func _apply_central_force(force: Vector3) -> void:
	pass;

func _apply_central_impulse(impulse: Vector3) -> void:
	pass;

func _apply_force(force: Vector3, position: Vector3) -> void:
	pass;

func _apply_impulse(impulse: Vector3, position: Vector3) -> void:
	pass;

func _apply_torque(torque: Vector3) -> void:
	pass;

func _apply_torque_impulse(impulse: Vector3) -> void:
	pass;

func _get_angular_velocity() -> Vector3:
	pass;

func _get_center_of_mass() -> Vector3:
	pass;

func _get_center_of_mass_local() -> Vector3:
	pass;

func _get_constant_force() -> Vector3:
	pass;

func _get_constant_torque() -> Vector3:
	pass;

func _get_contact_collider(contact_idx: int) -> RID:
	pass;

func _get_contact_collider_id(contact_idx: int) -> int:
	pass;

func _get_contact_collider_object(contact_idx: int) -> Object:
	pass;

func _get_contact_collider_position(contact_idx: int) -> Vector3:
	pass;

func _get_contact_collider_shape(contact_idx: int) -> int:
	pass;

func _get_contact_collider_velocity_at_position(contact_idx: int) -> Vector3:
	pass;

func _get_contact_count() -> int:
	pass;

func _get_contact_impulse(contact_idx: int) -> float:
	pass;

func _get_contact_local_normal(contact_idx: int) -> Vector3:
	pass;

func _get_contact_local_position(contact_idx: int) -> Vector3:
	pass;

func _get_contact_local_shape(contact_idx: int) -> int:
	pass;

func _get_inverse_inertia() -> Vector3:
	pass;

func _get_inverse_mass() -> float:
	pass;

func _get_linear_velocity() -> Vector3:
	pass;

func _get_principal_inertia_axes() -> Basis:
	pass;

func _get_space_state() -> PhysicsDirectSpaceState3D:
	pass;

func _get_step() -> float:
	pass;

func _get_total_angular_damp() -> float:
	pass;

func _get_total_gravity() -> Vector3:
	pass;

func _get_total_linear_damp() -> float:
	pass;

func _get_transform() -> Transform3D:
	pass;

func _get_velocity_at_local_position(local_position: Vector3) -> Vector3:
	pass;

func _integrate_forces() -> void:
	pass;

func _is_sleeping() -> bool:
	pass;

func _set_angular_velocity(velocity: Vector3) -> void:
	pass;

func _set_constant_force(force: Vector3) -> void:
	pass;

func _set_constant_torque(torque: Vector3) -> void:
	pass;

func _set_linear_velocity(velocity: Vector3) -> void:
	pass;

func _set_sleep_state(enabled: bool) -> void:
	pass;

func _set_transform(transform: Transform3D) -> void:
	pass;


