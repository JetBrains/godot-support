extends Joint3D
class_name PinJoint3D
const PARAM_BIAS = 0;
const PARAM_DAMPING = 1;
const PARAM_IMPULSE_CLAMP = 2;

var params/bias: float;
var params/damping: float;
var params/impulse_clamp: float;

func get_param(param: int) -> float:
    pass;
func set_param(param: int, value: float) -> void:
    pass;
