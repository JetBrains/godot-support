class_name PhysicsServer2DExtension




virtual func _area_add_shape(area: RID, shape: RID, transform: Transform2D, disabled: bool) -> void:
	pass;

virtual func _area_attach_canvas_instance_id(area: RID, id: int) -> void:
	pass;

virtual func _area_attach_object_instance_id(area: RID, id: int) -> void:
	pass;

virtual func _area_clear_shapes() -> void:
	pass;

virtual func _area_create() -> RID:
	pass;

virtual const func _area_get_canvas_instance_id() -> int:
	pass;

virtual const func _area_get_collision_layer() -> int:
	pass;

virtual const func _area_get_collision_mask() -> int:
	pass;

virtual const func _area_get_object_instance_id() -> int:
	pass;

virtual const func _area_get_param(area: RID, param: int) -> Variant:
	pass;

virtual const func _area_get_shape(area: RID, shape_idx: int) -> RID:
	pass;

virtual const func _area_get_shape_count() -> int:
	pass;

virtual const func _area_get_shape_transform(area: RID, shape_idx: int) -> Transform2D:
	pass;

virtual const func _area_get_space() -> RID:
	pass;

virtual const func _area_get_transform() -> Transform2D:
	pass;

virtual func _area_remove_shape(area: RID, shape_idx: int) -> void:
	pass;

virtual func _area_set_area_monitor_callback(area: RID, callback: Callable) -> void:
	pass;

virtual func _area_set_collision_layer(area: RID, layer: int) -> void:
	pass;

virtual func _area_set_collision_mask(area: RID, mask: int) -> void:
	pass;

virtual func _area_set_monitor_callback(area: RID, callback: Callable) -> void:
	pass;

virtual func _area_set_monitorable(area: RID, monitorable: bool) -> void:
	pass;

virtual func _area_set_param(area: RID, param: int, value: Variant) -> void:
	pass;

virtual func _area_set_pickable(area: RID, pickable: bool) -> void:
	pass;

virtual func _area_set_shape(area: RID, shape_idx: int, shape: RID) -> void:
	pass;

virtual func _area_set_shape_disabled(area: RID, shape_idx: int, disabled: bool) -> void:
	pass;

virtual func _area_set_shape_transform(area: RID, shape_idx: int, transform: Transform2D) -> void:
	pass;

virtual func _area_set_space(area: RID, space: RID) -> void:
	pass;

virtual func _area_set_transform(area: RID, transform: Transform2D) -> void:
	pass;

virtual func _body_add_collision_exception(body: RID, excepted_body: RID) -> void:
	pass;

virtual func _body_add_constant_central_force(body: RID, force: Vector2) -> void:
	pass;

virtual func _body_add_constant_force(body: RID, force: Vector2, position: Vector2) -> void:
	pass;

virtual func _body_add_constant_torque(body: RID, torque: float) -> void:
	pass;

virtual func _body_add_shape(body: RID, shape: RID, transform: Transform2D, disabled: bool) -> void:
	pass;

virtual func _body_apply_central_force(body: RID, force: Vector2) -> void:
	pass;

virtual func _body_apply_central_impulse(body: RID, impulse: Vector2) -> void:
	pass;

virtual func _body_apply_force(body: RID, force: Vector2, position: Vector2) -> void:
	pass;

virtual func _body_apply_impulse(body: RID, impulse: Vector2, position: Vector2) -> void:
	pass;

virtual func _body_apply_torque(body: RID, torque: float) -> void:
	pass;

virtual func _body_apply_torque_impulse(body: RID, impulse: float) -> void:
	pass;

virtual func _body_attach_canvas_instance_id(body: RID, id: int) -> void:
	pass;

virtual func _body_attach_object_instance_id(body: RID, id: int) -> void:
	pass;

virtual func _body_clear_shapes() -> void:
	pass;

virtual func _body_collide_shape(body: RID, body_shape: int, shape: RID, shape_xform: Transform2D, motion: Vector2, results: void*, result_max: int, result_count: int32_t*) -> bool:
	pass;

virtual func _body_create() -> RID:
	pass;

virtual const func _body_get_canvas_instance_id() -> int:
	pass;

virtual const func _body_get_collision_exceptions() -> RID[]:
	pass;

virtual const func _body_get_collision_layer() -> int:
	pass;

virtual const func _body_get_collision_mask() -> int:
	pass;

virtual const func _body_get_collision_priority() -> float:
	pass;

virtual const func _body_get_constant_force() -> Vector2:
	pass;

virtual const func _body_get_constant_torque() -> float:
	pass;

virtual const func _body_get_contacts_reported_depth_threshold() -> float:
	pass;

virtual const func _body_get_continuous_collision_detection_mode() -> int:
	pass;

virtual func _body_get_direct_state() -> PhysicsDirectBodyState2D:
	pass;

virtual const func _body_get_max_contacts_reported() -> int:
	pass;

virtual const func _body_get_mode() -> int:
	pass;

virtual const func _body_get_object_instance_id() -> int:
	pass;

virtual const func _body_get_param(body: RID, param: int) -> Variant:
	pass;

virtual const func _body_get_shape(body: RID, shape_idx: int) -> RID:
	pass;

virtual const func _body_get_shape_count() -> int:
	pass;

virtual const func _body_get_shape_transform(body: RID, shape_idx: int) -> Transform2D:
	pass;

virtual const func _body_get_space() -> RID:
	pass;

virtual const func _body_get_state(body: RID, state: int) -> Variant:
	pass;

virtual const func _body_is_omitting_force_integration() -> bool:
	pass;

virtual func _body_remove_collision_exception(body: RID, excepted_body: RID) -> void:
	pass;

virtual func _body_remove_shape(body: RID, shape_idx: int) -> void:
	pass;

virtual func _body_reset_mass_properties() -> void:
	pass;

virtual func _body_set_axis_velocity(body: RID, axis_velocity: Vector2) -> void:
	pass;

virtual func _body_set_collision_layer(body: RID, layer: int) -> void:
	pass;

virtual func _body_set_collision_mask(body: RID, mask: int) -> void:
	pass;

virtual func _body_set_collision_priority(body: RID, priority: float) -> void:
	pass;

virtual func _body_set_constant_force(body: RID, force: Vector2) -> void:
	pass;

virtual func _body_set_constant_torque(body: RID, torque: float) -> void:
	pass;

virtual func _body_set_contacts_reported_depth_threshold(body: RID, threshold: float) -> void:
	pass;

virtual func _body_set_continuous_collision_detection_mode(body: RID, mode: int) -> void:
	pass;

virtual func _body_set_force_integration_callback(body: RID, callable: Callable, userdata: Variant) -> void:
	pass;

virtual func _body_set_max_contacts_reported(body: RID, amount: int) -> void:
	pass;

virtual func _body_set_mode(body: RID, mode: int) -> void:
	pass;

virtual func _body_set_omit_force_integration(body: RID, enable: bool) -> void:
	pass;

virtual func _body_set_param(body: RID, param: int, value: Variant) -> void:
	pass;

virtual func _body_set_pickable(body: RID, pickable: bool) -> void:
	pass;

virtual func _body_set_shape(body: RID, shape_idx: int, shape: RID) -> void:
	pass;

virtual func _body_set_shape_as_one_way_collision(body: RID, shape_idx: int, enable: bool, margin: float) -> void:
	pass;

virtual func _body_set_shape_disabled(body: RID, shape_idx: int, disabled: bool) -> void:
	pass;

virtual func _body_set_shape_transform(body: RID, shape_idx: int, transform: Transform2D) -> void:
	pass;

virtual func _body_set_space(body: RID, space: RID) -> void:
	pass;

virtual func _body_set_state(body: RID, state: int, value: Variant) -> void:
	pass;

virtual func _body_set_state_sync_callback(body: RID, callable: Callable) -> void:
	pass;

virtual const func _body_test_motion(body: RID, from: Transform2D, motion: Vector2, margin: float, collide_separation_ray: bool, recovery_as_collision: bool, result: PhysicsServer2DExtensionMotionResult*) -> bool:
	pass;

virtual func _capsule_shape_create() -> RID:
	pass;

virtual func _circle_shape_create() -> RID:
	pass;

virtual func _concave_polygon_shape_create() -> RID:
	pass;

virtual func _convex_polygon_shape_create() -> RID:
	pass;

virtual const func _damped_spring_joint_get_param(joint: RID, param: int) -> float:
	pass;

virtual func _damped_spring_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

virtual func _end_sync() -> void:
	pass;

virtual func _finish() -> void:
	pass;

virtual func _flush_queries() -> void:
	pass;

virtual func _free_rid() -> void:
	pass;

virtual func _get_process_info() -> int:
	pass;

virtual func _init() -> void:
	pass;

virtual const func _is_flushing_queries() -> bool:
	pass;

virtual func _joint_clear() -> void:
	pass;

virtual func _joint_create() -> RID:
	pass;

virtual func _joint_disable_collisions_between_bodies(joint: RID, disable: bool) -> void:
	pass;

virtual const func _joint_get_param(joint: RID, param: int) -> float:
	pass;

virtual const func _joint_get_type() -> int:
	pass;

virtual const func _joint_is_disabled_collisions_between_bodies() -> bool:
	pass;

virtual func _joint_make_damped_spring(joint: RID, anchor_a: Vector2, anchor_b: Vector2, body_a: RID, body_b: RID) -> void:
	pass;

virtual func _joint_make_groove(joint: RID, a_groove1: Vector2, a_groove2: Vector2, b_anchor: Vector2, body_a: RID, body_b: RID) -> void:
	pass;

virtual func _joint_make_pin(joint: RID, anchor: Vector2, body_a: RID, body_b: RID) -> void:
	pass;

virtual func _joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

virtual const func _pin_joint_get_param(joint: RID, param: int) -> float:
	pass;

virtual func _pin_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

virtual func _rectangle_shape_create() -> RID:
	pass;

virtual func _segment_shape_create() -> RID:
	pass;

virtual func _separation_ray_shape_create() -> RID:
	pass;

virtual func _set_active() -> void:
	pass;

virtual func _shape_collide(shape_A: RID, xform_A: Transform2D, motion_A: Vector2, shape_B: RID, xform_B: Transform2D, motion_B: Vector2, results: void*, result_max: int, result_count: int32_t*) -> bool:
	pass;

virtual const func _shape_get_custom_solver_bias() -> float:
	pass;

virtual const func _shape_get_data() -> Variant:
	pass;

virtual const func _shape_get_type() -> int:
	pass;

virtual func _shape_set_custom_solver_bias(shape: RID, bias: float) -> void:
	pass;

virtual func _shape_set_data(shape: RID, data: Variant) -> void:
	pass;

virtual func _space_create() -> RID:
	pass;

virtual const func _space_get_contact_count() -> int:
	pass;

virtual const func _space_get_contacts() -> PackedVector2Array:
	pass;

virtual func _space_get_direct_state() -> PhysicsDirectSpaceState2D:
	pass;

virtual const func _space_get_param(space: RID, param: int) -> float:
	pass;

virtual const func _space_is_active() -> bool:
	pass;

virtual func _space_set_active(space: RID, active: bool) -> void:
	pass;

virtual func _space_set_debug_contacts(space: RID, max_contacts: int) -> void:
	pass;

virtual func _space_set_param(space: RID, param: int, value: float) -> void:
	pass;

virtual func _step() -> void:
	pass;

virtual func _sync() -> void:
	pass;

virtual func _world_boundary_shape_create() -> RID:
	pass;


