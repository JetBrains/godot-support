#brief The generic 6-degrees-of-freedom joint can implement a variety of joint types by locking certain axes' rotation or translation.
#desc The first 3 DOF axes are linear axes, which represent translation of Bodies, and the latter 3 DOF axes represent the angular motion. Each axis can be either locked, or limited.
class_name Generic6DOFJoint3D

#desc The minimum difference between the pivot points' axes.
const PARAM_LINEAR_LOWER_LIMIT = 0;

#desc The maximum difference between the pivot points' axes.
const PARAM_LINEAR_UPPER_LIMIT = 1;

#desc A factor applied to the movement across the axes. The lower, the slower the movement.
const PARAM_LINEAR_LIMIT_SOFTNESS = 2;

#desc The amount of restitution on the axes' movement. The lower, the more momentum gets lost.
const PARAM_LINEAR_RESTITUTION = 3;

#desc The amount of damping that happens at the linear motion across the axes.
const PARAM_LINEAR_DAMPING = 4;

#desc The velocity the linear motor will try to reach.
const PARAM_LINEAR_MOTOR_TARGET_VELOCITY = 5;

#desc The maximum force the linear motor will apply while trying to reach the velocity target.
const PARAM_LINEAR_MOTOR_FORCE_LIMIT = 6;

const PARAM_LINEAR_SPRING_STIFFNESS = 7;

const PARAM_LINEAR_SPRING_DAMPING = 8;

const PARAM_LINEAR_SPRING_EQUILIBRIUM_POINT = 9;

#desc The minimum rotation in negative direction to break loose and rotate around the axes.
const PARAM_ANGULAR_LOWER_LIMIT = 10;

#desc The minimum rotation in positive direction to break loose and rotate around the axes.
const PARAM_ANGULAR_UPPER_LIMIT = 11;

#desc The speed of all rotations across the axes.
const PARAM_ANGULAR_LIMIT_SOFTNESS = 12;

#desc The amount of rotational damping across the axes. The lower, the more dampening occurs.
const PARAM_ANGULAR_DAMPING = 13;

#desc The amount of rotational restitution across the axes. The lower, the more restitution occurs.
const PARAM_ANGULAR_RESTITUTION = 14;

#desc The maximum amount of force that can occur, when rotating around the axes.
const PARAM_ANGULAR_FORCE_LIMIT = 15;

#desc When rotating across the axes, this error tolerance factor defines how much the correction gets slowed down. The lower, the slower.
const PARAM_ANGULAR_ERP = 16;

#desc Target speed for the motor at the axes.
const PARAM_ANGULAR_MOTOR_TARGET_VELOCITY = 17;

#desc Maximum acceleration for the motor at the axes.
const PARAM_ANGULAR_MOTOR_FORCE_LIMIT = 18;

const PARAM_ANGULAR_SPRING_STIFFNESS = 19;

const PARAM_ANGULAR_SPRING_DAMPING = 20;

const PARAM_ANGULAR_SPRING_EQUILIBRIUM_POINT = 21;

#desc Represents the size of the [enum Param] enum.
const PARAM_MAX = 22;

#desc If enabled, linear motion is possible within the given limits.
const FLAG_ENABLE_LINEAR_LIMIT = 0;

#desc If enabled, rotational motion is possible within the given limits.
const FLAG_ENABLE_ANGULAR_LIMIT = 1;

const FLAG_ENABLE_LINEAR_SPRING = 3;

const FLAG_ENABLE_ANGULAR_SPRING = 2;

#desc If enabled, there is a rotational motor across these axes.
const FLAG_ENABLE_MOTOR = 4;

#desc If enabled, there is a linear motor across these axes.
const FLAG_ENABLE_LINEAR_MOTOR = 5;

#desc Represents the size of the [enum Flag] enum.
const FLAG_MAX = 6;


#desc The amount of rotational damping across the X axis.
#desc The lower, the longer an impulse from one side takes to travel to the other side.
var angular_limit_x/damping: float;

#desc If [code]true[/code], rotation across the X axis is limited.
var angular_limit_x/enabled: bool;

#desc When rotating across the X axis, this error tolerance factor defines how much the correction gets slowed down. The lower, the slower.
var angular_limit_x/erp: float;

#desc The maximum amount of force that can occur, when rotating around the X axis.
var angular_limit_x/force_limit: float;

#desc The minimum rotation in negative direction to break loose and rotate around the X axis.
var angular_limit_x/lower_angle: float;

#desc The amount of rotational restitution across the X axis. The lower, the more restitution occurs.
var angular_limit_x/restitution: float;

#desc The speed of all rotations across the X axis.
var angular_limit_x/softness: float;

#desc The minimum rotation in positive direction to break loose and rotate around the X axis.
var angular_limit_x/upper_angle: float;

#desc The amount of rotational damping across the Y axis. The lower, the more dampening occurs.
var angular_limit_y/damping: float;

#desc If [code]true[/code], rotation across the Y axis is limited.
var angular_limit_y/enabled: bool;

#desc When rotating across the Y axis, this error tolerance factor defines how much the correction gets slowed down. The lower, the slower.
var angular_limit_y/erp: float;

#desc The maximum amount of force that can occur, when rotating around the Y axis.
var angular_limit_y/force_limit: float;

#desc The minimum rotation in negative direction to break loose and rotate around the Y axis.
var angular_limit_y/lower_angle: float;

#desc The amount of rotational restitution across the Y axis. The lower, the more restitution occurs.
var angular_limit_y/restitution: float;

#desc The speed of all rotations across the Y axis.
var angular_limit_y/softness: float;

#desc The minimum rotation in positive direction to break loose and rotate around the Y axis.
var angular_limit_y/upper_angle: float;

#desc The amount of rotational damping across the Z axis. The lower, the more dampening occurs.
var angular_limit_z/damping: float;

#desc If [code]true[/code], rotation across the Z axis is limited.
var angular_limit_z/enabled: bool;

#desc When rotating across the Z axis, this error tolerance factor defines how much the correction gets slowed down. The lower, the slower.
var angular_limit_z/erp: float;

#desc The maximum amount of force that can occur, when rotating around the Z axis.
var angular_limit_z/force_limit: float;

#desc The minimum rotation in negative direction to break loose and rotate around the Z axis.
var angular_limit_z/lower_angle: float;

#desc The amount of rotational restitution across the Z axis. The lower, the more restitution occurs.
var angular_limit_z/restitution: float;

#desc The speed of all rotations across the Z axis.
var angular_limit_z/softness: float;

#desc The minimum rotation in positive direction to break loose and rotate around the Z axis.
var angular_limit_z/upper_angle: float;

#desc If [code]true[/code], a rotating motor at the X axis is enabled.
var angular_motor_x/enabled: bool;

#desc Maximum acceleration for the motor at the X axis.
var angular_motor_x/force_limit: float;

#desc Target speed for the motor at the X axis.
var angular_motor_x/target_velocity: float;

#desc If [code]true[/code], a rotating motor at the Y axis is enabled.
var angular_motor_y/enabled: bool;

#desc Maximum acceleration for the motor at the Y axis.
var angular_motor_y/force_limit: float;

#desc Target speed for the motor at the Y axis.
var angular_motor_y/target_velocity: float;

#desc If [code]true[/code], a rotating motor at the Z axis is enabled.
var angular_motor_z/enabled: bool;

#desc Maximum acceleration for the motor at the Z axis.
var angular_motor_z/force_limit: float;

#desc Target speed for the motor at the Z axis.
var angular_motor_z/target_velocity: float;

var angular_spring_x/damping: float;

var angular_spring_x/enabled: bool;

var angular_spring_x/equilibrium_point: float;

var angular_spring_x/stiffness: float;

var angular_spring_y/damping: float;

var angular_spring_y/enabled: bool;

var angular_spring_y/equilibrium_point: float;

var angular_spring_y/stiffness: float;

var angular_spring_z/damping: float;

var angular_spring_z/enabled: bool;

var angular_spring_z/equilibrium_point: float;

var angular_spring_z/stiffness: float;

#desc The amount of damping that happens at the X motion.
var linear_limit_x/damping: float;

#desc If [code]true[/code], the linear motion across the X axis is limited.
var linear_limit_x/enabled: bool;

#desc The minimum difference between the pivot points' X axis.
var linear_limit_x/lower_distance: float;

#desc The amount of restitution on the X axis movement. The lower, the more momentum gets lost.
var linear_limit_x/restitution: float;

#desc A factor applied to the movement across the X axis. The lower, the slower the movement.
var linear_limit_x/softness: float;

#desc The maximum difference between the pivot points' X axis.
var linear_limit_x/upper_distance: float;

#desc The amount of damping that happens at the Y motion.
var linear_limit_y/damping: float;

#desc If [code]true[/code], the linear motion across the Y axis is limited.
var linear_limit_y/enabled: bool;

#desc The minimum difference between the pivot points' Y axis.
var linear_limit_y/lower_distance: float;

#desc The amount of restitution on the Y axis movement. The lower, the more momentum gets lost.
var linear_limit_y/restitution: float;

#desc A factor applied to the movement across the Y axis. The lower, the slower the movement.
var linear_limit_y/softness: float;

#desc The maximum difference between the pivot points' Y axis.
var linear_limit_y/upper_distance: float;

#desc The amount of damping that happens at the Z motion.
var linear_limit_z/damping: float;

#desc If [code]true[/code], the linear motion across the Z axis is limited.
var linear_limit_z/enabled: bool;

#desc The minimum difference between the pivot points' Z axis.
var linear_limit_z/lower_distance: float;

#desc The amount of restitution on the Z axis movement. The lower, the more momentum gets lost.
var linear_limit_z/restitution: float;

#desc A factor applied to the movement across the Z axis. The lower, the slower the movement.
var linear_limit_z/softness: float;

#desc The maximum difference between the pivot points' Z axis.
var linear_limit_z/upper_distance: float;

#desc If [code]true[/code], then there is a linear motor on the X axis. It will attempt to reach the target velocity while staying within the force limits.
var linear_motor_x/enabled: bool;

#desc The maximum force the linear motor can apply on the X axis while trying to reach the target velocity.
var linear_motor_x/force_limit: float;

#desc The speed that the linear motor will attempt to reach on the X axis.
var linear_motor_x/target_velocity: float;

#desc If [code]true[/code], then there is a linear motor on the Y axis. It will attempt to reach the target velocity while staying within the force limits.
var linear_motor_y/enabled: bool;

#desc The maximum force the linear motor can apply on the Y axis while trying to reach the target velocity.
var linear_motor_y/force_limit: float;

#desc The speed that the linear motor will attempt to reach on the Y axis.
var linear_motor_y/target_velocity: float;

#desc If [code]true[/code], then there is a linear motor on the Z axis. It will attempt to reach the target velocity while staying within the force limits.
var linear_motor_z/enabled: bool;

#desc The maximum force the linear motor can apply on the Z axis while trying to reach the target velocity.
var linear_motor_z/force_limit: float;

#desc The speed that the linear motor will attempt to reach on the Z axis.
var linear_motor_z/target_velocity: float;

var linear_spring_x/damping: float;

var linear_spring_x/enabled: bool;

var linear_spring_x/equilibrium_point: float;

var linear_spring_x/stiffness: float;

var linear_spring_y/damping: float;

var linear_spring_y/enabled: bool;

var linear_spring_y/equilibrium_point: float;

var linear_spring_y/stiffness: float;

var linear_spring_z/damping: float;

var linear_spring_z/enabled: bool;

var linear_spring_z/equilibrium_point: float;

var linear_spring_z/stiffness: float;



func get_flag_x(flag: int) -> bool:
	pass;

func get_flag_y(flag: int) -> bool:
	pass;

func get_flag_z(flag: int) -> bool:
	pass;

func get_param_x(param: int) -> float:
	pass;

func get_param_y(param: int) -> float:
	pass;

func get_param_z(param: int) -> float:
	pass;

func set_flag_x(flag: int, value: bool) -> void:
	pass;

func set_flag_y(flag: int, value: bool) -> void:
	pass;

func set_flag_z(flag: int, value: bool) -> void:
	pass;

func set_param_x(param: int, value: float) -> void:
	pass;

func set_param_y(param: int, value: float) -> void:
	pass;

func set_param_z(param: int, value: float) -> void:
	pass;


