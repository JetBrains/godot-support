extends RigidBody2D
class_name PhysicalBone2D


var auto_configure_joint: bool setget set_auto_configure_joint, get_auto_configure_joint;
var bone2d_index: int setget set_bone2d_index, get_bone2d_index;
var bone2d_nodepath: NodePath setget set_bone2d_nodepath, get_bone2d_nodepath;
var follow_bone_when_simulating: bool setget set_follow_bone_when_simulating, get_follow_bone_when_simulating;
var simulate_physics: bool setget set_simulate_physics, get_simulate_physics;

func get_joint() -> Joint2D:
    pass;

func is_simulating_physics() -> bool:
    pass;

