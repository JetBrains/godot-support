#brief Pin joint for 3D PhysicsBodies.
#desc Pin joint for 3D rigid bodies. It pins 2 bodies (dynamic or static) together. See also [Generic6DOFJoint3D].
class_name PinJoint3D

#desc The force with which the pinned objects stay in positional relation to each other. The higher, the stronger.
const PARAM_BIAS = 0;

#desc The force with which the pinned objects stay in velocity relation to each other. The higher, the stronger.
const PARAM_DAMPING = 1;

#desc If above 0, this value is the maximum value for an impulse that this Joint3D produces.
const PARAM_IMPULSE_CLAMP = 2;


#desc The force with which the pinned objects stay in positional relation to each other. The higher, the stronger.
var params/bias: float;

#desc The force with which the pinned objects stay in velocity relation to each other. The higher, the stronger.
var params/damping: float;

#desc If above 0, this value is the maximum value for an impulse that this Joint3D produces.
var params/impulse_clamp: float;



#desc Returns the value of the specified parameter.
func get_param(param: int) -> float:
	pass;

#desc Sets the value of the specified parameter.
func set_param(param: int, value: float) -> void:
	pass;


