extends Object
class_name PhysicsServer2D
const SPACE_PARAM_CONTACT_RECYCLE_RADIUS = 0;
const SPACE_PARAM_CONTACT_MAX_SEPARATION = 1;
const SPACE_PARAM_BODY_MAX_ALLOWED_PENETRATION = 2;
const SPACE_PARAM_BODY_LINEAR_VELOCITY_SLEEP_THRESHOLD = 3;
const SPACE_PARAM_BODY_ANGULAR_VELOCITY_SLEEP_THRESHOLD = 4;
const SPACE_PARAM_BODY_TIME_TO_SLEEP = 5;
const SPACE_PARAM_CONSTRAINT_DEFAULT_BIAS = 6;
const SPACE_PARAM_TEST_MOTION_MIN_CONTACT_DEPTH = 7;
const SHAPE_LINE = 0;
const SHAPE_RAY = 1;
const SHAPE_SEGMENT = 2;
const SHAPE_CIRCLE = 3;
const SHAPE_RECTANGLE = 4;
const SHAPE_CAPSULE = 5;
const SHAPE_CONVEX_POLYGON = 6;
const SHAPE_CONCAVE_POLYGON = 7;
const SHAPE_CUSTOM = 8;
const AREA_PARAM_GRAVITY = 0;
const AREA_PARAM_GRAVITY_VECTOR = 1;
const AREA_PARAM_GRAVITY_IS_POINT = 2;
const AREA_PARAM_GRAVITY_DISTANCE_SCALE = 3;
const AREA_PARAM_GRAVITY_POINT_ATTENUATION = 4;
const AREA_PARAM_LINEAR_DAMP = 5;
const AREA_PARAM_ANGULAR_DAMP = 6;
const AREA_PARAM_PRIORITY = 7;
const AREA_SPACE_OVERRIDE_DISABLED = 0;
const AREA_SPACE_OVERRIDE_COMBINE = 1;
const AREA_SPACE_OVERRIDE_COMBINE_REPLACE = 2;
const AREA_SPACE_OVERRIDE_REPLACE = 3;
const AREA_SPACE_OVERRIDE_REPLACE_COMBINE = 4;
const BODY_MODE_STATIC = 0;
const BODY_MODE_KINEMATIC = 1;
const BODY_MODE_DYNAMIC = 2;
const BODY_MODE_DYNAMIC_LOCKED = 3;
const BODY_PARAM_BOUNCE = 0;
const BODY_PARAM_FRICTION = 1;
const BODY_PARAM_MASS = 2;
const BODY_PARAM_INERTIA = 3;
const BODY_PARAM_GRAVITY_SCALE = 4;
const BODY_PARAM_LINEAR_DAMP = 5;
const BODY_PARAM_ANGULAR_DAMP = 6;
const BODY_PARAM_MAX = 7;
const BODY_STATE_TRANSFORM = 0;
const BODY_STATE_LINEAR_VELOCITY = 1;
const BODY_STATE_ANGULAR_VELOCITY = 2;
const BODY_STATE_SLEEPING = 3;
const BODY_STATE_CAN_SLEEP = 4;
const JOINT_TYPE_PIN = 0;
const JOINT_TYPE_GROOVE = 1;
const JOINT_TYPE_DAMPED_SPRING = 2;
const JOINT_TYPE_MAX = 3;
const JOINT_PARAM_BIAS = 0;
const JOINT_PARAM_MAX_BIAS = 1;
const JOINT_PARAM_MAX_FORCE = 2;
const DAMPED_SPRING_REST_LENGTH = 0;
const DAMPED_SPRING_STIFFNESS = 1;
const DAMPED_SPRING_DAMPING = 2;
const CCD_MODE_DISABLED = 0;
const CCD_MODE_CAST_RAY = 1;
const CCD_MODE_CAST_SHAPE = 2;
const AREA_BODY_ADDED = 0;
const AREA_BODY_REMOVED = 1;
const INFO_ACTIVE_OBJECTS = 0;
const INFO_COLLISION_PAIRS = 1;
const INFO_ISLAND_COUNT = 2;


func area_add_shape(area: RID, shape: RID, transform: Transform2D, disabled: bool) -> void:
    pass;
func area_attach_canvas_instance_id(area: RID, id: int) -> void:
    pass;
func area_attach_object_instance_id(area: RID, id: int) -> void:
    pass;
func area_clear_shapes(area: RID) -> void:
    pass;
func area_create() -> RID:
    pass;
func area_get_canvas_instance_id(area: RID) -> int:
    pass;
func area_get_object_instance_id(area: RID) -> int:
    pass;
func area_get_param(area: RID, param: int) -> Variant:
    pass;
func area_get_shape(area: RID, shape_idx: int) -> RID:
    pass;
func area_get_shape_count(area: RID) -> int:
    pass;
func area_get_shape_transform(area: RID, shape_idx: int) -> Transform2D:
    pass;
func area_get_space(area: RID) -> RID:
    pass;
func area_get_space_override_mode(area: RID) -> int:
    pass;
func area_get_transform(area: RID) -> Transform2D:
    pass;
func area_remove_shape(area: RID, shape_idx: int) -> void:
    pass;
func area_set_area_monitor_callback(area: RID, receiver: Object, method: StringName) -> void:
    pass;
func area_set_collision_layer(area: RID, layer: int) -> void:
    pass;
func area_set_collision_mask(area: RID, mask: int) -> void:
    pass;
func area_set_monitor_callback(area: RID, receiver: Object, method: StringName) -> void:
    pass;
func area_set_monitorable(area: RID, monitorable: bool) -> void:
    pass;
func area_set_param(area: RID, param: int, value: Variant) -> void:
    pass;
func area_set_shape(area: RID, shape_idx: int, shape: RID) -> void:
    pass;
func area_set_shape_disabled(area: RID, shape_idx: int, disabled: bool) -> void:
    pass;
func area_set_shape_transform(area: RID, shape_idx: int, transform: Transform2D) -> void:
    pass;
func area_set_space(area: RID, space: RID) -> void:
    pass;
func area_set_space_override_mode(area: RID, mode: int) -> void:
    pass;
func area_set_transform(area: RID, transform: Transform2D) -> void:
    pass;
func body_add_central_force(body: RID, force: Vector2) -> void:
    pass;
func body_add_collision_exception(body: RID, excepted_body: RID) -> void:
    pass;
func body_add_force(body: RID, force: Vector2, position: Vector2) -> void:
    pass;
func body_add_shape(body: RID, shape: RID, transform: Transform2D, disabled: bool) -> void:
    pass;
func body_add_torque(body: RID, torque: float) -> void:
    pass;
func body_apply_central_impulse(body: RID, impulse: Vector2) -> void:
    pass;
func body_apply_impulse(body: RID, impulse: Vector2, position: Vector2) -> void:
    pass;
func body_apply_torque_impulse(body: RID, impulse: float) -> void:
    pass;
func body_attach_canvas_instance_id(body: RID, id: int) -> void:
    pass;
func body_attach_object_instance_id(body: RID, id: int) -> void:
    pass;
func body_clear_shapes(body: RID) -> void:
    pass;
func body_create() -> RID:
    pass;
func body_get_canvas_instance_id(body: RID) -> int:
    pass;
func body_get_collision_layer(body: RID) -> int:
    pass;
func body_get_collision_mask(body: RID) -> int:
    pass;
func body_get_continuous_collision_detection_mode(body: RID) -> int:
    pass;
func body_get_direct_state(body: RID) -> PhysicsDirectBodyState2D:
    pass;
func body_get_max_contacts_reported(body: RID) -> int:
    pass;
func body_get_mode(body: RID) -> int:
    pass;
func body_get_object_instance_id(body: RID) -> int:
    pass;
func body_get_param(body: RID, param: int) -> float:
    pass;
func body_get_shape(body: RID, shape_idx: int) -> RID:
    pass;
func body_get_shape_count(body: RID) -> int:
    pass;
func body_get_shape_metadata(body: RID, shape_idx: int) -> Variant:
    pass;
func body_get_shape_transform(body: RID, shape_idx: int) -> Transform2D:
    pass;
func body_get_space(body: RID) -> RID:
    pass;
func body_get_state(body: RID, state: int) -> Variant:
    pass;
func body_is_omitting_force_integration(body: RID) -> bool:
    pass;
func body_remove_collision_exception(body: RID, excepted_body: RID) -> void:
    pass;
func body_remove_shape(body: RID, shape_idx: int) -> void:
    pass;
func body_set_axis_velocity(body: RID, axis_velocity: Vector2) -> void:
    pass;
func body_set_collision_layer(body: RID, layer: int) -> void:
    pass;
func body_set_collision_mask(body: RID, mask: int) -> void:
    pass;
func body_set_continuous_collision_detection_mode(body: RID, mode: int) -> void:
    pass;
func body_set_force_integration_callback(body: RID, callable: Callable, userdata: Variant) -> void:
    pass;
func body_set_max_contacts_reported(body: RID, amount: int) -> void:
    pass;
func body_set_mode(body: RID, mode: int) -> void:
    pass;
func body_set_omit_force_integration(body: RID, enable: bool) -> void:
    pass;
func body_set_param(body: RID, param: int, value: float) -> void:
    pass;
func body_set_shape(body: RID, shape_idx: int, shape: RID) -> void:
    pass;
func body_set_shape_as_one_way_collision(body: RID, shape_idx: int, enable: bool, margin: float) -> void:
    pass;
func body_set_shape_disabled(body: RID, shape_idx: int, disabled: bool) -> void:
    pass;
func body_set_shape_metadata(body: RID, shape_idx: int, metadata: Variant) -> void:
    pass;
func body_set_shape_transform(body: RID, shape_idx: int, transform: Transform2D) -> void:
    pass;
func body_set_space(body: RID, space: RID) -> void:
    pass;
func body_set_state(body: RID, state: int, value: Variant) -> void:
    pass;
func body_test_motion(body: RID, from: Transform2D, motion: Vector2, infinite_inertia: bool, margin: float, result: PhysicsTestMotionResult2D, exclude_raycast_shapes: bool, exclude: Array) -> bool:
    pass;
func capsule_shape_create() -> RID:
    pass;
func circle_shape_create() -> RID:
    pass;
func concave_polygon_shape_create() -> RID:
    pass;
func convex_polygon_shape_create() -> RID:
    pass;
func damped_spring_joint_get_param(joint: RID, param: int) -> float:
    pass;
func damped_spring_joint_set_param(joint: RID, param: int, value: float) -> void:
    pass;
func free_rid(rid: RID) -> void:
    pass;
func get_process_info(process_info: int) -> int:
    pass;
func joint_clear(joint: RID) -> void:
    pass;
func joint_create() -> RID:
    pass;
func joint_get_param(joint: RID, param: int) -> float:
    pass;
func joint_get_type(joint: RID) -> int:
    pass;
func joint_make_damped_spring(joint: RID, anchor_a: Vector2, anchor_b: Vector2, body_a: RID, body_b: RID) -> void:
    pass;
func joint_make_groove(joint: RID, groove1_a: Vector2, groove2_a: Vector2, anchor_b: Vector2, body_a: RID, body_b: RID) -> void:
    pass;
func joint_make_pin(joint: RID, anchor: Vector2, body_a: RID, body_b: RID) -> void:
    pass;
func joint_set_param(joint: RID, param: int, value: float) -> void:
    pass;
func line_shape_create() -> RID:
    pass;
func ray_shape_create() -> RID:
    pass;
func rectangle_shape_create() -> RID:
    pass;
func segment_shape_create() -> RID:
    pass;
func set_active(active: bool) -> void:
    pass;
func set_collision_iterations(iterations: int) -> void:
    pass;
func shape_get_data(shape: RID) -> Variant:
    pass;
func shape_get_type(shape: RID) -> int:
    pass;
func shape_set_data(shape: RID, data: Variant) -> void:
    pass;
func space_create() -> RID:
    pass;
func space_get_direct_state(space: RID) -> PhysicsDirectSpaceState2D:
    pass;
func space_get_param(space: RID, param: int) -> float:
    pass;
func space_is_active(space: RID) -> bool:
    pass;
func space_set_active(space: RID, active: bool) -> void:
    pass;
func space_set_param(space: RID, param: int, value: float) -> void:
    pass;
