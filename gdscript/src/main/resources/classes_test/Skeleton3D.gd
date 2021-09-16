extends Node3D
class_name Skeleton3D
const NOTIFICATION_UPDATE_SKELETON = 50;

var animate_physical_bones: bool;

func add_bone(name: String) -> void:
    pass;
func bone_transform_to_world_transform(bone_transform: Transform3D) -> Transform3D:
    pass;
func clear_bones() -> void:
    pass;
func clear_bones_global_pose_override() -> void:
    pass;
func find_bone(name: String) -> int:
    pass;
func get_bone_count() -> int:
    pass;
func get_bone_custom_pose(bone_idx: int) -> Transform3D:
    pass;
func get_bone_global_pose(bone_idx: int) -> Transform3D:
    pass;
func get_bone_global_pose_no_override(bone_idx: int) -> Transform3D:
    pass;
func get_bone_name(bone_idx: int) -> String:
    pass;
func get_bone_parent(bone_idx: int) -> int:
    pass;
func get_bone_pose(bone_idx: int) -> Transform3D:
    pass;
func get_bone_process_orders() -> PackedInt32Array:
    pass;
func get_bone_rest(bone_idx: int) -> Transform3D:
    pass;
func is_bone_rest_disabled(bone_idx: int) -> bool:
    pass;
func localize_rests() -> void:
    pass;
func physical_bones_add_collision_exception(exception: RID) -> void:
    pass;
func physical_bones_remove_collision_exception(exception: RID) -> void:
    pass;
func physical_bones_start_simulation(bones: Array[StringName]) -> void:
    pass;
func physical_bones_stop_simulation() -> void:
    pass;
func register_skin(skin: Skin) -> SkinReference:
    pass;
func set_bone_custom_pose(bone_idx: int, custom_pose: Transform3D) -> void:
    pass;
func set_bone_disable_rest(bone_idx: int, disable: bool) -> void:
    pass;
func set_bone_global_pose_override(bone_idx: int, pose: Transform3D, amount: float, persistent: bool) -> void:
    pass;
func set_bone_name(bone_idx: int, name: String) -> void:
    pass;
func set_bone_parent(bone_idx: int, parent_idx: int) -> void:
    pass;
func set_bone_pose(bone_idx: int, pose: Transform3D) -> void:
    pass;
func set_bone_rest(bone_idx: int, rest: Transform3D) -> void:
    pass;
func unparent_bone_and_rest(bone_idx: int) -> void:
    pass;
func world_transform_to_bone_transform(world_transform: Transform3D) -> Transform3D:
    pass;
