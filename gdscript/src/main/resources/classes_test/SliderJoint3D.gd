#brief Slider between two PhysicsBodies in 3D.
#desc Slides across the X axis of the pivot object. See also [Generic6DOFJoint3D].
class_name SliderJoint3D

#desc The maximum difference between the pivot points on their X axis before damping happens.
const PARAM_LINEAR_LIMIT_UPPER = 0;

#desc The minimum difference between the pivot points on their X axis before damping happens.
const PARAM_LINEAR_LIMIT_LOWER = 1;

#desc A factor applied to the movement across the slider axis once the limits get surpassed. The lower, the slower the movement.
const PARAM_LINEAR_LIMIT_SOFTNESS = 2;

#desc The amount of restitution once the limits are surpassed. The lower, the more velocityenergy gets lost.
const PARAM_LINEAR_LIMIT_RESTITUTION = 3;

#desc The amount of damping once the slider limits are surpassed.
const PARAM_LINEAR_LIMIT_DAMPING = 4;

#desc A factor applied to the movement across the slider axis as long as the slider is in the limits. The lower, the slower the movement.
const PARAM_LINEAR_MOTION_SOFTNESS = 5;

#desc The amount of restitution inside the slider limits.
const PARAM_LINEAR_MOTION_RESTITUTION = 6;

#desc The amount of damping inside the slider limits.
const PARAM_LINEAR_MOTION_DAMPING = 7;

#desc A factor applied to the movement across axes orthogonal to the slider.
const PARAM_LINEAR_ORTHOGONAL_SOFTNESS = 8;

#desc The amount of restitution when movement is across axes orthogonal to the slider.
const PARAM_LINEAR_ORTHOGONAL_RESTITUTION = 9;

#desc The amount of damping when movement is across axes orthogonal to the slider.
const PARAM_LINEAR_ORTHOGONAL_DAMPING = 10;

#desc The upper limit of rotation in the slider.
const PARAM_ANGULAR_LIMIT_UPPER = 11;

#desc The lower limit of rotation in the slider.
const PARAM_ANGULAR_LIMIT_LOWER = 12;

#desc A factor applied to the all rotation once the limit is surpassed.
const PARAM_ANGULAR_LIMIT_SOFTNESS = 13;

#desc The amount of restitution of the rotation when the limit is surpassed.
const PARAM_ANGULAR_LIMIT_RESTITUTION = 14;

#desc The amount of damping of the rotation when the limit is surpassed.
const PARAM_ANGULAR_LIMIT_DAMPING = 15;

#desc A factor applied to the all rotation in the limits.
const PARAM_ANGULAR_MOTION_SOFTNESS = 16;

#desc The amount of restitution of the rotation in the limits.
const PARAM_ANGULAR_MOTION_RESTITUTION = 17;

#desc The amount of damping of the rotation in the limits.
const PARAM_ANGULAR_MOTION_DAMPING = 18;

#desc A factor applied to the all rotation across axes orthogonal to the slider.
const PARAM_ANGULAR_ORTHOGONAL_SOFTNESS = 19;

#desc The amount of restitution of the rotation across axes orthogonal to the slider.
const PARAM_ANGULAR_ORTHOGONAL_RESTITUTION = 20;

#desc The amount of damping of the rotation across axes orthogonal to the slider.
const PARAM_ANGULAR_ORTHOGONAL_DAMPING = 21;

#desc Represents the size of the [enum Param] enum.
const PARAM_MAX = 22;


#desc The amount of damping of the rotation when the limit is surpassed.
#desc A lower damping value allows a rotation initiated by body A to travel to body B slower.
var angular_limit/damping: float;

#desc The lower limit of rotation in the slider.
var angular_limit/lower_angle: float;

#desc The amount of restitution of the rotation when the limit is surpassed.
#desc Does not affect damping.
var angular_limit/restitution: float;

#desc A factor applied to the all rotation once the limit is surpassed.
#desc Makes all rotation slower when between 0 and 1.
var angular_limit/softness: float;

#desc The upper limit of rotation in the slider.
var angular_limit/upper_angle: float;

#desc The amount of damping of the rotation in the limits.
var angular_motion/damping: float;

#desc The amount of restitution of the rotation in the limits.
var angular_motion/restitution: float;

#desc A factor applied to the all rotation in the limits.
var angular_motion/softness: float;

#desc The amount of damping of the rotation across axes orthogonal to the slider.
var angular_ortho/damping: float;

#desc The amount of restitution of the rotation across axes orthogonal to the slider.
var angular_ortho/restitution: float;

#desc A factor applied to the all rotation across axes orthogonal to the slider.
var angular_ortho/softness: float;

#desc The amount of damping that happens once the limit defined by [member linear_limit/lower_distance] and [member linear_limit/upper_distance] is surpassed.
var linear_limit/damping: float;

#desc The minimum difference between the pivot points on their X axis before damping happens.
var linear_limit/lower_distance: float;

#desc The amount of restitution once the limits are surpassed. The lower, the more velocity-energy gets lost.
var linear_limit/restitution: float;

#desc A factor applied to the movement across the slider axis once the limits get surpassed. The lower, the slower the movement.
var linear_limit/softness: float;

#desc The maximum difference between the pivot points on their X axis before damping happens.
var linear_limit/upper_distance: float;

#desc The amount of damping inside the slider limits.
var linear_motion/damping: float;

#desc The amount of restitution inside the slider limits.
var linear_motion/restitution: float;

#desc A factor applied to the movement across the slider axis as long as the slider is in the limits. The lower, the slower the movement.
var linear_motion/softness: float;

#desc The amount of damping when movement is across axes orthogonal to the slider.
var linear_ortho/damping: float;

#desc The amount of restitution when movement is across axes orthogonal to the slider.
var linear_ortho/restitution: float;

#desc A factor applied to the movement across axes orthogonal to the slider.
var linear_ortho/softness: float;



func get_param() -> float:
	pass;

func set_param(param: int, value: float) -> void:
	pass;


