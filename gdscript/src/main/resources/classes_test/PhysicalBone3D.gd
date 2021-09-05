extends PhysicsBody3D
class_name PhysicalBone3D

const JOINT_TYPE_NONE = 0;
const JOINT_TYPE_PIN = 1;
const JOINT_TYPE_CONE = 2;
const JOINT_TYPE_HINGE = 3;
const JOINT_TYPE_SLIDER = 4;
const JOINT_TYPE_6DOF = 5;

var angular_damp: float setget set_angular_damp, get_angular_damp;
var body_offset: Transform3D setget set_body_offset, get_body_offset;
var bounce: float setget set_bounce, get_bounce;
var can_sleep: bool setget set_can_sleep, is_able_to_sleep;
var friction: float setget set_friction, get_friction;
var gravity_scale: float setget set_gravity_scale, get_gravity_scale;
var joint_offset: Transform3D setget set_joint_offset, get_joint_offset;
var joint_rotation: Vector3 setget set_joint_rotation, get_joint_rotation;
var joint_type: int setget set_joint_type, get_joint_type;
var linear_damp: float setget set_linear_damp, get_linear_damp;
var mass: float setget set_mass, get_mass;

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

