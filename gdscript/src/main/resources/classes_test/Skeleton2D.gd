extends Node2D
class_name Skeleton2D


func execute_modifications(delta: float, execution_mode: int) -> void:
    pass;
func get_bone(idx: int) -> Bone2D:
    pass;
func get_bone_count() -> int:
    pass;
func get_bone_local_pose_override(bone_idx: int) -> Transform2D:
    pass;
func get_modification_stack() -> SkeletonModificationStack2D:
    pass;
func get_skeleton() -> RID:
    pass;
func set_bone_local_pose_override(bone_idx: int, override_pose: Transform2D, strength: float, persistent: bool) -> void:
    pass;
func set_modification_stack(modification_stack: SkeletonModificationStack2D) -> void:
    pass;
