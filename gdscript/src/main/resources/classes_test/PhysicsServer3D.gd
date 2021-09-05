extends Object
class_name PhysicsServer3D

const JOINT_TYPE_PIN = 0;
const JOINT_TYPE_HINGE = 1;
const JOINT_TYPE_SLIDER = 2;
const JOINT_TYPE_CONE_TWIST = 3;
const JOINT_TYPE_6DOF = 4;
const JOINT_TYPE_MAX = 5;
const PIN_JOINT_BIAS = 0;
const PIN_JOINT_DAMPING = 1;
const PIN_JOINT_IMPULSE_CLAMP = 2;
const HINGE_JOINT_BIAS = 0;
const HINGE_JOINT_LIMIT_UPPER = 1;
const HINGE_JOINT_LIMIT_LOWER = 2;
const HINGE_JOINT_LIMIT_BIAS = 3;
const HINGE_JOINT_LIMIT_SOFTNESS = 4;
const HINGE_JOINT_LIMIT_RELAXATION = 5;
const HINGE_JOINT_MOTOR_TARGET_VELOCITY = 6;
const HINGE_JOINT_MOTOR_MAX_IMPULSE = 7;
const HINGE_JOINT_FLAG_USE_LIMIT = 0;
const HINGE_JOINT_FLAG_ENABLE_MOTOR = 1;
const SLIDER_JOINT_LINEAR_LIMIT_UPPER = 0;
const SLIDER_JOINT_LINEAR_LIMIT_LOWER = 1;
const SLIDER_JOINT_LINEAR_LIMIT_SOFTNESS = 2;
const SLIDER_JOINT_LINEAR_LIMIT_RESTITUTION = 3;
const SLIDER_JOINT_LINEAR_LIMIT_DAMPING = 4;
const SLIDER_JOINT_LINEAR_MOTION_SOFTNESS = 5;
const SLIDER_JOINT_LINEAR_MOTION_RESTITUTION = 6;
const SLIDER_JOINT_LINEAR_MOTION_DAMPING = 7;
const SLIDER_JOINT_LINEAR_ORTHOGONAL_SOFTNESS = 8;
const SLIDER_JOINT_LINEAR_ORTHOGONAL_RESTITUTION = 9;
const SLIDER_JOINT_LINEAR_ORTHOGONAL_DAMPING = 10;
const SLIDER_JOINT_ANGULAR_LIMIT_UPPER = 11;
const SLIDER_JOINT_ANGULAR_LIMIT_LOWER = 12;
const SLIDER_JOINT_ANGULAR_LIMIT_SOFTNESS = 13;
const SLIDER_JOINT_ANGULAR_LIMIT_RESTITUTION = 14;
const SLIDER_JOINT_ANGULAR_LIMIT_DAMPING = 15;
const SLIDER_JOINT_ANGULAR_MOTION_SOFTNESS = 16;
const SLIDER_JOINT_ANGULAR_MOTION_RESTITUTION = 17;
const SLIDER_JOINT_ANGULAR_MOTION_DAMPING = 18;
const SLIDER_JOINT_ANGULAR_ORTHOGONAL_SOFTNESS = 19;
const SLIDER_JOINT_ANGULAR_ORTHOGONAL_RESTITUTION = 20;
const SLIDER_JOINT_ANGULAR_ORTHOGONAL_DAMPING = 21;
const SLIDER_JOINT_MAX = 22;
const CONE_TWIST_JOINT_SWING_SPAN = 0;
const CONE_TWIST_JOINT_TWIST_SPAN = 1;
const CONE_TWIST_JOINT_BIAS = 2;
const CONE_TWIST_JOINT_SOFTNESS = 3;
const CONE_TWIST_JOINT_RELAXATION = 4;
const G6DOF_JOINT_LINEAR_LOWER_LIMIT = 0;
const G6DOF_JOINT_LINEAR_UPPER_LIMIT = 1;
const G6DOF_JOINT_LINEAR_LIMIT_SOFTNESS = 2;
const G6DOF_JOINT_LINEAR_RESTITUTION = 3;
const G6DOF_JOINT_LINEAR_DAMPING = 4;
const G6DOF_JOINT_LINEAR_MOTOR_TARGET_VELOCITY = 5;
const G6DOF_JOINT_LINEAR_MOTOR_FORCE_LIMIT = 6;
const G6DOF_JOINT_ANGULAR_LOWER_LIMIT = 10;
const G6DOF_JOINT_ANGULAR_UPPER_LIMIT = 11;
const G6DOF_JOINT_ANGULAR_LIMIT_SOFTNESS = 12;
const G6DOF_JOINT_ANGULAR_DAMPING = 13;
const G6DOF_JOINT_ANGULAR_RESTITUTION = 14;
const G6DOF_JOINT_ANGULAR_FORCE_LIMIT = 15;
const G6DOF_JOINT_ANGULAR_ERP = 16;
const G6DOF_JOINT_ANGULAR_MOTOR_TARGET_VELOCITY = 17;
const G6DOF_JOINT_ANGULAR_MOTOR_FORCE_LIMIT = 18;
const G6DOF_JOINT_FLAG_ENABLE_LINEAR_LIMIT = 0;
const G6DOF_JOINT_FLAG_ENABLE_ANGULAR_LIMIT = 1;
const G6DOF_JOINT_FLAG_ENABLE_MOTOR = 4;
const G6DOF_JOINT_FLAG_ENABLE_LINEAR_MOTOR = 5;
const SHAPE_PLANE = 0;
const SHAPE_RAY = 1;
const SHAPE_SPHERE = 2;
const SHAPE_BOX = 3;
const SHAPE_CAPSULE = 4;
const SHAPE_CYLINDER = 5;
const SHAPE_CONVEX_POLYGON = 6;
const SHAPE_CONCAVE_POLYGON = 7;
const SHAPE_HEIGHTMAP = 8;
const SHAPE_SOFT_BODY = 9;
const SHAPE_CUSTOM = 10;
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
const BODY_PARAM_GRAVITY_SCALE = 3;
const BODY_PARAM_LINEAR_DAMP = 4;
const BODY_PARAM_ANGULAR_DAMP = 5;
const BODY_PARAM_MAX = 6;
const BODY_STATE_TRANSFORM = 0;
const BODY_STATE_LINEAR_VELOCITY = 1;
const BODY_STATE_ANGULAR_VELOCITY = 2;
const BODY_STATE_SLEEPING = 3;
const BODY_STATE_CAN_SLEEP = 4;
const AREA_BODY_ADDED = 0;
const AREA_BODY_REMOVED = 1;
const INFO_ACTIVE_OBJECTS = 0;
const INFO_COLLISION_PAIRS = 1;
const INFO_ISLAND_COUNT = 2;
const SPACE_PARAM_CONTACT_RECYCLE_RADIUS = 0;
const SPACE_PARAM_CONTACT_MAX_SEPARATION = 1;
const SPACE_PARAM_BODY_MAX_ALLOWED_PENETRATION = 2;
const SPACE_PARAM_BODY_LINEAR_VELOCITY_SLEEP_THRESHOLD = 3;
const SPACE_PARAM_BODY_ANGULAR_VELOCITY_SLEEP_THRESHOLD = 4;
const SPACE_PARAM_BODY_TIME_TO_SLEEP = 5;
const SPACE_PARAM_BODY_ANGULAR_VELOCITY_DAMP_RATIO = 6;
const SPACE_PARAM_CONSTRAINT_DEFAULT_BIAS = 7;
const SPACE_PARAM_TEST_MOTION_MIN_CONTACT_DEPTH = 8;
const BODY_AXIS_LINEAR_X = 1;
const BODY_AXIS_LINEAR_Y = 2;
const BODY_AXIS_LINEAR_Z = 4;
const BODY_AXIS_ANGULAR_X = 8;
const BODY_AXIS_ANGULAR_Y = 16;
const BODY_AXIS_ANGULAR_Z = 32;


func area_add_shape(area: RID, shape: RID, transform: Transform3D, disabled: bool) -> void:
    pass;

func area_attach_object_instance_id(area: RID, id: int) -> void:
    pass;

func area_clear_shapes(area: RID) -> void:
    pass;

func area_create() -> RID:
    pass;

func area_get_object_instance_id(area: RID) -> int:
    pass;

func area_get_param(area: RID, param: int) -> Variant:
    pass;

func area_get_shape(area: RID, shape_idx: int) -> RID:
    pass;

func area_get_shape_count(area: RID) -> int:
    pass;

func area_get_shape_transform(area: RID, shape_idx: int) -> Transform3D:
    pass;

func area_get_space(area: RID) -> RID:
    pass;

func area_get_space_override_mode(area: RID) -> int:
    pass;

func area_get_transform(area: RID) -> Transform3D:
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

func area_set_ray_pickable(area: RID, enable: bool) -> void:
    pass;

func area_set_shape(area: RID, shape_idx: int, shape: RID) -> void:
    pass;

func area_set_shape_disabled(area: RID, shape_idx: int, disabled: bool) -> void:
    pass;

func area_set_shape_transform(area: RID, shape_idx: int, transform: Transform3D) -> void:
    pass;

func area_set_space(area: RID, space: RID) -> void:
    pass;

func area_set_space_override_mode(area: RID, mode: int) -> void:
    pass;

func area_set_transform(area: RID, transform: Transform3D) -> void:
    pass;

func body_add_central_force(body: RID, force: Vector3) -> void:
    pass;

func body_add_collision_exception(body: RID, excepted_body: RID) -> void:
    pass;

func body_add_force(body: RID, force: Vector3, position: Vector3) -> void:
    pass;

func body_add_shape(body: RID, shape: RID, transform: Transform3D, disabled: bool) -> void:
    pass;

func body_add_torque(body: RID, torque: Vector3) -> void:
    pass;

func body_apply_central_impulse(body: RID, impulse: Vector3) -> void:
    pass;

func body_apply_impulse(body: RID, impulse: Vector3, position: Vector3) -> void:
    pass;

func body_apply_torque_impulse(body: RID, impulse: Vector3) -> void:
    pass;

func body_attach_object_instance_id(body: RID, id: int) -> void:
    pass;

func body_clear_shapes(body: RID) -> void:
    pass;

func body_create() -> RID:
    pass;

func body_get_collision_layer(body: RID) -> int:
    pass;

func body_get_collision_mask(body: RID) -> int:
    pass;

func body_get_direct_state(body: RID) -> PhysicsDirectBodyState3D:
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

func body_get_shape_transform(body: RID, shape_idx: int) -> Transform3D:
    pass;

func body_get_space(body: RID) -> RID:
    pass;

func body_get_state(body: RID, state: int) -> Variant:
    pass;

func body_is_axis_locked(body: RID, axis: int) -> bool:
    pass;

func body_is_continuous_collision_detection_enabled(body: RID) -> bool:
    pass;

func body_is_omitting_force_integration(body: RID) -> bool:
    pass;

func body_remove_collision_exception(body: RID, excepted_body: RID) -> void:
    pass;

func body_remove_shape(body: RID, shape_idx: int) -> void:
    pass;

func body_set_axis_lock(body: RID, axis: int, lock: bool) -> void:
    pass;

func body_set_axis_velocity(body: RID, axis_velocity: Vector3) -> void:
    pass;

func body_set_collision_layer(body: RID, layer: int) -> void:
    pass;

func body_set_collision_mask(body: RID, mask: int) -> void:
    pass;

func body_set_enable_continuous_collision_detection(body: RID, enable: bool) -> void:
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

func body_set_ray_pickable(body: RID, enable: bool) -> void:
    pass;

func body_set_shape(body: RID, shape_idx: int, shape: RID) -> void:
    pass;

func body_set_shape_disabled(body: RID, shape_idx: int, disabled: bool) -> void:
    pass;

func body_set_shape_transform(body: RID, shape_idx: int, transform: Transform3D) -> void:
    pass;

func body_set_space(body: RID, space: RID) -> void:
    pass;

func body_set_state(body: RID, state: int, value: Variant) -> void:
    pass;

func body_test_motion(body: RID, from: Transform3D, motion: Vector3, infinite_inertia: bool, margin: float, result: PhysicsTestMotionResult3D) -> bool:
    pass;

func box_shape_create() -> RID:
    pass;

func capsule_shape_create() -> RID:
    pass;

func concave_polygon_shape_create() -> RID:
    pass;

func cone_twist_joint_get_param(joint: RID, param: int) -> float:
    pass;

func cone_twist_joint_set_param(joint: RID, param: int, value: float) -> void:
    pass;

func convex_polygon_shape_create() -> RID:
    pass;

func custom_shape_create() -> RID:
    pass;

func cylinder_shape_create() -> RID:
    pass;

func free_rid(rid: RID) -> void:
    pass;

func generic_6dof_joint_get_flag(joint: RID, axis: int, flag: int) -> bool:
    pass;

func generic_6dof_joint_get_param(joint: RID, axis: int, param: int) -> float:
    pass;

func generic_6dof_joint_set_flag(joint: RID, axis: int, flag: int, enable: bool) -> void:
    pass;

func generic_6dof_joint_set_param(joint: RID, axis: int, param: int, value: float) -> void:
    pass;

func get_process_info(process_info: int) -> int:
    pass;

func heightmap_shape_create() -> RID:
    pass;

func hinge_joint_get_flag(joint: RID, flag: int) -> bool:
    pass;

func hinge_joint_get_param(joint: RID, param: int) -> float:
    pass;

func hinge_joint_set_flag(joint: RID, flag: int, enabled: bool) -> void:
    pass;

func hinge_joint_set_param(joint: RID, param: int, value: float) -> void:
    pass;

func joint_clear(joint: RID) -> void:
    pass;

func joint_create() -> RID:
    pass;

func joint_get_solver_priority(joint: RID) -> int:
    pass;

func joint_get_type(joint: RID) -> int:
    pass;

func joint_make_cone_twist(joint: RID, body_A: RID, local_ref_A: Transform3D, body_B: RID, local_ref_B: Transform3D) -> void:
    pass;

func joint_make_generic_6dof(joint: RID, body_A: RID, local_ref_A: Transform3D, body_B: RID, local_ref_B: Transform3D) -> void:
    pass;

func joint_make_hinge(joint: RID, body_A: RID, hinge_A: Transform3D, body_B: RID, hinge_B: Transform3D) -> void:
    pass;

func joint_make_pin(joint: RID, body_A: RID, local_A: Vector3, body_B: RID, local_B: Vector3) -> void:
    pass;

func joint_make_slider(joint: RID, body_A: RID, local_ref_A: Transform3D, body_B: RID, local_ref_B: Transform3D) -> void:
    pass;

func joint_set_solver_priority(joint: RID, priority: int) -> void:
    pass;

func pin_joint_get_local_a(joint: RID) -> Vector3:
    pass;

func pin_joint_get_local_b(joint: RID) -> Vector3:
    pass;

func pin_joint_get_param(joint: RID, param: int) -> float:
    pass;

func pin_joint_set_local_a(joint: RID, local_A: Vector3) -> void:
    pass;

func pin_joint_set_local_b(joint: RID, local_B: Vector3) -> void:
    pass;

func pin_joint_set_param(joint: RID, param: int, value: float) -> void:
    pass;

func plane_shape_create() -> RID:
    pass;

func ray_shape_create() -> RID:
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

func slider_joint_get_param(joint: RID, param: int) -> float:
    pass;

func slider_joint_set_param(joint: RID, param: int, value: float) -> void:
    pass;

func soft_body_get_bounds(body: RID) -> AABB:
    pass;

func space_create() -> RID:
    pass;

func space_get_direct_state(space: RID) -> PhysicsDirectSpaceState3D:
    pass;

func space_get_param(space: RID, param: int) -> float:
    pass;

func space_is_active(space: RID) -> bool:
    pass;

func space_set_active(space: RID, active: bool) -> void:
    pass;

func space_set_param(space: RID, param: int, value: float) -> void:
    pass;

func sphere_shape_create() -> RID:
    pass;

