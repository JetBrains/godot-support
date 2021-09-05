extends Joint3D
class_name HingeJoint3D

const PARAM_BIAS = 0;
const PARAM_LIMIT_UPPER = 1;
const PARAM_LIMIT_LOWER = 2;
const PARAM_LIMIT_BIAS = 3;
const PARAM_LIMIT_SOFTNESS = 4;
const PARAM_LIMIT_RELAXATION = 5;
const PARAM_MOTOR_TARGET_VELOCITY = 6;
const PARAM_MOTOR_MAX_IMPULSE = 7;
const PARAM_MAX = 8;
const FLAG_USE_LIMIT = 0;
const FLAG_ENABLE_MOTOR = 1;
const FLAG_MAX = 2;

var angular_limit/bias: float setget set_param, get_param;
var angular_limit/enable: bool setget set_flag, get_flag;
var angular_limit/lower: float setget _set_lower_limit, _get_lower_limit;
var angular_limit/relaxation: float setget set_param, get_param;
var angular_limit/softness: float setget set_param, get_param;
var angular_limit/upper: float setget _set_upper_limit, _get_upper_limit;
var motor/enable: bool setget set_flag, get_flag;
var motor/max_impulse: float setget set_param, get_param;
var motor/target_velocity: float setget set_param, get_param;
var params/bias: float setget set_param, get_param;

func get_flag(flag: int) -> bool:
    pass;

func get_param(param: int) -> float:
    pass;

func set_flag(flag: int, enabled: bool) -> void:
    pass;

func set_param(param: int, value: float) -> void:
    pass;

