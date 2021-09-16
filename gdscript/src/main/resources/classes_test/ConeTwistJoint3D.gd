extends Joint3D
class_name ConeTwistJoint3D
const PARAM_SWING_SPAN = 0;
const PARAM_TWIST_SPAN = 1;
const PARAM_BIAS = 2;
const PARAM_SOFTNESS = 3;
const PARAM_RELAXATION = 4;
const PARAM_MAX = 5;

var bias: float;
var relaxation: float;
var softness: float;
var swing_span: float;
var twist_span: float;

func get_param(param: int) -> float:
    pass;
func set_param(param: int, value: float) -> void:
    pass;
