extends SkeletonModification2D
class_name SkeletonModification2DCCDIK


var ccdik_data_chain_length: int setget set_ccdik_data_chain_length, get_ccdik_data_chain_length;
var target_nodepath: NodePath setget set_target_node, get_target_node;
var tip_nodepath: NodePath setget set_tip_node, get_tip_node;

func get_ccdik_joint_bone2d_node(joint_idx: int) -> NodePath:
    pass;

func get_ccdik_joint_bone_index(joint_idx: int) -> int:
    pass;

func get_ccdik_joint_constraint_angle_invert(joint_idx: int) -> bool:
    pass;

func get_ccdik_joint_constraint_angle_max(joint_idx: int) -> float:
    pass;

func get_ccdik_joint_constraint_angle_min(joint_idx: int) -> float:
    pass;

func get_ccdik_joint_enable_constraint(joint_idx: int) -> bool:
    pass;

func get_ccdik_joint_rotate_from_joint(joint_idx: int) -> bool:
    pass;

func set_ccdik_joint_bone2d_node(joint_idx: int, bone2d_nodepath: NodePath) -> void:
    pass;

func set_ccdik_joint_bone_index(joint_idx: int, bone_idx: int) -> void:
    pass;

func set_ccdik_joint_constraint_angle_invert(joint_idx: int, invert: bool) -> void:
    pass;

func set_ccdik_joint_constraint_angle_max(joint_idx: int, angle_max: float) -> void:
    pass;

func set_ccdik_joint_constraint_angle_min(joint_idx: int, angle_min: float) -> void:
    pass;

func set_ccdik_joint_enable_constraint(joint_idx: int, enable_constraint: bool) -> void:
    pass;

func set_ccdik_joint_rotate_from_joint(joint_idx: int, rotate_from_joint: bool) -> void:
    pass;

