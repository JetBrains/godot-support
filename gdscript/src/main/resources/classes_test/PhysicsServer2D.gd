#brief Server interface for low-level 2D physics access.
#desc PhysicsServer2D is the server responsible for all 2D physics. It can create many kinds of physics objects, but does not insert them on the node tree.
class_name PhysicsServer2D

#desc Constant to set/get the maximum distance a pair of bodies has to move before their collision status has to be recalculated.
const SPACE_PARAM_CONTACT_RECYCLE_RADIUS = 0;

#desc Constant to set/get the maximum distance a shape can be from another before they are considered separated and the contact is discarded.
const SPACE_PARAM_CONTACT_MAX_SEPARATION = 1;

#desc Constant to set/get the maximum distance a shape can penetrate another shape before it is considered a collision.
const SPACE_PARAM_CONTACT_MAX_ALLOWED_PENETRATION = 2;

#desc Constant to set/get the default solver bias for all physics contacts. A solver bias is a factor controlling how much two objects "rebound", after overlapping, to avoid leaving them in that state because of numerical imprecision.
const SPACE_PARAM_CONTACT_DEFAULT_BIAS = 3;

#desc Constant to set/get the threshold linear velocity of activity. A body marked as potentially inactive for both linear and angular velocity will be put to sleep after the time given.
const SPACE_PARAM_BODY_LINEAR_VELOCITY_SLEEP_THRESHOLD = 4;

#desc Constant to set/get the threshold angular velocity of activity. A body marked as potentially inactive for both linear and angular velocity will be put to sleep after the time given.
const SPACE_PARAM_BODY_ANGULAR_VELOCITY_SLEEP_THRESHOLD = 5;

#desc Constant to set/get the maximum time of activity. A body marked as potentially inactive for both linear and angular velocity will be put to sleep after this time.
const SPACE_PARAM_BODY_TIME_TO_SLEEP = 6;

#desc Constant to set/get the default solver bias for all physics constraints. A solver bias is a factor controlling how much two objects "rebound", after violating a constraint, to avoid leaving them in that state because of numerical imprecision.
const SPACE_PARAM_CONSTRAINT_DEFAULT_BIAS = 7;

#desc Constant to set/get the number of solver iterations for all contacts and constraints. The greater the number of iterations, the more accurate the collisions will be. However, a greater number of iterations requires more CPU power, which can decrease performance.
const SPACE_PARAM_SOLVER_ITERATIONS = 8;

#desc This is the constant for creating world boundary shapes. A world boundary shape is an [i]infinite[/i] line with an origin point, and a normal. Thus, it can be used for front/behind checks.
const SHAPE_WORLD_BOUNDARY = 0;

#desc This is the constant for creating separation ray shapes. A separation ray is defined by a length and separates itself from what is touching its far endpoint. Useful for character controllers.
const SHAPE_SEPARATION_RAY = 1;

#desc This is the constant for creating segment shapes. A segment shape is a [i]finite[/i] line from a point A to a point B. It can be checked for intersections.
const SHAPE_SEGMENT = 2;

#desc This is the constant for creating circle shapes. A circle shape only has a radius. It can be used for intersections and inside/outside checks.
const SHAPE_CIRCLE = 3;

#desc This is the constant for creating rectangle shapes. A rectangle shape is defined by a width and a height. It can be used for intersections and inside/outside checks.
const SHAPE_RECTANGLE = 4;

#desc This is the constant for creating capsule shapes. A capsule shape is defined by a radius and a length. It can be used for intersections and inside/outside checks.
const SHAPE_CAPSULE = 5;

#desc This is the constant for creating convex polygon shapes. A polygon is defined by a list of points. It can be used for intersections and inside/outside checks. Unlike the [member CollisionPolygon2D.polygon] property, polygons modified with [method shape_set_data] do not verify that the points supplied form is a convex polygon.
const SHAPE_CONVEX_POLYGON = 6;

#desc This is the constant for creating concave polygon shapes. A polygon is defined by a list of points. It can be used for intersections checks, but not for inside/outside checks.
const SHAPE_CONCAVE_POLYGON = 7;

#desc This constant is used internally by the engine. Any attempt to create this kind of shape results in an error.
const SHAPE_CUSTOM = 8;

#desc Constant to set/get gravity override mode in an area. See [enum AreaSpaceOverrideMode] for possible values.
const AREA_PARAM_GRAVITY_OVERRIDE_MODE = 0;

#desc Constant to set/get gravity strength in an area.
const AREA_PARAM_GRAVITY = 1;

#desc Constant to set/get gravity vector/center in an area.
const AREA_PARAM_GRAVITY_VECTOR = 2;

#desc Constant to set/get whether the gravity vector of an area is a direction, or a center point.
const AREA_PARAM_GRAVITY_IS_POINT = 3;

#desc Constant to set/get the falloff factor for point gravity of an area. The greater this value is, the faster the strength of gravity decreases with the square of distance.
const AREA_PARAM_GRAVITY_DISTANCE_SCALE = 4;

#desc This constant was used to set/get the falloff factor for point gravity. It has been superseded by [constant AREA_PARAM_GRAVITY_DISTANCE_SCALE].
const AREA_PARAM_GRAVITY_POINT_ATTENUATION = 5;

#desc Constant to set/get linear damping override mode in an area. See [enum AreaSpaceOverrideMode] for possible values.
const AREA_PARAM_LINEAR_DAMP_OVERRIDE_MODE = 6;

#desc Constant to set/get the linear damping factor of an area.
const AREA_PARAM_LINEAR_DAMP = 7;

#desc Constant to set/get angular damping override mode in an area. See [enum AreaSpaceOverrideMode] for possible values.
const AREA_PARAM_ANGULAR_DAMP_OVERRIDE_MODE = 8;

#desc Constant to set/get the angular damping factor of an area.
const AREA_PARAM_ANGULAR_DAMP = 9;

#desc Constant to set/get the priority (order of processing) of an area.
const AREA_PARAM_PRIORITY = 10;

#desc This area does not affect gravity/damp. These are generally areas that exist only to detect collisions, and objects entering or exiting them.
const AREA_SPACE_OVERRIDE_DISABLED = 0;

#desc This area adds its gravity/damp values to whatever has been calculated so far. This way, many overlapping areas can combine their physics to make interesting effects.
const AREA_SPACE_OVERRIDE_COMBINE = 1;

#desc This area adds its gravity/damp values to whatever has been calculated so far. Then stops taking into account the rest of the areas, even the default one.
const AREA_SPACE_OVERRIDE_COMBINE_REPLACE = 2;

#desc This area replaces any gravity/damp, even the default one, and stops taking into account the rest of the areas.
const AREA_SPACE_OVERRIDE_REPLACE = 3;

#desc This area replaces any gravity/damp calculated so far, but keeps calculating the rest of the areas, down to the default one.
const AREA_SPACE_OVERRIDE_REPLACE_COMBINE = 4;

#desc Constant for static bodies. In this mode, a body can be only moved by user code and doesn't collide with other bodies along its path when moved.
const BODY_MODE_STATIC = 0;

#desc Constant for kinematic bodies. In this mode, a body can be only moved by user code and collides with other bodies along its path.
const BODY_MODE_KINEMATIC = 1;

#desc Constant for rigid bodies. In this mode, a body can be pushed by other bodies and has forces applied.
const BODY_MODE_RIGID = 2;

#desc Constant for linear rigid bodies. In this mode, a body can not rotate, and only its linear velocity is affected by external forces.
const BODY_MODE_RIGID_LINEAR = 3;

#desc Constant to set/get a body's bounce factor.
const BODY_PARAM_BOUNCE = 0;

#desc Constant to set/get a body's friction.
const BODY_PARAM_FRICTION = 1;

#desc Constant to set/get a body's mass.
const BODY_PARAM_MASS = 2;

#desc Constant to set/get a body's inertia.
const BODY_PARAM_INERTIA = 3;

#desc Constant to set/get a body's center of mass position in the body's local coordinate system.
const BODY_PARAM_CENTER_OF_MASS = 4;

#desc Constant to set/get a body's gravity multiplier.
const BODY_PARAM_GRAVITY_SCALE = 5;

#desc Constant to set/get a body's linear dampening mode. See [enum BodyDampMode] for possible values.
const BODY_PARAM_LINEAR_DAMP_MODE = 6;

#desc Constant to set/get a body's angular dampening mode. See [enum BodyDampMode] for possible values.
const BODY_PARAM_ANGULAR_DAMP_MODE = 7;

#desc Constant to set/get a body's linear dampening factor.
const BODY_PARAM_LINEAR_DAMP = 8;

#desc Constant to set/get a body's angular dampening factor.
const BODY_PARAM_ANGULAR_DAMP = 9;

#desc Represents the size of the [enum BodyParameter] enum.
const BODY_PARAM_MAX = 10;

#desc The body's damping value is added to any value set in areas or the default value.
const BODY_DAMP_MODE_COMBINE = 0;

#desc The body's damping value replaces any value set in areas or the default value.
const BODY_DAMP_MODE_REPLACE = 1;

#desc Constant to set/get the current transform matrix of the body.
const BODY_STATE_TRANSFORM = 0;

#desc Constant to set/get the current linear velocity of the body.
const BODY_STATE_LINEAR_VELOCITY = 1;

#desc Constant to set/get the current angular velocity of the body.
const BODY_STATE_ANGULAR_VELOCITY = 2;

#desc Constant to sleep/wake up a body, or to get whether it is sleeping.
const BODY_STATE_SLEEPING = 3;

#desc Constant to set/get whether the body can sleep.
const BODY_STATE_CAN_SLEEP = 4;

#desc Constant to create pin joints.
const JOINT_TYPE_PIN = 0;

#desc Constant to create groove joints.
const JOINT_TYPE_GROOVE = 1;

#desc Constant to create damped spring joints.
const JOINT_TYPE_DAMPED_SPRING = 2;

#desc Represents the size of the [enum JointType] enum.
const JOINT_TYPE_MAX = 3;

const JOINT_PARAM_BIAS = 0;

const JOINT_PARAM_MAX_BIAS = 1;

const JOINT_PARAM_MAX_FORCE = 2;

const PIN_JOINT_SOFTNESS = 0;

#desc Sets the resting length of the spring joint. The joint will always try to go to back this length when pulled apart.
const DAMPED_SPRING_REST_LENGTH = 0;

#desc Sets the stiffness of the spring joint. The joint applies a force equal to the stiffness times the distance from its resting length.
const DAMPED_SPRING_STIFFNESS = 1;

#desc Sets the damping ratio of the spring joint. A value of 0 indicates an undamped spring, while 1 causes the system to reach equilibrium as fast as possible (critical damping).
const DAMPED_SPRING_DAMPING = 2;

#desc Disables continuous collision detection. This is the fastest way to detect body collisions, but can miss small, fast-moving objects.
const CCD_MODE_DISABLED = 0;

#desc Enables continuous collision detection by raycasting. It is faster than shapecasting, but less precise.
const CCD_MODE_CAST_RAY = 1;

#desc Enables continuous collision detection by shapecasting. It is the slowest CCD method, and the most precise.
const CCD_MODE_CAST_SHAPE = 2;

#desc The value of the first parameter and area callback function receives, when an object enters one of its shapes.
const AREA_BODY_ADDED = 0;

#desc The value of the first parameter and area callback function receives, when an object exits one of its shapes.
const AREA_BODY_REMOVED = 1;

#desc Constant to get the number of objects that are not sleeping.
const INFO_ACTIVE_OBJECTS = 0;

#desc Constant to get the number of possible collisions.
const INFO_COLLISION_PAIRS = 1;

#desc Constant to get the number of space regions where a collision could occur.
const INFO_ISLAND_COUNT = 2;




#desc Adds a shape to the area, along with a transform matrix. Shapes are usually referenced by their index, so you should track which shape has a given index.
func area_add_shape(area: RID, shape: RID, transform: Transform2D, disabled: bool) -> void:
	pass;

func area_attach_canvas_instance_id(area: RID, id: int) -> void:
	pass;

#desc Assigns the area to a descendant of [Object], so it can exist in the node tree.
func area_attach_object_instance_id(area: RID, id: int) -> void:
	pass;

#desc Removes all shapes from an area. It does not delete the shapes, so they can be reassigned later.
func area_clear_shapes(area: RID) -> void:
	pass;

#desc Creates an [Area2D]. After creating an [Area2D] with this method, assign it to a space using [method area_set_space] to use the created [Area2D] in the physics world.
func area_create() -> RID:
	pass;

func area_get_canvas_instance_id(area: RID) -> int:
	pass;

#desc Returns the physics layer or layers an area belongs to.
func area_get_collision_layer(area: RID) -> int:
	pass;

#desc Returns the physics layer or layers an area can contact with.
func area_get_collision_mask(area: RID) -> int:
	pass;

#desc Gets the instance ID of the object the area is assigned to.
func area_get_object_instance_id(area: RID) -> int:
	pass;

#desc Returns an area parameter value. See [enum AreaParameter] for a list of available parameters.
func area_get_param(area: RID, param: int) -> Variant:
	pass;

#desc Returns the [RID] of the nth shape of an area.
func area_get_shape(area: RID, shape_idx: int) -> RID:
	pass;

#desc Returns the number of shapes assigned to an area.
func area_get_shape_count(area: RID) -> int:
	pass;

#desc Returns the transform matrix of a shape within an area.
func area_get_shape_transform(area: RID, shape_idx: int) -> Transform2D:
	pass;

#desc Returns the space assigned to the area.
func area_get_space(area: RID) -> RID:
	pass;

#desc Returns the transform matrix for an area.
func area_get_transform(area: RID) -> Transform2D:
	pass;

#desc Removes a shape from an area. It does not delete the shape, so it can be reassigned later.
func area_remove_shape(area: RID, shape_idx: int) -> void:
	pass;

func area_set_area_monitor_callback(area: RID, callback: Callable) -> void:
	pass;

#desc Assigns the area to one or many physics layers.
func area_set_collision_layer(area: RID, layer: int) -> void:
	pass;

#desc Sets which physics layers the area will monitor.
func area_set_collision_mask(area: RID, mask: int) -> void:
	pass;

#desc Sets the function to call when any body/area enters or exits the area. This callback will be called for any object interacting with the area, and takes five parameters:
#desc 1: [constant AREA_BODY_ADDED] or [constant AREA_BODY_REMOVED], depending on whether the object entered or exited the area.
#desc 2: [RID] of the object that entered/exited the area.
#desc 3: Instance ID of the object that entered/exited the area.
#desc 4: The shape index of the object that entered/exited the area.
#desc 5: The shape index of the area where the object entered/exited.
func area_set_monitor_callback(area: RID, callback: Callable) -> void:
	pass;

func area_set_monitorable(area: RID, monitorable: bool) -> void:
	pass;

#desc Sets the value for an area parameter. See [enum AreaParameter] for a list of available parameters.
func area_set_param(area: RID, param: int, value: Variant) -> void:
	pass;

#desc Substitutes a given area shape by another. The old shape is selected by its index, the new one by its [RID].
func area_set_shape(area: RID, shape_idx: int, shape: RID) -> void:
	pass;

#desc Disables a given shape in an area.
func area_set_shape_disabled(area: RID, shape_idx: int, disabled: bool) -> void:
	pass;

#desc Sets the transform matrix for an area shape.
func area_set_shape_transform(area: RID, shape_idx: int, transform: Transform2D) -> void:
	pass;

#desc Assigns a space to the area.
func area_set_space(area: RID, space: RID) -> void:
	pass;

#desc Sets the transform matrix for an area.
func area_set_transform(area: RID, transform: Transform2D) -> void:
	pass;

#desc Adds a body to the list of bodies exempt from collisions.
func body_add_collision_exception(body: RID, excepted_body: RID) -> void:
	pass;

#desc Adds a constant directional force without affecting rotation that keeps being applied over time until cleared with [code]body_set_constant_force(body, Vector2(0, 0))[/code].
#desc This is equivalent to using [method body_add_constant_force] at the body's center of mass.
func body_add_constant_central_force(body: RID, force: Vector2) -> void:
	pass;

#desc Adds a constant positioned force to the body that keeps being applied over time until cleared with [code]body_set_constant_force(body, Vector2(0, 0))[/code].
#desc [param position] is the offset from the body origin in global coordinates.
func body_add_constant_force(body: RID, force: Vector2, position: Vector2) -> void:
	pass;

#desc Adds a constant rotational force without affecting position that keeps being applied over time until cleared with [code]body_set_constant_torque(body, 0)[/code].
func body_add_constant_torque(body: RID, torque: float) -> void:
	pass;

#desc Adds a shape to the body, along with a transform matrix. Shapes are usually referenced by their index, so you should track which shape has a given index.
func body_add_shape(body: RID, shape: RID, transform: Transform2D, disabled: bool) -> void:
	pass;

#desc Applies a directional force without affecting rotation. A force is time dependent and meant to be applied every physics update.
#desc This is equivalent to using [method body_apply_force] at the body's center of mass.
func body_apply_central_force(body: RID, force: Vector2) -> void:
	pass;

#desc Applies a directional impulse without affecting rotation.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc This is equivalent to using [method body_apply_impulse] at the body's center of mass.
func body_apply_central_impulse(body: RID, impulse: Vector2) -> void:
	pass;

#desc Applies a positioned force to the body. A force is time dependent and meant to be applied every physics update.
#desc [param position] is the offset from the body origin in global coordinates.
func body_apply_force(body: RID, force: Vector2, position: Vector2) -> void:
	pass;

#desc Applies a positioned impulse to the body.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc [param position] is the offset from the body origin in global coordinates.
func body_apply_impulse(body: RID, impulse: Vector2, position: Vector2) -> void:
	pass;

#desc Applies a rotational force without affecting position. A force is time dependent and meant to be applied every physics update.
func body_apply_torque(body: RID, torque: float) -> void:
	pass;

#desc Applies a rotational impulse to the body without affecting the position.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
func body_apply_torque_impulse(body: RID, impulse: float) -> void:
	pass;

func body_attach_canvas_instance_id(body: RID, id: int) -> void:
	pass;

#desc Assigns the area to a descendant of [Object], so it can exist in the node tree.
func body_attach_object_instance_id(body: RID, id: int) -> void:
	pass;

#desc Removes all shapes from a body.
func body_clear_shapes(body: RID) -> void:
	pass;

#desc Creates a physics body.
func body_create() -> RID:
	pass;

func body_get_canvas_instance_id(body: RID) -> int:
	pass;

#desc Returns the physics layer or layers a body belongs to.
func body_get_collision_layer(body: RID) -> int:
	pass;

#desc Returns the physics layer or layers a body can collide with.
func body_get_collision_mask(body: RID) -> int:
	pass;

#desc Returns the body's collision priority.
func body_get_collision_priority(body: RID) -> float:
	pass;

#desc Returns the body's total constant positional forces applied during each physics update.
#desc See [method body_add_constant_force] and [method body_add_constant_central_force].
func body_get_constant_force(body: RID) -> Vector2:
	pass;

#desc Returns the body's total constant rotational forces applied during each physics update.
#desc See [method body_add_constant_torque].
func body_get_constant_torque(body: RID) -> float:
	pass;

#desc Returns the continuous collision detection mode.
func body_get_continuous_collision_detection_mode(body: RID) -> int:
	pass;

#desc Returns the [PhysicsDirectBodyState2D] of the body. Returns [code]null[/code] if the body is destroyed or removed from the physics space.
func body_get_direct_state(body: RID) -> PhysicsDirectBodyState2D:
	pass;

#desc Returns the maximum contacts that can be reported. See [method body_set_max_contacts_reported].
func body_get_max_contacts_reported(body: RID) -> int:
	pass;

#desc Returns the body mode.
func body_get_mode(body: RID) -> int:
	pass;

#desc Gets the instance ID of the object the area is assigned to.
func body_get_object_instance_id(body: RID) -> int:
	pass;

#desc Returns the value of a body parameter. See [enum BodyParameter] for a list of available parameters.
func body_get_param(body: RID, param: int) -> Variant:
	pass;

#desc Returns the [RID] of the nth shape of a body.
func body_get_shape(body: RID, shape_idx: int) -> RID:
	pass;

#desc Returns the number of shapes assigned to a body.
func body_get_shape_count(body: RID) -> int:
	pass;

#desc Returns the transform matrix of a body shape.
func body_get_shape_transform(body: RID, shape_idx: int) -> Transform2D:
	pass;

#desc Returns the [RID] of the space assigned to a body.
func body_get_space(body: RID) -> RID:
	pass;

#desc Returns a body state.
func body_get_state(body: RID, state: int) -> Variant:
	pass;

#desc Returns whether a body uses a callback function to calculate its own physics (see [method body_set_force_integration_callback]).
func body_is_omitting_force_integration(body: RID) -> bool:
	pass;

#desc Removes a body from the list of bodies exempt from collisions.
func body_remove_collision_exception(body: RID, excepted_body: RID) -> void:
	pass;

#desc Removes a shape from a body. The shape is not deleted, so it can be reused afterwards.
func body_remove_shape(body: RID, shape_idx: int) -> void:
	pass;

#desc Restores the default inertia and center of mass based on shapes to cancel any custom values previously set using [method body_set_param].
func body_reset_mass_properties(body: RID) -> void:
	pass;

#desc Sets an axis velocity. The velocity in the given vector axis will be set as the given vector length. This is useful for jumping behavior.
func body_set_axis_velocity(body: RID, axis_velocity: Vector2) -> void:
	pass;

#desc Sets the physics layer or layers a body belongs to.
func body_set_collision_layer(body: RID, layer: int) -> void:
	pass;

#desc Sets the physics layer or layers a body can collide with.
func body_set_collision_mask(body: RID, mask: int) -> void:
	pass;

#desc Sets the body's collision priority.
func body_set_collision_priority(body: RID, priority: float) -> void:
	pass;

#desc Sets the body's total constant positional forces applied during each physics update.
#desc See [method body_add_constant_force] and [method body_add_constant_central_force].
func body_set_constant_force(body: RID, force: Vector2) -> void:
	pass;

#desc Sets the body's total constant rotational forces applied during each physics update.
#desc See [method body_add_constant_torque].
func body_set_constant_torque(body: RID, torque: float) -> void:
	pass;

#desc Sets the continuous collision detection mode using one of the [enum CCDMode] constants.
#desc Continuous collision detection tries to predict where a moving body will collide, instead of moving it and correcting its movement if it collided.
func body_set_continuous_collision_detection_mode(body: RID, mode: int) -> void:
	pass;

#desc Sets the function used to calculate physics for an object, if that object allows it (see [method body_set_omit_force_integration]).
#desc The force integration function takes 2 arguments:
#desc [code]state:[/code] [PhysicsDirectBodyState2D] used to retrieve and modify the body's state.
#desc [code]userdata:[/code] Optional user data, if it was passed when calling [code]body_set_force_integration_callback[/code].
func body_set_force_integration_callback(body: RID, callable: Callable, userdata: Variant) -> void:
	pass;

#desc Sets the maximum contacts to report. Bodies can keep a log of the contacts with other bodies. This is enabled by setting the maximum number of contacts reported to a number greater than 0.
func body_set_max_contacts_reported(body: RID, amount: int) -> void:
	pass;

#desc Sets the body mode using one of the [enum BodyMode] constants.
func body_set_mode(body: RID, mode: int) -> void:
	pass;

#desc Sets whether a body uses a callback function to calculate its own physics (see [method body_set_force_integration_callback]).
func body_set_omit_force_integration(body: RID, enable: bool) -> void:
	pass;

#desc Sets a body parameter. See [enum BodyParameter] for a list of available parameters.
func body_set_param(body: RID, param: int, value: Variant) -> void:
	pass;

#desc Substitutes a given body shape by another. The old shape is selected by its index, the new one by its [RID].
func body_set_shape(body: RID, shape_idx: int, shape: RID) -> void:
	pass;

#desc Enables one way collision on body if [param enable] is [code]true[/code].
func body_set_shape_as_one_way_collision(body: RID, shape_idx: int, enable: bool, margin: float) -> void:
	pass;

#desc Disables shape in body if [param disabled] is [code]true[/code].
func body_set_shape_disabled(body: RID, shape_idx: int, disabled: bool) -> void:
	pass;

#desc Sets the transform matrix for a body shape.
func body_set_shape_transform(body: RID, shape_idx: int, transform: Transform2D) -> void:
	pass;

#desc Assigns a space to the body (see [method space_create]).
func body_set_space(body: RID, space: RID) -> void:
	pass;

#desc Sets a body state using one of the [enum BodyState] constants.
#desc Note that the method doesn't take effect immediately. The state will change on the next physics frame.
func body_set_state(body: RID, state: int, value: Variant) -> void:
	pass;

#desc Returns [code]true[/code] if a collision would result from moving along a motion vector from a given point in space. [PhysicsTestMotionParameters2D] is passed to set motion parameters. [PhysicsTestMotionResult2D] can be passed to return additional information.
func body_test_motion(body: RID, parameters: PhysicsTestMotionParameters2D, result: PhysicsTestMotionResult2D) -> bool:
	pass;

func capsule_shape_create() -> RID:
	pass;

func circle_shape_create() -> RID:
	pass;

func concave_polygon_shape_create() -> RID:
	pass;

func convex_polygon_shape_create() -> RID:
	pass;

#desc Returns the value of a damped spring joint parameter. See [enum DampedSpringParam] for a list of available parameters.
func damped_spring_joint_get_param(joint: RID, param: int) -> float:
	pass;

#desc Sets a damped spring joint parameter. See [enum DampedSpringParam] for a list of available parameters.
func damped_spring_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

#desc Destroys any of the objects created by PhysicsServer2D. If the [RID] passed is not one of the objects that can be created by PhysicsServer2D, an error will be sent to the console.
func free_rid(rid: RID) -> void:
	pass;

#desc Returns information about the current state of the 2D physics engine. See [enum ProcessInfo] for a list of available states.
func get_process_info(process_info: int) -> int:
	pass;

func joint_clear(joint: RID) -> void:
	pass;

func joint_create() -> RID:
	pass;

#desc Returns the value of a joint parameter.
func joint_get_param(joint: RID, param: int) -> float:
	pass;

#desc Returns a joint's type (see [enum JointType]).
func joint_get_type(joint: RID) -> int:
	pass;

func joint_make_damped_spring(joint: RID, anchor_a: Vector2, anchor_b: Vector2, body_a: RID, body_b: RID) -> void:
	pass;

func joint_make_groove(joint: RID, groove1_a: Vector2, groove2_a: Vector2, anchor_b: Vector2, body_a: RID, body_b: RID) -> void:
	pass;

func joint_make_pin(joint: RID, anchor: Vector2, body_a: RID, body_b: RID) -> void:
	pass;

#desc Sets a joint parameter. See [enum JointParam] for a list of available parameters.
func joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

func rectangle_shape_create() -> RID:
	pass;

func segment_shape_create() -> RID:
	pass;

func separation_ray_shape_create() -> RID:
	pass;

#desc Activates or deactivates the 2D physics engine.
func set_active(active: bool) -> void:
	pass;

#desc Returns the shape data.
func shape_get_data(shape: RID) -> Variant:
	pass;

#desc Returns a shape's type (see [enum ShapeType]).
func shape_get_type(shape: RID) -> int:
	pass;

#desc Sets the shape data that defines its shape and size. The data to be passed depends on the kind of shape created [method shape_get_type].
func shape_set_data(shape: RID, data: Variant) -> void:
	pass;

#desc Creates a space. A space is a collection of parameters for the physics engine that can be assigned to an area or a body. It can be assigned to an area with [method area_set_space], or to a body with [method body_set_space].
func space_create() -> RID:
	pass;

#desc Returns the state of a space, a [PhysicsDirectSpaceState2D]. This object can be used to make collision/intersection queries.
func space_get_direct_state(space: RID) -> PhysicsDirectSpaceState2D:
	pass;

#desc Returns the value of a space parameter.
func space_get_param(space: RID, param: int) -> float:
	pass;

#desc Returns whether the space is active.
func space_is_active(space: RID) -> bool:
	pass;

#desc Marks a space as active. It will not have an effect, unless it is assigned to an area or body.
func space_set_active(space: RID, active: bool) -> void:
	pass;

#desc Sets the value for a space parameter. See [enum SpaceParameter] for a list of available parameters.
func space_set_param(space: RID, param: int, value: float) -> void:
	pass;

func world_boundary_shape_create() -> RID:
	pass;


