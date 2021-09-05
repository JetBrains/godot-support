extends SkeletonModification2D
class_name SkeletonModification2DFABRIK


var fabrik_data_chain_length: int setget set_fabrik_data_chain_length, get_fabrik_data_chain_length;
var target_nodepath: NodePath setget set_target_node, get_target_node;

func get_fabrik_joint_bone2d_node(joint_idx: int) -> NodePath:
    pass;

func get_fabrik_joint_bone_index(joint_idx: int) -> int:
    pass;

func get_fabrik_joint_magnet_position(joint_idx: int) -> Vector2:
    pass;

func get_fabrik_joint_use_target_rotation(joint_idx: int) -> bool:
    pass;

func set_fabrik_joint_bone2d_node(joint_idx: int, bone2d_nodepath: NodePath) -> void:
    pass;

func set_fabrik_joint_bone_index(joint_idx: int, bone_idx: int) -> void:
    pass;

func set_fabrik_joint_magnet_position(joint_idx: int, magnet_position: Vector2) -> void:
    pass;

func set_fabrik_joint_use_target_rotation(joint_idx: int, use_target_rotation: bool) -> void:
    pass;

