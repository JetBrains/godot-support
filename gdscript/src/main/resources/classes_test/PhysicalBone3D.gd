extends PhysicsBody3D
class_name PhysicalBone3D
const JOINT_TYPE_NONE = 0;
const JOINT_TYPE_PIN = 1;
const JOINT_TYPE_CONE = 2;
const JOINT_TYPE_HINGE = 3;
const JOINT_TYPE_SLIDER = 4;
const JOINT_TYPE_6DOF = 5;

var angular_damp: float;
var body_offset: Transform3D;
var bounce: float;
var can_sleep: bool;
var friction: float;
var gravity_scale: float;
var joint_offset: Transform3D;
var joint_rotation: Vector3;
var joint_type: int;
var linear_damp: float;
var mass: float;

func apply_central_impulse(impulse: Vector3) -> void:
    pass;
func apply_impulse(impulse: Vector3, position: Vector3) -> void:
    pass;
func get_bone_id() -> int:
    pass;
func get_simulate_physics() -> bool:
    pass;
func is_simulating_physics() -> bool:
    pass;
