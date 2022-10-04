#brief A twist joint between two 3D PhysicsBodies.
#desc The joint can rotate the bodies across an axis defined by the local x-axes of the [Joint3D].
#desc The twist axis is initiated as the X axis of the [Joint3D].
#desc Once the Bodies swing, the twist axis is calculated as the middle of the x-axes of the Joint3D in the local space of the two Bodies. See also [Generic6DOFJoint3D].
class_name ConeTwistJoint3D

#desc Swing is rotation from side to side, around the axis perpendicular to the twist axis.
#desc The swing span defines, how much rotation will not get corrected along the swing axis.
#desc Could be defined as looseness in the [ConeTwistJoint3D].
#desc If below 0.05, this behavior is locked.
const PARAM_SWING_SPAN = 0;

#desc Twist is the rotation around the twist axis, this value defined how far the joint can twist.
#desc Twist is locked if below 0.05.
const PARAM_TWIST_SPAN = 1;

#desc The speed with which the swing or twist will take place.
#desc The higher, the faster.
const PARAM_BIAS = 2;

#desc The ease with which the joint starts to twist. If it's too low, it takes more force to start twisting the joint.
const PARAM_SOFTNESS = 3;

#desc Defines, how fast the swing- and twist-speed-difference on both sides gets synced.
const PARAM_RELAXATION = 4;

#desc Represents the size of the [enum Param] enum.
const PARAM_MAX = 5;


#desc The speed with which the swing or twist will take place.
#desc The higher, the faster.
var bias: float;

#desc Defines, how fast the swing- and twist-speed-difference on both sides gets synced.
var relaxation: float;

#desc The ease with which the joint starts to twist. If it's too low, it takes more force to start twisting the joint.
var softness: float;

#desc Swing is rotation from side to side, around the axis perpendicular to the twist axis.
#desc The swing span defines, how much rotation will not get corrected along the swing axis.
#desc Could be defined as looseness in the [ConeTwistJoint3D].
#desc If below 0.05, this behavior is locked.
var swing_span: float;

#desc Twist is the rotation around the twist axis, this value defined how far the joint can twist.
#desc Twist is locked if below 0.05.
var twist_span: float;



func get_param() -> float:
	pass;

func set_param(param: int, value: float) -> void:
	pass;


