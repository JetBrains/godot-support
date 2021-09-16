extends Joint3D
class_name Generic6DOFJoint3D
const PARAM_LINEAR_LOWER_LIMIT = 0;
const PARAM_LINEAR_UPPER_LIMIT = 1;
const PARAM_LINEAR_LIMIT_SOFTNESS = 2;
const PARAM_LINEAR_RESTITUTION = 3;
const PARAM_LINEAR_DAMPING = 4;
const PARAM_LINEAR_MOTOR_TARGET_VELOCITY = 5;
const PARAM_LINEAR_MOTOR_FORCE_LIMIT = 6;
const PARAM_LINEAR_SPRING_STIFFNESS = 7;
const PARAM_LINEAR_SPRING_DAMPING = 8;
const PARAM_LINEAR_SPRING_EQUILIBRIUM_POINT = 9;
const PARAM_ANGULAR_LOWER_LIMIT = 10;
const PARAM_ANGULAR_UPPER_LIMIT = 11;
const PARAM_ANGULAR_LIMIT_SOFTNESS = 12;
const PARAM_ANGULAR_DAMPING = 13;
const PARAM_ANGULAR_RESTITUTION = 14;
const PARAM_ANGULAR_FORCE_LIMIT = 15;
const PARAM_ANGULAR_ERP = 16;
const PARAM_ANGULAR_MOTOR_TARGET_VELOCITY = 17;
const PARAM_ANGULAR_MOTOR_FORCE_LIMIT = 18;
const PARAM_ANGULAR_SPRING_STIFFNESS = 19;
const PARAM_ANGULAR_SPRING_DAMPING = 20;
const PARAM_ANGULAR_SPRING_EQUILIBRIUM_POINT = 21;
const PARAM_MAX = 22;
const FLAG_ENABLE_LINEAR_LIMIT = 0;
const FLAG_ENABLE_ANGULAR_LIMIT = 1;
const FLAG_ENABLE_LINEAR_SPRING = 3;
const FLAG_ENABLE_ANGULAR_SPRING = 2;
const FLAG_ENABLE_MOTOR = 4;
const FLAG_ENABLE_LINEAR_MOTOR = 5;
const FLAG_MAX = 6;

var angular_limit_x/damping: float;
var angular_limit_x/enabled: bool;
var angular_limit_x/erp: float;
var angular_limit_x/force_limit: float;
var angular_limit_x/lower_angle: float;
var angular_limit_x/restitution: float;
var angular_limit_x/softness: float;
var angular_limit_x/upper_angle: float;
var angular_limit_y/damping: float;
var angular_limit_y/enabled: bool;
var angular_limit_y/erp: float;
var angular_limit_y/force_limit: float;
var angular_limit_y/lower_angle: float;
var angular_limit_y/restitution: float;
var angular_limit_y/softness: float;
var angular_limit_y/upper_angle: float;
var angular_limit_z/damping: float;
var angular_limit_z/enabled: bool;
var angular_limit_z/erp: float;
var angular_limit_z/force_limit: float;
var angular_limit_z/lower_angle: float;
var angular_limit_z/restitution: float;
var angular_limit_z/softness: float;
var angular_limit_z/upper_angle: float;
var angular_motor_x/enabled: bool;
var angular_motor_x/force_limit: float;
var angular_motor_x/target_velocity: float;
var angular_motor_y/enabled: bool;
var angular_motor_y/force_limit: float;
var angular_motor_y/target_velocity: float;
var angular_motor_z/enabled: bool;
var angular_motor_z/force_limit: float;
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
var linear_limit_x/damping: float;
var linear_limit_x/enabled: bool;
var linear_limit_x/lower_distance: float;
var linear_limit_x/restitution: float;
var linear_limit_x/softness: float;
var linear_limit_x/upper_distance: float;
var linear_limit_y/damping: float;
var linear_limit_y/enabled: bool;
var linear_limit_y/lower_distance: float;
var linear_limit_y/restitution: float;
var linear_limit_y/softness: float;
var linear_limit_y/upper_distance: float;
var linear_limit_z/damping: float;
var linear_limit_z/enabled: bool;
var linear_limit_z/lower_distance: float;
var linear_limit_z/restitution: float;
var linear_limit_z/softness: float;
var linear_limit_z/upper_distance: float;
var linear_motor_x/enabled: bool;
var linear_motor_x/force_limit: float;
var linear_motor_x/target_velocity: float;
var linear_motor_y/enabled: bool;
var linear_motor_y/force_limit: float;
var linear_motor_y/target_velocity: float;
var linear_motor_z/enabled: bool;
var linear_motor_z/force_limit: float;
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
