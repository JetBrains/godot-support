#brief Skeleton for characters and animated objects.
#desc Skeleton3D provides a hierarchical interface for managing bones, including pose, rest and animation (see [Animation]). It can also use ragdoll physics.
#desc The overall transform of a bone with respect to the skeleton is determined by the following hierarchical order: rest pose, custom pose and pose.
#desc Note that "global pose" below refers to the overall transform of the bone with respect to skeleton, so it not the actual global/world transform of the bone.
class_name Skeleton3D

const NOTIFICATION_UPDATE_SKELETON = 50;


var animate_physical_bones: bool;

#desc Multiplies the position 3D track animation.
#desc [b]Note:[/b] Unless this value is [code]1.0[/code], the key value in animation will not match the actual position value.
var motion_scale: float;

var show_rest_only: bool;



#desc Adds a bone, with name [param name]. [method get_bone_count] will become the bone index.
func add_bone(name: String) -> void:
	pass;

#desc Clear all the bones in this skeleton.
func clear_bones() -> void:
	pass;

#desc Removes the global pose override on all bones in the skeleton.
func clear_bones_global_pose_override() -> void:
	pass;

#desc Deprecated. Local pose overrides will be removed.
#desc Removes the local pose override on all bones in the skeleton.
func clear_bones_local_pose_override() -> void:
	pass;

func create_skin_from_rest_transforms() -> Skin:
	pass;

#desc Executes all the modifications on the [SkeletonModificationStack3D], if the Skeleton3D has one assigned.
func execute_modifications(delta: float, execution_mode: int) -> void:
	pass;

#desc Returns the bone index that matches [param name] as its name.
func find_bone(name: String) -> int:
	pass;

#desc Force updates the bone transforms/poses for all bones in the skeleton.
func force_update_all_bone_transforms() -> void:
	pass;

#desc Force updates the bone transform for the bone at [param bone_idx] and all of its children.
func force_update_bone_child_transform(bone_idx: int) -> void:
	pass;

#desc Returns an array containing the bone indexes of all the children node of the passed in bone, [param bone_idx].
func get_bone_children(bone_idx: int) -> PackedInt32Array:
	pass;

#desc Returns the number of bones in the skeleton.
func get_bone_count() -> int:
	pass;

#desc Returns the overall transform of the specified bone, with respect to the skeleton. Being relative to the skeleton frame, this is not the actual "global" transform of the bone.
func get_bone_global_pose(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the overall transform of the specified bone, with respect to the skeleton, but without any global pose overrides. Being relative to the skeleton frame, this is not the actual "global" transform of the bone.
func get_bone_global_pose_no_override(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the global pose override transform for [param bone_idx].
func get_bone_global_pose_override(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the global rest transform for [param bone_idx].
func get_bone_global_rest(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the local pose override transform for [param bone_idx].
func get_bone_local_pose_override(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the name of the bone at index [param bone_idx].
func get_bone_name(bone_idx: int) -> String:
	pass;

#desc Returns the bone index which is the parent of the bone at [param bone_idx]. If -1, then bone has no parent.
#desc [b]Note:[/b] The parent bone returned will always be less than [param bone_idx].
func get_bone_parent(bone_idx: int) -> int:
	pass;

#desc Returns the pose transform of the specified bone. Pose is applied on top of the custom pose, which is applied on top the rest pose.
func get_bone_pose(bone_idx: int) -> Transform3D:
	pass;

func get_bone_pose_position(bone_idx: int) -> Vector3:
	pass;

func get_bone_pose_rotation(bone_idx: int) -> Quaternion:
	pass;

func get_bone_pose_scale(bone_idx: int) -> Vector3:
	pass;

#desc Returns the rest transform for a bone [param bone_idx].
func get_bone_rest(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the modification stack attached to this skeleton, if one exists.
func get_modification_stack() -> SkeletonModificationStack3D:
	pass;

#desc Returns an array with all of the bones that are parentless. Another way to look at this is that it returns the indexes of all the bones that are not dependent or modified by other bones in the Skeleton.
func get_parentless_bones() -> PackedInt32Array:
	pass;

#desc Takes the passed-in global pose and converts it to local pose transform.
#desc This can be used to easily convert a global pose from [method get_bone_global_pose] to a global transform in [method set_bone_local_pose_override].
func global_pose_to_local_pose(bone_idx: int, global_pose: Transform3D) -> Transform3D:
	pass;

#desc Deprecated. Use [Node3D] apis instead.
#desc Takes the passed-in global pose and converts it to a world transform.
#desc This can be used to easily convert a global pose from [method get_bone_global_pose] to a global transform usable with a node's transform, like [member Node3D.global_transform] for example.
func global_pose_to_world_transform(global_pose: Transform3D) -> Transform3D:
	pass;

#desc Rotates the given [Basis] so that the forward axis of the Basis is facing in the forward direction of the bone at [param bone_idx].
#desc This is helper function to make using [method Transform3D.looking_at] easier with bone poses.
func global_pose_z_forward_to_bone_forward(bone_idx: int, basis: Basis) -> Basis:
	pass;

#desc Returns whether the bone pose for the bone at [param bone_idx] is enabled.
func is_bone_enabled(bone_idx: int) -> bool:
	pass;

#desc Converts the passed-in local pose to a global pose relative to the inputted bone, [param bone_idx].
#desc This could be used to convert [method get_bone_pose] for use with the [method set_bone_global_pose_override] function.
func local_pose_to_global_pose(bone_idx: int, local_pose: Transform3D) -> Transform3D:
	pass;

#desc Returns all bones in the skeleton to their rest poses.
func localize_rests() -> void:
	pass;

#desc Adds a collision exception to the physical bone.
#desc Works just like the [RigidBody3D] node.
func physical_bones_add_collision_exception(exception: RID) -> void:
	pass;

#desc Removes a collision exception to the physical bone.
#desc Works just like the [RigidBody3D] node.
func physical_bones_remove_collision_exception(exception: RID) -> void:
	pass;

#desc Tells the [PhysicalBone3D] nodes in the Skeleton to start simulating and reacting to the physics world.
#desc Optionally, a list of bone names can be passed-in, allowing only the passed-in bones to be simulated.
func physical_bones_start_simulation(bones: StringName[]) -> void:
	pass;

#desc Tells the [PhysicalBone3D] nodes in the Skeleton to stop simulating.
func physical_bones_stop_simulation() -> void:
	pass;

#desc Binds the given Skin to the Skeleton.
func register_skin(skin: Skin) -> SkinReference:
	pass;

#desc Sets the bone pose to rest for [param bone_idx].
func reset_bone_pose(bone_idx: int) -> void:
	pass;

#desc Sets all bone poses to rests.
func reset_bone_poses() -> void:
	pass;

#desc Disables the pose for the bone at [param bone_idx] if [code]false[/code], enables the bone pose if [code]true[/code].
func set_bone_enabled(bone_idx: int, enabled: bool) -> void:
	pass;

#desc Sets the global pose transform, [param pose], for the bone at [param bone_idx].
#desc [param amount] is the interpolation strength that will be used when applying the pose, and [param persistent] determines if the applied pose will remain.
#desc [b]Note:[/b] The pose transform needs to be a global pose! To convert a world transform from a [Node3D] to a global bone pose, multiply the [method Transform3D.affine_inverse] of the node's [member Node3D.global_transform] by the desired world transform
func set_bone_global_pose_override(bone_idx: int, pose: Transform3D, amount: float, persistent: bool) -> void:
	pass;

#desc Deprecated. Local pose overrides will be removed.
#desc Sets the local pose transform, [param pose], for the bone at [param bone_idx].
#desc [param amount] is the interpolation strength that will be used when applying the pose, and [param persistent] determines if the applied pose will remain.
#desc [b]Note:[/b] The pose transform needs to be a local pose! Use [method global_pose_to_local_pose] to convert a global pose to a local pose.
func set_bone_local_pose_override(bone_idx: int, pose: Transform3D, amount: float, persistent: bool) -> void:
	pass;

func set_bone_name(bone_idx: int, name: String) -> void:
	pass;

#desc Sets the bone index [param parent_idx] as the parent of the bone at [param bone_idx]. If -1, then bone has no parent.
#desc [b]Note:[/b] [param parent_idx] must be less than [param bone_idx].
func set_bone_parent(bone_idx: int, parent_idx: int) -> void:
	pass;

func set_bone_pose_position(bone_idx: int, position: Vector3) -> void:
	pass;

func set_bone_pose_rotation(bone_idx: int, rotation: Quaternion) -> void:
	pass;

func set_bone_pose_scale(bone_idx: int, scale: Vector3) -> void:
	pass;

#desc Sets the rest transform for bone [param bone_idx].
func set_bone_rest(bone_idx: int, rest: Transform3D) -> void:
	pass;

#desc Sets the modification stack for this skeleton to the passed-in modification stack, [param modification_stack].
func set_modification_stack(modification_stack: SkeletonModificationStack3D) -> void:
	pass;

#desc Unparents the bone at [param bone_idx] and sets its rest position to that of its parent prior to being reset.
func unparent_bone_and_rest(bone_idx: int) -> void:
	pass;

#desc Deprecated. Use [Node3D] apis instead.
#desc Takes the passed-in global transform and converts it to a global pose.
#desc This can be used to easily convert a global transform from [member Node3D.global_transform] to a global pose usable with [method set_bone_global_pose_override], for example.
func world_transform_to_global_pose(world_transform: Transform3D) -> Transform3D:
	pass;


