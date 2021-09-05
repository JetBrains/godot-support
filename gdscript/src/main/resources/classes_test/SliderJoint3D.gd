extends Joint3D
class_name SliderJoint3D

const PARAM_LINEAR_LIMIT_UPPER = 0;
const PARAM_LINEAR_LIMIT_LOWER = 1;
const PARAM_LINEAR_LIMIT_SOFTNESS = 2;
const PARAM_LINEAR_LIMIT_RESTITUTION = 3;
const PARAM_LINEAR_LIMIT_DAMPING = 4;
const PARAM_LINEAR_MOTION_SOFTNESS = 5;
const PARAM_LINEAR_MOTION_RESTITUTION = 6;
const PARAM_LINEAR_MOTION_DAMPING = 7;
const PARAM_LINEAR_ORTHOGONAL_SOFTNESS = 8;
const PARAM_LINEAR_ORTHOGONAL_RESTITUTION = 9;
const PARAM_LINEAR_ORTHOGONAL_DAMPING = 10;
const PARAM_ANGULAR_LIMIT_UPPER = 11;
const PARAM_ANGULAR_LIMIT_LOWER = 12;
const PARAM_ANGULAR_LIMIT_SOFTNESS = 13;
const PARAM_ANGULAR_LIMIT_RESTITUTION = 14;
const PARAM_ANGULAR_LIMIT_DAMPING = 15;
const PARAM_ANGULAR_MOTION_SOFTNESS = 16;
const PARAM_ANGULAR_MOTION_RESTITUTION = 17;
const PARAM_ANGULAR_MOTION_DAMPING = 18;
const PARAM_ANGULAR_ORTHOGONAL_SOFTNESS = 19;
const PARAM_ANGULAR_ORTHOGONAL_RESTITUTION = 20;
const PARAM_ANGULAR_ORTHOGONAL_DAMPING = 21;
const PARAM_MAX = 22;

var angular_limit/damping: float setget set_param, get_param;
var angular_limit/lower_angle: float setget _set_lower_limit_angular, _get_lower_limit_angular;
var angular_limit/restitution: float setget set_param, get_param;
var angular_limit/softness: float setget set_param, get_param;
var angular_limit/upper_angle: float setget _set_upper_limit_angular, _get_upper_limit_angular;
var angular_motion/damping: float setget set_param, get_param;
var angular_motion/restitution: float setget set_param, get_param;
var angular_motion/softness: float setget set_param, get_param;
var angular_ortho/damping: float setget set_param, get_param;
var angular_ortho/restitution: float setget set_param, get_param;
var angular_ortho/softness: float setget set_param, get_param;
var linear_limit/damping: float setget set_param, get_param;
var linear_limit/lower_distance: float setget set_param, get_param;
var linear_limit/restitution: float setget set_param, get_param;
var linear_limit/softness: float setget set_param, get_param;
var linear_limit/upper_distance: float setget set_param, get_param;
var linear_motion/damping: float setget set_param, get_param;
var linear_motion/restitution: float setget set_param, get_param;
var linear_motion/softness: float setget set_param, get_param;
var linear_ortho/damping: float setget set_param, get_param;
var linear_ortho/restitution: float setget set_param, get_param;
var linear_ortho/softness: float setget set_param, get_param;

func get_param(param: int) -> float:
    pass;

func set_param(param: int, value: float) -> void:
    pass;

