extends Joint3D
#brief A hinge between two 3D PhysicsBodies.
#desc A HingeJoint3D normally uses the Z axis of body A as the hinge axis, another axis can be specified when adding it manually though. See also [Generic6DOFJoint3D].
class_name HingeJoint3D

#desc The speed with which the two bodies get pulled together when they move in different directions.
const PARAM_BIAS = 0;

#desc The maximum rotation. Only active if [member angular_limit/enable] is [code]true[/code].
const PARAM_LIMIT_UPPER = 1;

#desc The minimum rotation. Only active if [member angular_limit/enable] is [code]true[/code].
const PARAM_LIMIT_LOWER = 2;

#desc The speed with which the rotation across the axis perpendicular to the hinge gets corrected.
const PARAM_LIMIT_BIAS = 3;

const PARAM_LIMIT_SOFTNESS = 4;

#desc The lower this value, the more the rotation gets slowed down.
const PARAM_LIMIT_RELAXATION = 5;

#desc Target speed for the motor.
const PARAM_MOTOR_TARGET_VELOCITY = 6;

#desc Maximum acceleration for the motor.
const PARAM_MOTOR_MAX_IMPULSE = 7;

#desc Represents the size of the [enum Param] enum.
const PARAM_MAX = 8;

#desc If [code]true[/code], the hinges maximum and minimum rotation, defined by [member angular_limit/lower] and [member angular_limit/upper] has effects.
const FLAG_USE_LIMIT = 0;

#desc When activated, a motor turns the hinge.
const FLAG_ENABLE_MOTOR = 1;

#desc Represents the size of the [enum Flag] enum.
const FLAG_MAX = 2;


#desc The speed with which the rotation across the axis perpendicular to the hinge gets corrected.
var angular_limit/bias: float;

#desc If [code]true[/code], the hinges maximum and minimum rotation, defined by [member angular_limit/lower] and [member angular_limit/upper] has effects.
var angular_limit/enable: bool;

#desc The minimum rotation. Only active if [member angular_limit/enable] is [code]true[/code].
var angular_limit/lower: float;

#desc The lower this value, the more the rotation gets slowed down.
var angular_limit/relaxation: float;

var angular_limit/softness: float;

#desc The maximum rotation. Only active if [member angular_limit/enable] is [code]true[/code].
var angular_limit/upper: float;

#desc When activated, a motor turns the hinge.
var motor/enable: bool;

#desc Maximum acceleration for the motor.
var motor/max_impulse: float;

#desc Target speed for the motor.
var motor/target_velocity: float;

#desc The speed with which the two bodies get pulled together when they move in different directions.
var params/bias: float;



#desc Returns the value of the specified flag.
func get_flag(flag: int) -> bool:
	pass;

#desc Returns the value of the specified parameter.
func get_param(param: int) -> float:
	pass;

#desc If [code]true[/code], enables the specified flag.
func set_flag(flag: int, enabled: bool) -> void:
	pass;

#desc Sets the value of the specified parameter.
func set_param(param: int, value: float) -> void:
	pass;


