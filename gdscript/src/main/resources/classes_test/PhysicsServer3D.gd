#brief Server interface for low-level physics access.
#desc PhysicsServer3D is the server responsible for all 3D physics. It can create many kinds of physics objects, but does not insert them on the node tree.
class_name PhysicsServer3D

#desc The [Joint3D] is a [PinJoint3D].
const JOINT_TYPE_PIN = 0;

#desc The [Joint3D] is a [HingeJoint3D].
const JOINT_TYPE_HINGE = 1;

#desc The [Joint3D] is a [SliderJoint3D].
const JOINT_TYPE_SLIDER = 2;

#desc The [Joint3D] is a [ConeTwistJoint3D].
const JOINT_TYPE_CONE_TWIST = 3;

#desc The [Joint3D] is a [Generic6DOFJoint3D].
const JOINT_TYPE_6DOF = 4;

#desc Represents the size of the [enum JointType] enum.
const JOINT_TYPE_MAX = 5;

#desc The strength with which the pinned objects try to stay in positional relation to each other.
#desc The higher, the stronger.
const PIN_JOINT_BIAS = 0;

#desc The strength with which the pinned objects try to stay in velocity relation to each other.
#desc The higher, the stronger.
const PIN_JOINT_DAMPING = 1;

#desc If above 0, this value is the maximum value for an impulse that this Joint3D puts on its ends.
const PIN_JOINT_IMPULSE_CLAMP = 2;

#desc The speed with which the two bodies get pulled together when they move in different directions.
const HINGE_JOINT_BIAS = 0;

#desc The maximum rotation across the Hinge.
const HINGE_JOINT_LIMIT_UPPER = 1;

#desc The minimum rotation across the Hinge.
const HINGE_JOINT_LIMIT_LOWER = 2;

#desc The speed with which the rotation across the axis perpendicular to the hinge gets corrected.
const HINGE_JOINT_LIMIT_BIAS = 3;

const HINGE_JOINT_LIMIT_SOFTNESS = 4;

#desc The lower this value, the more the rotation gets slowed down.
const HINGE_JOINT_LIMIT_RELAXATION = 5;

#desc Target speed for the motor.
const HINGE_JOINT_MOTOR_TARGET_VELOCITY = 6;

#desc Maximum acceleration for the motor.
const HINGE_JOINT_MOTOR_MAX_IMPULSE = 7;

#desc If [code]true[/code], the Hinge has a maximum and a minimum rotation.
const HINGE_JOINT_FLAG_USE_LIMIT = 0;

#desc If [code]true[/code], a motor turns the Hinge.
const HINGE_JOINT_FLAG_ENABLE_MOTOR = 1;

#desc The maximum difference between the pivot points on their X axis before damping happens.
const SLIDER_JOINT_LINEAR_LIMIT_UPPER = 0;

#desc The minimum difference between the pivot points on their X axis before damping happens.
const SLIDER_JOINT_LINEAR_LIMIT_LOWER = 1;

#desc A factor applied to the movement across the slider axis once the limits get surpassed. The lower, the slower the movement.
const SLIDER_JOINT_LINEAR_LIMIT_SOFTNESS = 2;

#desc The amount of restitution once the limits are surpassed. The lower, the more velocityenergy gets lost.
const SLIDER_JOINT_LINEAR_LIMIT_RESTITUTION = 3;

#desc The amount of damping once the slider limits are surpassed.
const SLIDER_JOINT_LINEAR_LIMIT_DAMPING = 4;

#desc A factor applied to the movement across the slider axis as long as the slider is in the limits. The lower, the slower the movement.
const SLIDER_JOINT_LINEAR_MOTION_SOFTNESS = 5;

#desc The amount of restitution inside the slider limits.
const SLIDER_JOINT_LINEAR_MOTION_RESTITUTION = 6;

#desc The amount of damping inside the slider limits.
const SLIDER_JOINT_LINEAR_MOTION_DAMPING = 7;

#desc A factor applied to the movement across axes orthogonal to the slider.
const SLIDER_JOINT_LINEAR_ORTHOGONAL_SOFTNESS = 8;

#desc The amount of restitution when movement is across axes orthogonal to the slider.
const SLIDER_JOINT_LINEAR_ORTHOGONAL_RESTITUTION = 9;

#desc The amount of damping when movement is across axes orthogonal to the slider.
const SLIDER_JOINT_LINEAR_ORTHOGONAL_DAMPING = 10;

#desc The upper limit of rotation in the slider.
const SLIDER_JOINT_ANGULAR_LIMIT_UPPER = 11;

#desc The lower limit of rotation in the slider.
const SLIDER_JOINT_ANGULAR_LIMIT_LOWER = 12;

#desc A factor applied to the all rotation once the limit is surpassed.
const SLIDER_JOINT_ANGULAR_LIMIT_SOFTNESS = 13;

#desc The amount of restitution of the rotation when the limit is surpassed.
const SLIDER_JOINT_ANGULAR_LIMIT_RESTITUTION = 14;

#desc The amount of damping of the rotation when the limit is surpassed.
const SLIDER_JOINT_ANGULAR_LIMIT_DAMPING = 15;

#desc A factor that gets applied to the all rotation in the limits.
const SLIDER_JOINT_ANGULAR_MOTION_SOFTNESS = 16;

#desc The amount of restitution of the rotation in the limits.
const SLIDER_JOINT_ANGULAR_MOTION_RESTITUTION = 17;

#desc The amount of damping of the rotation in the limits.
const SLIDER_JOINT_ANGULAR_MOTION_DAMPING = 18;

#desc A factor that gets applied to the all rotation across axes orthogonal to the slider.
const SLIDER_JOINT_ANGULAR_ORTHOGONAL_SOFTNESS = 19;

#desc The amount of restitution of the rotation across axes orthogonal to the slider.
const SLIDER_JOINT_ANGULAR_ORTHOGONAL_RESTITUTION = 20;

#desc The amount of damping of the rotation across axes orthogonal to the slider.
const SLIDER_JOINT_ANGULAR_ORTHOGONAL_DAMPING = 21;

#desc Represents the size of the [enum SliderJointParam] enum.
const SLIDER_JOINT_MAX = 22;

#desc Swing is rotation from side to side, around the axis perpendicular to the twist axis.
#desc The swing span defines, how much rotation will not get corrected along the swing axis.
#desc Could be defined as looseness in the [ConeTwistJoint3D].
#desc If below 0.05, this behavior is locked.
const CONE_TWIST_JOINT_SWING_SPAN = 0;

#desc Twist is the rotation around the twist axis, this value defined how far the joint can twist.
#desc Twist is locked if below 0.05.
const CONE_TWIST_JOINT_TWIST_SPAN = 1;

#desc The speed with which the swing or twist will take place.
#desc The higher, the faster.
const CONE_TWIST_JOINT_BIAS = 2;

#desc The ease with which the Joint3D twists, if it's too low, it takes more force to twist the joint.
const CONE_TWIST_JOINT_SOFTNESS = 3;

#desc Defines, how fast the swing- and twist-speed-difference on both sides gets synced.
const CONE_TWIST_JOINT_RELAXATION = 4;

#desc The minimum difference between the pivot points' axes.
const G6DOF_JOINT_LINEAR_LOWER_LIMIT = 0;

#desc The maximum difference between the pivot points' axes.
const G6DOF_JOINT_LINEAR_UPPER_LIMIT = 1;

#desc A factor that gets applied to the movement across the axes. The lower, the slower the movement.
const G6DOF_JOINT_LINEAR_LIMIT_SOFTNESS = 2;

#desc The amount of restitution on the axes movement. The lower, the more velocity-energy gets lost.
const G6DOF_JOINT_LINEAR_RESTITUTION = 3;

#desc The amount of damping that happens at the linear motion across the axes.
const G6DOF_JOINT_LINEAR_DAMPING = 4;

#desc The velocity that the joint's linear motor will attempt to reach.
const G6DOF_JOINT_LINEAR_MOTOR_TARGET_VELOCITY = 5;

#desc The maximum force that the linear motor can apply while trying to reach the target velocity.
const G6DOF_JOINT_LINEAR_MOTOR_FORCE_LIMIT = 6;

#desc The minimum rotation in negative direction to break loose and rotate around the axes.
const G6DOF_JOINT_ANGULAR_LOWER_LIMIT = 10;

#desc The minimum rotation in positive direction to break loose and rotate around the axes.
const G6DOF_JOINT_ANGULAR_UPPER_LIMIT = 11;

#desc A factor that gets multiplied onto all rotations across the axes.
const G6DOF_JOINT_ANGULAR_LIMIT_SOFTNESS = 12;

#desc The amount of rotational damping across the axes. The lower, the more dampening occurs.
const G6DOF_JOINT_ANGULAR_DAMPING = 13;

#desc The amount of rotational restitution across the axes. The lower, the more restitution occurs.
const G6DOF_JOINT_ANGULAR_RESTITUTION = 14;

#desc The maximum amount of force that can occur, when rotating around the axes.
const G6DOF_JOINT_ANGULAR_FORCE_LIMIT = 15;

#desc When correcting the crossing of limits in rotation across the axes, this error tolerance factor defines how much the correction gets slowed down. The lower, the slower.
const G6DOF_JOINT_ANGULAR_ERP = 16;

#desc Target speed for the motor at the axes.
const G6DOF_JOINT_ANGULAR_MOTOR_TARGET_VELOCITY = 17;

#desc Maximum acceleration for the motor at the axes.
const G6DOF_JOINT_ANGULAR_MOTOR_FORCE_LIMIT = 18;

#desc If set, linear motion is possible within the given limits.
const G6DOF_JOINT_FLAG_ENABLE_LINEAR_LIMIT = 0;

#desc If set, rotational motion is possible.
const G6DOF_JOINT_FLAG_ENABLE_ANGULAR_LIMIT = 1;

#desc If set, there is a rotational motor across these axes.
const G6DOF_JOINT_FLAG_ENABLE_MOTOR = 4;

#desc If set, there is a linear motor on this axis that targets a specific velocity.
const G6DOF_JOINT_FLAG_ENABLE_LINEAR_MOTOR = 5;

#desc The [Shape3D] is a [WorldBoundaryShape3D].
const SHAPE_WORLD_BOUNDARY = 0;

#desc The [Shape3D] is a [SeparationRayShape3D].
const SHAPE_SEPARATION_RAY = 1;

#desc The [Shape3D] is a [SphereShape3D].
const SHAPE_SPHERE = 2;

#desc The [Shape3D] is a [BoxShape3D].
const SHAPE_BOX = 3;

#desc The [Shape3D] is a [CapsuleShape3D].
const SHAPE_CAPSULE = 4;

#desc The [Shape3D] is a [CylinderShape3D].
const SHAPE_CYLINDER = 5;

#desc The [Shape3D] is a [ConvexPolygonShape3D].
const SHAPE_CONVEX_POLYGON = 6;

#desc The [Shape3D] is a [ConcavePolygonShape3D].
const SHAPE_CONCAVE_POLYGON = 7;

#desc The [Shape3D] is a [HeightMapShape3D].
const SHAPE_HEIGHTMAP = 8;

#desc The [Shape3D] is used internally for a soft body. Any attempt to create this kind of shape results in an error.
const SHAPE_SOFT_BODY = 9;

#desc This constant is used internally by the engine. Any attempt to create this kind of shape results in an error.
const SHAPE_CUSTOM = 10;

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

#desc Constant to set/get the magnitude of area-specific wind force.
const AREA_PARAM_WIND_FORCE_MAGNITUDE = 11;

#desc Constant to set/get the 3D vector that specifies the origin from which an area-specific wind blows.
const AREA_PARAM_WIND_SOURCE = 12;

#desc Constant to set/get the 3D vector that specifies the direction in which an area-specific wind blows.
const AREA_PARAM_WIND_DIRECTION = 13;

#desc Constant to set/get the exponential rate at which wind force decreases with distance from its origin.
const AREA_PARAM_WIND_ATTENUATION_FACTOR = 14;

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

#desc Constant to set/get the number of solver iterations for contacts and constraints. The greater the number of iterations, the more accurate the collisions and constraints will be. However, a greater number of iterations requires more CPU power, which can decrease performance.
const SPACE_PARAM_SOLVER_ITERATIONS = 7;

const BODY_AXIS_LINEAR_X = 1;

const BODY_AXIS_LINEAR_Y = 2;

const BODY_AXIS_LINEAR_Z = 4;

const BODY_AXIS_ANGULAR_X = 8;

const BODY_AXIS_ANGULAR_Y = 16;

const BODY_AXIS_ANGULAR_Z = 32;




#desc Adds a shape to the area, along with a transform matrix. Shapes are usually referenced by their index, so you should track which shape has a given index.
func area_add_shape(area: RID, shape: RID, transform: Transform3D, disabled: bool) -> void:
	pass;

#desc Assigns the area to a descendant of [Object], so it can exist in the node tree.
func area_attach_object_instance_id(area: RID, id: int) -> void:
	pass;

#desc Removes all shapes from an area. It does not delete the shapes, so they can be reassigned later.
func area_clear_shapes(area: RID) -> void:
	pass;

#desc Creates an [Area3D].
func area_create() -> RID:
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

#desc Returns an area parameter value. A list of available parameters is on the [enum AreaParameter] constants.
func area_get_param(area: RID, param: int) -> Variant:
	pass;

#desc Returns the [RID] of the nth shape of an area.
func area_get_shape(area: RID, shape_idx: int) -> RID:
	pass;

#desc Returns the number of shapes assigned to an area.
func area_get_shape_count(area: RID) -> int:
	pass;

#desc Returns the transform matrix of a shape within an area.
func area_get_shape_transform(area: RID, shape_idx: int) -> Transform3D:
	pass;

#desc Returns the space assigned to the area.
func area_get_space(area: RID) -> RID:
	pass;

#desc Returns the transform matrix for an area.
func area_get_transform(area: RID) -> Transform3D:
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

#desc Sets the value for an area parameter. A list of available parameters is on the [enum AreaParameter] constants.
func area_set_param(area: RID, param: int, value: Variant) -> void:
	pass;

#desc Sets object pickable with rays.
func area_set_ray_pickable(area: RID, enable: bool) -> void:
	pass;

#desc Substitutes a given area shape by another. The old shape is selected by its index, the new one by its [RID].
func area_set_shape(area: RID, shape_idx: int, shape: RID) -> void:
	pass;

func area_set_shape_disabled(area: RID, shape_idx: int, disabled: bool) -> void:
	pass;

#desc Sets the transform matrix for an area shape.
func area_set_shape_transform(area: RID, shape_idx: int, transform: Transform3D) -> void:
	pass;

#desc Assigns a space to the area.
func area_set_space(area: RID, space: RID) -> void:
	pass;

#desc Sets the transform matrix for an area.
func area_set_transform(area: RID, transform: Transform3D) -> void:
	pass;

#desc Adds a body to the list of bodies exempt from collisions.
func body_add_collision_exception(body: RID, excepted_body: RID) -> void:
	pass;

#desc Adds a constant directional force without affecting rotation that keeps being applied over time until cleared with [code]body_set_constant_force(body, Vector3(0, 0, 0))[/code].
#desc This is equivalent to using [method body_add_constant_force] at the body's center of mass.
func body_add_constant_central_force(body: RID, force: Vector3) -> void:
	pass;

#desc Adds a constant positioned force to the body that keeps being applied over time until cleared with [code]body_set_constant_force(body, Vector3(0, 0, 0))[/code].
#desc [param position] is the offset from the body origin in global coordinates.
func body_add_constant_force(body: RID, force: Vector3, position: Vector3) -> void:
	pass;

#desc Adds a constant rotational force without affecting position that keeps being applied over time until cleared with [code]body_set_constant_torque(body, Vector3(0, 0, 0))[/code].
func body_add_constant_torque(body: RID, torque: Vector3) -> void:
	pass;

#desc Adds a shape to the body, along with a transform matrix. Shapes are usually referenced by their index, so you should track which shape has a given index.
func body_add_shape(body: RID, shape: RID, transform: Transform3D, disabled: bool) -> void:
	pass;

#desc Applies a directional force without affecting rotation. A force is time dependent and meant to be applied every physics update.
#desc This is equivalent to using [method body_apply_force] at the body's center of mass.
func body_apply_central_force(body: RID, force: Vector3) -> void:
	pass;

#desc Applies a directional impulse without affecting rotation.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc This is equivalent to using [method body_apply_impulse] at the body's center of mass.
func body_apply_central_impulse(body: RID, impulse: Vector3) -> void:
	pass;

#desc Applies a positioned force to the body. A force is time dependent and meant to be applied every physics update.
#desc [param position] is the offset from the body origin in global coordinates.
func body_apply_force(body: RID, force: Vector3, position: Vector3) -> void:
	pass;

#desc Applies a positioned impulse to the body.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc [param position] is the offset from the body origin in global coordinates.
func body_apply_impulse(body: RID, impulse: Vector3, position: Vector3) -> void:
	pass;

#desc Applies a rotational force without affecting position. A force is time dependent and meant to be applied every physics update.
func body_apply_torque(body: RID, torque: Vector3) -> void:
	pass;

#desc Applies a rotational impulse to the body without affecting the position.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
func body_apply_torque_impulse(body: RID, impulse: Vector3) -> void:
	pass;

#desc Assigns the area to a descendant of [Object], so it can exist in the node tree.
func body_attach_object_instance_id(body: RID, id: int) -> void:
	pass;

#desc Removes all shapes from a body.
func body_clear_shapes(body: RID) -> void:
	pass;

func body_create() -> RID:
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
func body_get_constant_force(body: RID) -> Vector3:
	pass;

#desc Returns the body's total constant rotational forces applied during each physics update.
#desc See [method body_add_constant_torque].
func body_get_constant_torque(body: RID) -> Vector3:
	pass;

#desc Returns the [PhysicsDirectBodyState3D] of the body. Returns [code]null[/code] if the body is destroyed or removed from the physics space.
func body_get_direct_state(body: RID) -> PhysicsDirectBodyState3D:
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

#desc Returns the value of a body parameter. A list of available parameters is on the [enum BodyParameter] constants.
func body_get_param(body: RID, param: int) -> Variant:
	pass;

#desc Returns the [RID] of the nth shape of a body.
func body_get_shape(body: RID, shape_idx: int) -> RID:
	pass;

#desc Returns the number of shapes assigned to a body.
func body_get_shape_count(body: RID) -> int:
	pass;

#desc Returns the transform matrix of a body shape.
func body_get_shape_transform(body: RID, shape_idx: int) -> Transform3D:
	pass;

#desc Returns the [RID] of the space assigned to a body.
func body_get_space(body: RID) -> RID:
	pass;

#desc Returns a body state.
func body_get_state(body: RID, state: int) -> Variant:
	pass;

func body_is_axis_locked(body: RID, axis: int) -> bool:
	pass;

#desc If [code]true[/code], the continuous collision detection mode is enabled.
func body_is_continuous_collision_detection_enabled(body: RID) -> bool:
	pass;

#desc Returns whether a body uses a callback function to calculate its own physics (see [method body_set_force_integration_callback]).
func body_is_omitting_force_integration(body: RID) -> bool:
	pass;

#desc Removes a body from the list of bodies exempt from collisions.
#desc Continuous collision detection tries to predict where a moving body will collide, instead of moving it and correcting its movement if it collided.
func body_remove_collision_exception(body: RID, excepted_body: RID) -> void:
	pass;

#desc Removes a shape from a body. The shape is not deleted, so it can be reused afterwards.
func body_remove_shape(body: RID, shape_idx: int) -> void:
	pass;

#desc Restores the default inertia and center of mass based on shapes to cancel any custom values previously set using [method body_set_param].
func body_reset_mass_properties(body: RID) -> void:
	pass;

func body_set_axis_lock(body: RID, axis: int, lock: bool) -> void:
	pass;

#desc Sets an axis velocity. The velocity in the given vector axis will be set as the given vector length. This is useful for jumping behavior.
func body_set_axis_velocity(body: RID, axis_velocity: Vector3) -> void:
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
func body_set_constant_force(body: RID, force: Vector3) -> void:
	pass;

#desc Sets the body's total constant rotational forces applied during each physics update.
#desc See [method body_add_constant_torque].
func body_set_constant_torque(body: RID, torque: Vector3) -> void:
	pass;

#desc If [code]true[/code], the continuous collision detection mode is enabled.
#desc Continuous collision detection tries to predict where a moving body will collide, instead of moving it and correcting its movement if it collided.
func body_set_enable_continuous_collision_detection(body: RID, enable: bool) -> void:
	pass;

#desc Sets the function used to calculate physics for an object, if that object allows it (see [method body_set_omit_force_integration]).
#desc The force integration function takes 2 arguments:
#desc [code]state:[/code] [PhysicsDirectBodyState3D] used to retrieve and modify the body's state.
#desc [code]userdata:[/code] Optional user data, if it was passed when calling [code]body_set_force_integration_callback[/code].
func body_set_force_integration_callback(body: RID, callable: Callable, userdata: Variant) -> void:
	pass;

#desc Sets the maximum contacts to report. Bodies can keep a log of the contacts with other bodies. This is enabled by setting the maximum number of contacts reported to a number greater than 0.
func body_set_max_contacts_reported(body: RID, amount: int) -> void:
	pass;

#desc Sets the body mode, from one of the [enum BodyMode] constants.
func body_set_mode(body: RID, mode: int) -> void:
	pass;

#desc Sets whether a body uses a callback function to calculate its own physics (see [method body_set_force_integration_callback]).
func body_set_omit_force_integration(body: RID, enable: bool) -> void:
	pass;

#desc Sets a body parameter. A list of available parameters is on the [enum BodyParameter] constants.
func body_set_param(body: RID, param: int, value: Variant) -> void:
	pass;

#desc Sets the body pickable with rays if [param enable] is set.
func body_set_ray_pickable(body: RID, enable: bool) -> void:
	pass;

#desc Substitutes a given body shape by another. The old shape is selected by its index, the new one by its [RID].
func body_set_shape(body: RID, shape_idx: int, shape: RID) -> void:
	pass;

func body_set_shape_disabled(body: RID, shape_idx: int, disabled: bool) -> void:
	pass;

#desc Sets the transform matrix for a body shape.
func body_set_shape_transform(body: RID, shape_idx: int, transform: Transform3D) -> void:
	pass;

#desc Assigns a space to the body (see [method space_create]).
func body_set_space(body: RID, space: RID) -> void:
	pass;

#desc Sets a body state (see [enum BodyState] constants).
func body_set_state(body: RID, state: int, value: Variant) -> void:
	pass;

#desc Returns [code]true[/code] if a collision would result from moving along a motion vector from a given point in space. [PhysicsTestMotionParameters3D] is passed to set motion parameters. [PhysicsTestMotionResult3D] can be passed to return additional information.
func body_test_motion(body: RID, parameters: PhysicsTestMotionParameters3D, result: PhysicsTestMotionResult3D) -> bool:
	pass;

func box_shape_create() -> RID:
	pass;

func capsule_shape_create() -> RID:
	pass;

func concave_polygon_shape_create() -> RID:
	pass;

#desc Gets a cone_twist_joint parameter (see [enum ConeTwistJointParam] constants).
func cone_twist_joint_get_param(joint: RID, param: int) -> float:
	pass;

#desc Sets a cone_twist_joint parameter (see [enum ConeTwistJointParam] constants).
func cone_twist_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

func convex_polygon_shape_create() -> RID:
	pass;

func custom_shape_create() -> RID:
	pass;

func cylinder_shape_create() -> RID:
	pass;

#desc Destroys any of the objects created by PhysicsServer3D. If the [RID] passed is not one of the objects that can be created by PhysicsServer3D, an error will be sent to the console.
func free_rid(rid: RID) -> void:
	pass;

#desc Gets a generic_6_DOF_joint flag (see [enum G6DOFJointAxisFlag] constants).
func generic_6dof_joint_get_flag(joint: RID, axis: int, flag: int) -> bool:
	pass;

#desc Gets a generic_6_DOF_joint parameter (see [enum G6DOFJointAxisParam] constants).
func generic_6dof_joint_get_param(joint: RID, axis: int, param: int) -> float:
	pass;

#desc Sets a generic_6_DOF_joint flag (see [enum G6DOFJointAxisFlag] constants).
func generic_6dof_joint_set_flag(joint: RID, axis: int, flag: int, enable: bool) -> void:
	pass;

#desc Sets a generic_6_DOF_joint parameter (see [enum G6DOFJointAxisParam] constants).
func generic_6dof_joint_set_param(joint: RID, axis: int, param: int, value: float) -> void:
	pass;

#desc Returns information about the current state of the 3D physics engine. See [enum ProcessInfo] for a list of available states.
func get_process_info(process_info: int) -> int:
	pass;

func heightmap_shape_create() -> RID:
	pass;

#desc Gets a hinge_joint flag (see [enum HingeJointFlag] constants).
func hinge_joint_get_flag(joint: RID, flag: int) -> bool:
	pass;

#desc Gets a hinge_joint parameter (see [enum HingeJointParam]).
func hinge_joint_get_param(joint: RID, param: int) -> float:
	pass;

#desc Sets a hinge_joint flag (see [enum HingeJointFlag] constants).
func hinge_joint_set_flag(joint: RID, flag: int, enabled: bool) -> void:
	pass;

#desc Sets a hinge_joint parameter (see [enum HingeJointParam] constants).
func hinge_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

func joint_clear(joint: RID) -> void:
	pass;

func joint_create() -> RID:
	pass;

#desc Gets the priority value of the Joint3D.
func joint_get_solver_priority(joint: RID) -> int:
	pass;

#desc Returns the type of the Joint3D.
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

#desc Sets the priority value of the Joint3D.
func joint_set_solver_priority(joint: RID, priority: int) -> void:
	pass;

#desc Returns position of the joint in the local space of body a of the joint.
func pin_joint_get_local_a(joint: RID) -> Vector3:
	pass;

#desc Returns position of the joint in the local space of body b of the joint.
func pin_joint_get_local_b(joint: RID) -> Vector3:
	pass;

#desc Gets a pin_joint parameter (see [enum PinJointParam] constants).
func pin_joint_get_param(joint: RID, param: int) -> float:
	pass;

#desc Sets position of the joint in the local space of body a of the joint.
func pin_joint_set_local_a(joint: RID, local_A: Vector3) -> void:
	pass;

#desc Sets position of the joint in the local space of body b of the joint.
func pin_joint_set_local_b(joint: RID, local_B: Vector3) -> void:
	pass;

#desc Sets a pin_joint parameter (see [enum PinJointParam] constants).
func pin_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

func separation_ray_shape_create() -> RID:
	pass;

#desc Activates or deactivates the 3D physics engine.
func set_active(active: bool) -> void:
	pass;

#desc Returns the shape data.
func shape_get_data(shape: RID) -> Variant:
	pass;

#desc Returns the type of shape (see [enum ShapeType] constants).
func shape_get_type(shape: RID) -> int:
	pass;

#desc Sets the shape data that defines its shape and size. The data to be passed depends on the kind of shape created [method shape_get_type].
func shape_set_data(shape: RID, data: Variant) -> void:
	pass;

#desc Gets a slider_joint parameter (see [enum SliderJointParam] constants).
func slider_joint_get_param(joint: RID, param: int) -> float:
	pass;

#desc Gets a slider_joint parameter (see [enum SliderJointParam] constants).
func slider_joint_set_param(joint: RID, param: int, value: float) -> void:
	pass;

func soft_body_get_bounds(body: RID) -> AABB:
	pass;

#desc Creates a space. A space is a collection of parameters for the physics engine that can be assigned to an area or a body. It can be assigned to an area with [method area_set_space], or to a body with [method body_set_space].
func space_create() -> RID:
	pass;

#desc Returns the state of a space, a [PhysicsDirectSpaceState3D]. This object can be used to make collision/intersection queries.
func space_get_direct_state(space: RID) -> PhysicsDirectSpaceState3D:
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

#desc Sets the value for a space parameter. A list of available parameters is on the [enum SpaceParameter] constants.
func space_set_param(space: RID, param: int, value: float) -> void:
	pass;

func sphere_shape_create() -> RID:
	pass;

func world_boundary_shape_create() -> RID:
	pass;


