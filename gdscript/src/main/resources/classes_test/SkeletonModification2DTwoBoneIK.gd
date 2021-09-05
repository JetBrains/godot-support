extends SkeletonModification2D
class_name SkeletonModification2DTwoBoneIK


var flip_bend_direction: bool setget set_flip_bend_direction, get_flip_bend_direction;
var target_maximum_distance: float setget set_target_maximum_distance, get_target_maximum_distance;
var target_minimum_distance: float setget set_target_minimum_distance, get_target_minimum_distance;
var target_nodepath: NodePath setget set_target_node, get_target_node;

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

