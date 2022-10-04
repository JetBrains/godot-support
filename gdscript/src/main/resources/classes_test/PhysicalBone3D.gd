class_name PhysicalBone3D

#desc In this mode, the body's damping value is added to any value set in areas or the default value.
const DAMP_MODE_COMBINE = 0;

#desc In this mode, the body's damping value replaces any value set in areas or the default value.
const DAMP_MODE_REPLACE = 1;

const JOINT_TYPE_NONE = 0;

const JOINT_TYPE_PIN = 1;

const JOINT_TYPE_CONE = 2;

const JOINT_TYPE_HINGE = 3;

const JOINT_TYPE_SLIDER = 4;

const JOINT_TYPE_6DOF = 5;


#desc Damps the body's rotation. By default, the body will use the [b]Default Angular Damp[/b] in [b]Project > Project Settings > Physics > 3d[/b] or any value override set by an [Area3D] the body is in. Depending on [member angular_damp_mode], you can set [member angular_damp] to be added to or to replace the body's damping value.
#desc See [member ProjectSettings.physics/3d/default_angular_damp] for more details about damping.
var angular_damp: float;

#desc Defines how [member angular_damp] is applied. See [enum DampMode] for possible values.
var angular_damp_mode: int;

#desc The PhysicalBone3D's rotational velocity in [i]radians[/i] per second.
var angular_velocity: Vector3;

#desc Sets the body's transform.
var body_offset: Transform3D;

#desc The body's bounciness. Values range from [code]0[/code] (no bounce) to [code]1[/code] (full bounciness).
var bounce: float;

#desc If [code]true[/code], the body is deactivated when there is no movement, so it will not take part in the simulation until it is awakened by an external force.
var can_sleep: bool;

#desc If [code]true[/code], internal force integration will be disabled (like gravity or air friction) for this body. Other than collision response, the body will only move as determined by the [method _integrate_forces] function, if defined.
var custom_integrator: bool;

#desc The body's friction, from [code]0[/code] (frictionless) to [code]1[/code] (max friction).
var friction: float;

#desc This is multiplied by the global 3D gravity setting found in [b]Project > Project Settings > Physics > 3d[/b] to produce the body's gravity. For example, a value of 1 will be normal gravity, 2 will apply double gravity, and 0.5 will apply half gravity to this object.
var gravity_scale: float;

#desc Sets the joint's transform.
var joint_offset: Transform3D;

#desc Sets the joint's rotation in radians.
var joint_rotation: Vector3;

#desc Sets the joint type. See [enum JointType] for possible values.
var joint_type: int;

#desc Damps the body's movement. By default, the body will use the [b]Default Linear Damp[/b] in [b]Project > Project Settings > Physics > 3d[/b] or any value override set by an [Area3D] the body is in. Depending on [member linear_damp_mode], you can set [member linear_damp] to be added to or to replace the body's damping value.
#desc See [member ProjectSettings.physics/3d/default_linear_damp] for more details about damping.
var linear_damp: float;

#desc Defines how [member linear_damp] is applied. See [enum DampMode] for possible values.
var linear_damp_mode: int;

#desc The body's linear velocity in units per second. Can be used sporadically, but [b]don't set this every frame[/b], because physics may run in another thread and runs at a different granularity. Use [method _integrate_forces] as your process loop for precise control of the body state.
var linear_velocity: Vector3;

#desc The body's mass.
var mass: float;



#desc Called during physics processing, allowing you to read and safely modify the simulation state for the object. By default, it works in addition to the usual physics behavior, but the [member custom_integrator] property allows you to disable the default behavior and do fully custom force integration for a body.
virtual func _integrate_forces(state: PhysicsDirectBodyState3D) -> void:
	pass;

func apply_central_impulse(impulse: Vector3) -> void:
	pass;

func apply_impulse(impulse: Vector3, position: Vector3) -> void:
	pass;

func get_bone_id() -> int:
	pass;

func get_simulate_physics() -> bool:
	pass;

func is_simulating_physics() -> bool:
	pass;


