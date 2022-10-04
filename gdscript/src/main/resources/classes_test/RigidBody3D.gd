#brief Physics Body which is moved by 3D physics simulation. Useful for objects that have gravity and can be pushed by other objects.
#desc This is the node that implements full 3D physics. This means that you do not control a RigidBody3D directly. Instead, you can apply forces to it (gravity, impulses, etc.), and the physics simulation will calculate the resulting movement, collision, bouncing, rotating, etc.
#desc You can switch the body's behavior using [member lock_rotation], [member freeze], and [member freeze_mode].
#desc [b]Note:[/b] Don't change a RigidBody3D's position every frame or very often. Sporadic changes work fine, but physics runs at a different granularity (fixed Hz) than usual rendering (process callback) and maybe even in a separate thread, so changing this from a process loop may result in strange behavior. If you need to directly affect the body's state, use [method _integrate_forces], which allows you to directly access the physics state.
#desc If you need to override the default physics behavior, you can write a custom force integration function. See [member custom_integrator].
class_name RigidBody3D

#desc Static body freeze mode (default). The body is not affected by gravity and forces. It can be only moved by user code and doesn't collide with other bodies along its path.
const FREEZE_MODE_STATIC = 0;

#desc Kinematic body freeze mode. Similar to [constant FREEZE_MODE_STATIC], but collides with other bodies along its path when moved. Useful for a frozen body that needs to be animated.
const FREEZE_MODE_KINEMATIC = 1;

#desc In this mode, the body's center of mass is calculated automatically based on its shapes.
const CENTER_OF_MASS_MODE_AUTO = 0;

#desc In this mode, the body's center of mass is set through [member center_of_mass]. Defaults to the body's origin position.
const CENTER_OF_MASS_MODE_CUSTOM = 1;

#desc In this mode, the body's damping value is added to any value set in areas or the default value.
const DAMP_MODE_COMBINE = 0;

#desc In this mode, the body's damping value replaces any value set in areas or the default value.
const DAMP_MODE_REPLACE = 1;


#desc Damps the body's rotation. By default, the body will use the [b]Default Angular Damp[/b] in [b]Project > Project Settings > Physics > 3d[/b] or any value override set by an [Area3D] the body is in. Depending on [member angular_damp_mode], you can set [member angular_damp] to be added to or to replace the body's damping value.
#desc See [member ProjectSettings.physics/3d/default_angular_damp] for more details about damping.
var angular_damp: float;

#desc Defines how [member angular_damp] is applied. See [enum DampMode] for possible values.
var angular_damp_mode: int;

#desc The RigidBody3D's rotational velocity in [i]radians[/i] per second.
var angular_velocity: Vector3;

#desc If [code]true[/code], the body can enter sleep mode when there is no movement. See [member sleeping].
var can_sleep: bool;

#desc The body's custom center of mass, relative to the body's origin position, when [member center_of_mass_mode] is set to [constant CENTER_OF_MASS_MODE_CUSTOM]. This is the balanced point of the body, where applied forces only cause linear acceleration. Applying forces outside of the center of mass causes angular acceleration.
#desc When [member center_of_mass_mode] is set to [constant CENTER_OF_MASS_MODE_AUTO] (default value), the center of mass is automatically computed.
var center_of_mass: Vector3;

#desc Defines the way the body's center of mass is set. See [enum CenterOfMassMode] for possible values.
var center_of_mass_mode: int;

#desc The body's total constant positional forces applied during each physics update.
#desc See [method add_constant_force] and [method add_constant_central_force].
var constant_force: Vector3;

#desc The body's total constant rotational forces applied during each physics update.
#desc See [method add_constant_torque].
var constant_torque: Vector3;

#desc If [code]true[/code], the RigidBody3D will emit signals when it collides with another RigidBody3D.
#desc [b]Note:[/b] By default the maximum contacts reported is set to 0, meaning nothing will be recorded, see [member max_contacts_reported].
var contact_monitor: bool;

#desc If [code]true[/code], continuous collision detection is used.
#desc Continuous collision detection tries to predict where a moving body will collide, instead of moving it and correcting its movement if it collided. Continuous collision detection is more precise, and misses fewer impacts by small, fast-moving objects. Not using continuous collision detection is faster to compute, but can miss small, fast-moving objects.
var continuous_cd: bool;

#desc If [code]true[/code], internal force integration will be disabled (like gravity or air friction) for this body. Other than collision response, the body will only move as determined by the [method _integrate_forces] function, if defined.
var custom_integrator: bool;

#desc If [code]true[/code], the body is frozen. Gravity and forces are not applied anymore.
#desc See [member freeze_mode] to set the body's behavior when frozen.
#desc For a body that is always frozen, use [StaticBody3D] or [AnimatableBody3D] instead.
var freeze: bool;

#desc The body's freeze mode. Can be used to set the body's behavior when [member freeze] is enabled. See [enum FreezeMode] for possible values.
#desc For a body that is always frozen, use [StaticBody3D] or [AnimatableBody3D] instead.
var freeze_mode: int;

#desc This is multiplied by the global 3D gravity setting found in [b]Project > Project Settings > Physics > 3d[/b] to produce RigidBody3D's gravity. For example, a value of 1 will be normal gravity, 2 will apply double gravity, and 0.5 will apply half gravity to this object.
var gravity_scale: float;

#desc The body's moment of inertia. This is like mass, but for rotation: it determines how much torque it takes to rotate the body on each axis. The moment of inertia is usually computed automatically from the mass and the shapes, but this property allows you to set a custom value.
#desc If set to [code]Vector3.ZERO[/code], inertia is automatically computed (default value).
var inertia: Vector3;

#desc Damps the body's movement. By default, the body will use the [b]Default Linear Damp[/b] in [b]Project > Project Settings > Physics > 3d[/b] or any value override set by an [Area3D] the body is in. Depending on [member linear_damp_mode], you can set [member linear_damp] to be added to or to replace the body's damping value.
#desc See [member ProjectSettings.physics/3d/default_linear_damp] for more details about damping.
var linear_damp: float;

#desc Defines how [member linear_damp] is applied. See [enum DampMode] for possible values.
var linear_damp_mode: int;

#desc The body's linear velocity in units per second. Can be used sporadically, but [b]don't set this every frame[/b], because physics may run in another thread and runs at a different granularity. Use [method _integrate_forces] as your process loop for precise control of the body state.
var linear_velocity: Vector3;

#desc If [code]true[/code], the body cannot rotate. Gravity and forces only apply linear movement.
var lock_rotation: bool;

#desc The body's mass.
var mass: float;

#desc The maximum number of contacts that will be recorded. Requires a value greater than 0 and [member contact_monitor] to be set to [code]true[/code] to start to register contacts. Use [method get_contact_count] to retrieve the count or [method get_colliding_bodies] to retrieve bodies that have been collided with.
#desc [b]Note:[/b] The number of contacts is different from the number of collisions. Collisions between parallel edges will result in two contacts (one at each end), and collisions between parallel faces will result in four contacts (one at each corner).
var max_contacts_reported: int;

#desc The physics material override for the body.
#desc If a material is assigned to this property, it will be used instead of any other physics material, such as an inherited one.
var physics_material_override: PhysicsMaterial;

#desc If [code]true[/code], the body will not move and will not calculate forces until woken up by another body through, for example, a collision, or by using the [method apply_impulse] or [method apply_force] methods.
var sleeping: bool;



#desc Called during physics processing, allowing you to read and safely modify the simulation state for the object. By default, it works in addition to the usual physics behavior, but the [member custom_integrator] property allows you to disable the default behavior and do fully custom force integration for a body.
virtual func _integrate_forces() -> void:
	pass;

#desc Adds a constant directional force without affecting rotation that keeps being applied over time until cleared with [code]constant_force = Vector3(0, 0, 0)[/code].
#desc This is equivalent to using [method add_constant_force] at the body's center of mass.
func add_constant_central_force() -> void:
	pass;

#desc Adds a constant positioned force to the body that keeps being applied over time until cleared with [code]constant_force = Vector3(0, 0, 0)[/code].
#desc [param position] is the offset from the body origin in global coordinates.
func add_constant_force(force: Vector3, position: Vector3) -> void:
	pass;

#desc Adds a constant rotational force without affecting position that keeps being applied over time until cleared with [code]constant_torque = Vector3(0, 0, 0)[/code].
func add_constant_torque() -> void:
	pass;

#desc Applies a directional force without affecting rotation. A force is time dependent and meant to be applied every physics update.
#desc This is equivalent to using [method apply_force] at the body's center of mass.
func apply_central_force() -> void:
	pass;

#desc Applies a directional impulse without affecting rotation.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc This is equivalent to using [method apply_impulse] at the body's center of mass.
func apply_central_impulse() -> void:
	pass;

#desc Applies a positioned force to the body. A force is time dependent and meant to be applied every physics update.
#desc [param position] is the offset from the body origin in global coordinates.
func apply_force(force: Vector3, position: Vector3) -> void:
	pass;

#desc Applies a positioned impulse to the body.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
#desc [param position] is the offset from the body origin in global coordinates.
func apply_impulse(impulse: Vector3, position: Vector3) -> void:
	pass;

#desc Applies a rotational force without affecting position. A force is time dependent and meant to be applied every physics update.
func apply_torque() -> void:
	pass;

#desc Applies a rotational impulse to the body without affecting the position.
#desc An impulse is time-independent! Applying an impulse every frame would result in a framerate-dependent force. For this reason, it should only be used when simulating one-time impacts (use the "_force" functions otherwise).
func apply_torque_impulse() -> void:
	pass;

#desc Returns a list of the bodies colliding with this one. Requires [member contact_monitor] to be set to [code]true[/code] and [member max_contacts_reported] to be set high enough to detect all the collisions.
#desc [b]Note:[/b] The result of this test is not immediate after moving objects. For performance, list of collisions is updated once per frame and before the physics step. Consider using signals instead.
func get_colliding_bodies() -> Node3D[]:
	pass;

#desc Returns the number of contacts this body has with other bodies. By default, this returns 0 unless bodies are configured to monitor contacts (see [member contact_monitor]).
#desc [b]Note:[/b] To retrieve the colliding bodies, use [method get_colliding_bodies].
func get_contact_count() -> int:
	pass;

#desc Returns the inverse inertia tensor basis. This is used to calculate the angular acceleration resulting from a torque applied to the [RigidBody3D].
func get_inverse_inertia_tensor() -> Basis:
	pass;

#desc Sets an axis velocity. The velocity in the given vector axis will be set as the given vector length. This is useful for jumping behavior.
func set_axis_velocity() -> void:
	pass;


