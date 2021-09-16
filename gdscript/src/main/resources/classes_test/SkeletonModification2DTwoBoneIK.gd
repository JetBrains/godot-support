extends SkeletonModification2D
class_name SkeletonModification2DTwoBoneIK

var flip_bend_direction: bool;
var target_maximum_distance: float;
var target_minimum_distance: float;
var target_nodepath: NodePath;

func get_joint_one_bone2d_node() -> NodePath:
    pass;
func get_joint_one_bone_idx() -> int:
    pass;
func get_joint_two_bone2d_node() -> NodePath:
    pass;
func get_joint_two_bone_idx() -> int:
    pass;
func set_joint_one_bone2d_node(bone2d_node: NodePath) -> void:
    pass;
func set_joint_one_bone_idx(bone_idx: int) -> void:
    pass;
func set_joint_two_bone2d_node(bone2d_node: NodePath) -> void:
    pass;
func set_joint_two_bone_idx(bone_idx: int) -> void:
    pass;
