extends RigidBody2D
class_name PhysicalBone2D

var auto_configure_joint: bool;
var bone2d_index: int;
var bone2d_nodepath: NodePath;
var follow_bone_when_simulating: bool;
var simulate_physics: bool;

func get_joint() -> Joint2D:
    pass;
func is_simulating_physics() -> bool:
    pass;
