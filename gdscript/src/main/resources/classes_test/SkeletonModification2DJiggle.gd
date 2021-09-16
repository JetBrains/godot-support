extends SkeletonModification2D
class_name SkeletonModification2DJiggle

var damping: float;
var gravity: Vector2;
var jiggle_data_chain_length: int;
var mass: float;
var stiffness: float;
var target_nodepath: NodePath;
var use_gravity: bool;

func get_collision_mask() -> int:
    pass;
func get_jiggle_joint_bone2d_node(joint_idx: int) -> NodePath:
    pass;
func get_jiggle_joint_bone_index(joint_idx: int) -> int:
    pass;
func get_jiggle_joint_damping(joint_idx: int) -> float:
    pass;
func get_jiggle_joint_gravity(joint_idx: int) -> Vector2:
    pass;
func get_jiggle_joint_mass(joint_idx: int) -> float:
    pass;
func get_jiggle_joint_override(joint_idx: int) -> bool:
    pass;
func get_jiggle_joint_stiffness(joint_idx: int) -> float:
    pass;
func get_jiggle_joint_use_gravity(joint_idx: int) -> bool:
    pass;
func get_use_colliders() -> bool:
    pass;
func set_collision_mask(collision_mask: int) -> void:
    pass;
func set_jiggle_joint_bone2d_node(joint_idx: int, bone2d_node: NodePath) -> void:
    pass;
func set_jiggle_joint_bone_index(joint_idx: int, bone_idx: int) -> void:
    pass;
func set_jiggle_joint_damping(joint_idx: int, damping: float) -> void:
    pass;
func set_jiggle_joint_gravity(joint_idx: int, gravity: Vector2) -> void:
    pass;
func set_jiggle_joint_mass(joint_idx: int, mass: float) -> void:
    pass;
func set_jiggle_joint_override(joint_idx: int, override: bool) -> void:
    pass;
func set_jiggle_joint_stiffness(joint_idx: int, stiffness: float) -> void:
    pass;
func set_jiggle_joint_use_gravity(joint_idx: int, use_gravity: bool) -> void:
    pass;
func set_use_colliders(use_colliders: bool) -> void:
    pass;
