#brief A modification that rotates a bone to look at a target.
#desc This [SkeletonModification3D] rotates a bone to look a target. This is extremely helpful for moving character's heads to look at the player, rotating a turret to look at a target, or any other case where you want to make a bone rotate towards something quickly and easily.
class_name SkeletonModification3DLookAt


#desc The bone index of the bone that should be operated on by this modification.
#desc When possible, this will also update the [member bone_name] based on data provided by the [Skeleton3D].
var bone_index: int;

#desc The name of the bone that should be operated on by this modification.
#desc When possible, this will also update the [member bone_index] based on data provided by the [Skeleton3D].
var bone_name: String;

#desc The NodePath to the node that is the target for the modification.
var target_nodepath: NodePath;



#desc Returns the amount of extra rotation that is applied to the bone after the LookAt modification executes.
func get_additional_rotation() -> Vector3:
	pass;

#desc Returns the plane that the LookAt modification is limiting rotation to.
func get_lock_rotation_plane() -> int:
	pass;

#desc Returns whether the LookAt modification is limiting rotation to a single plane in 3D space.
func get_lock_rotation_to_plane() -> bool:
	pass;

#desc Sets the amount of extra rotation to be applied after the LookAt modification executes. This allows you to adjust the finished result.
func set_additional_rotation() -> void:
	pass;

func set_lock_rotation_plane() -> void:
	pass;

#desc When [code]true[/code], the LookAt modification will limit its rotation to a single plane in 3D space. The plane used can be configured using the [code]set_lock_rotation_plane[/code] function.
func set_lock_rotation_to_plane() -> void:
	pass;


