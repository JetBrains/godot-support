class_name PhysicsDirectBodyState3DExtension




virtual func _add_constant_central_force() -> void:
	pass;

virtual func _add_constant_force(force: Vector3, position: Vector3) -> void:
	pass;

virtual func _add_constant_torque() -> void:
	pass;

virtual func _apply_central_force() -> void:
	pass;

virtual func _apply_central_impulse() -> void:
	pass;

virtual func _apply_force(force: Vector3, position: Vector3) -> void:
	pass;

virtual func _apply_impulse(impulse: Vector3, position: Vector3) -> void:
	pass;

virtual func _apply_torque() -> void:
	pass;

virtual func _apply_torque_impulse() -> void:
	pass;

virtual const func _get_angular_velocity() -> Vector3:
	pass;

virtual const func _get_center_of_mass() -> Vector3:
	pass;

virtual const func _get_center_of_mass_local() -> Vector3:
	pass;

virtual const func _get_constant_force() -> Vector3:
	pass;

virtual const func _get_constant_torque() -> Vector3:
	pass;

virtual const func _get_contact_collider() -> RID:
	pass;

virtual const func _get_contact_collider_id() -> int:
	pass;

virtual const func _get_contact_collider_object() -> Object:
	pass;

virtual const func _get_contact_collider_position() -> Vector3:
	pass;

virtual const func _get_contact_collider_shape() -> int:
	pass;

virtual const func _get_contact_collider_velocity_at_position() -> Vector3:
	pass;

virtual const func _get_contact_count() -> int:
	pass;

virtual const func _get_contact_impulse() -> float:
	pass;

virtual const func _get_contact_local_normal() -> Vector3:
	pass;

virtual const func _get_contact_local_position() -> Vector3:
	pass;

virtual const func _get_contact_local_shape() -> int:
	pass;

virtual const func _get_inverse_inertia() -> Vector3:
	pass;

virtual const func _get_inverse_mass() -> float:
	pass;

virtual const func _get_linear_velocity() -> Vector3:
	pass;

virtual const func _get_principal_inertia_axes() -> Basis:
	pass;

virtual func _get_space_state() -> PhysicsDirectSpaceState3D:
	pass;

virtual const func _get_step() -> float:
	pass;

virtual const func _get_total_angular_damp() -> float:
	pass;

virtual const func _get_total_gravity() -> Vector3:
	pass;

virtual const func _get_total_linear_damp() -> float:
	pass;

virtual const func _get_transform() -> Transform3D:
	pass;

virtual const func _get_velocity_at_local_position() -> Vector3:
	pass;

virtual func _integrate_forces() -> void:
	pass;

virtual const func _is_sleeping() -> bool:
	pass;

virtual func _set_angular_velocity() -> void:
	pass;

virtual func _set_constant_force() -> void:
	pass;

virtual func _set_constant_torque() -> void:
	pass;

virtual func _set_linear_velocity() -> void:
	pass;

virtual func _set_sleep_state() -> void:
	pass;

virtual func _set_transform() -> void:
	pass;


