extends Joint3D
class_name ConeTwistJoint3D

const PARAM_SWING_SPAN = 0;
const PARAM_TWIST_SPAN = 1;
const PARAM_BIAS = 2;
const PARAM_SOFTNESS = 3;
const PARAM_RELAXATION = 4;
const PARAM_MAX = 5;

var bias: float setget set_param, get_param;
var relaxation: float setget set_param, get_param;
var softness: float setget set_param, get_param;
var swing_span: float setget _set_swing_span, _get_swing_span;
var twist_span: float setget _set_twist_span, _get_twist_span;

func get_param(param: int) -> float:
    pass;

func set_param(param: int, value: float) -> void:
    pass;

