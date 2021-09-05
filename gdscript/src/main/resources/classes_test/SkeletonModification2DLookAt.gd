extends SkeletonModification2D
class_name SkeletonModification2DLookAt


var bone2d_node: NodePath setget set_bone2d_node, get_bone2d_node;
var bone_index: int setget set_bone_index, get_bone_index;
var target_nodepath: NodePath setget set_target_node, get_target_node;

func get_additional_rotation() -> float:
    pass;

func get_constraint_angle_invert() -> bool:
    pass;

func get_constraint_angle_max() -> float:
    pass;

func get_constraint_angle_min() -> float:
    pass;

func get_enable_constraint() -> bool:
    pass;

func set_additional_rotation(rotation: float) -> void:
    pass;

func set_constraint_angle_invert(invert: bool) -> void:
    pass;

func set_constraint_angle_max(angle_max: float) -> void:
    pass;

func set_constraint_angle_min(angle_min: float) -> void:
    pass;

func set_enable_constraint(enable_constraint: bool) -> void:
    pass;

