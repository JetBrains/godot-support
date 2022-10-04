#brief Skeleton for 2D characters and animated objects.
#desc Skeleton2D parents a hierarchy of [Bone2D] objects. It is a requirement of [Bone2D]. Skeleton2D holds a reference to the rest pose of its children and acts as a single point of access to its bones.
#desc To setup different types of inverse kinematics for the given Skeleton2D, a [SkeletonModificationStack2D] should be created. They can be applied by creating the desired number of modifications, which can be done by increasing [member SkeletonModificationStack2D.modification_count].
class_name Skeleton2D




#desc Executes all the modifications on the [SkeletonModificationStack2D], if the Skeleton3D has one assigned.
func execute_modifications(delta: float, execution_mode: int) -> void:
	pass;

#desc Returns a [Bone2D] from the node hierarchy parented by Skeleton2D. The object to return is identified by the parameter [param idx]. Bones are indexed by descending the node hierarchy from top to bottom, adding the children of each branch before moving to the next sibling.
func get_bone(idx: int) -> Bone2D:
	pass;

#desc Returns the number of [Bone2D] nodes in the node hierarchy parented by Skeleton2D.
func get_bone_count() -> int:
	pass;

#desc Returns the local pose override transform for [param bone_idx].
func get_bone_local_pose_override(bone_idx: int) -> Transform2D:
	pass;

#desc Returns the [SkeletonModificationStack2D] attached to this skeleton, if one exists.
func get_modification_stack() -> SkeletonModificationStack2D:
	pass;

#desc Returns the [RID] of a Skeleton2D instance.
func get_skeleton() -> RID:
	pass;

#desc Sets the local pose transform, [param override_pose], for the bone at [param bone_idx].
#desc [param strength] is the interpolation strength that will be used when applying the pose, and [param persistent] determines if the applied pose will remain.
#desc [b]Note:[/b] The pose transform needs to be a local transform relative to the [Bone2D] node at [param bone_idx]!
func set_bone_local_pose_override(bone_idx: int, override_pose: Transform2D, strength: float, persistent: bool) -> void:
	pass;

#desc Sets the [SkeletonModificationStack2D] attached to this skeleton.
func set_modification_stack(modification_stack: SkeletonModificationStack2D) -> void:
	pass;


