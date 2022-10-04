#brief Profile of a virtual skeleton used as a target for retargeting.
#desc This resource is used in [EditorScenePostImport]. Some parameters are referring to bones in [Skeleton3D], [Skin], [Animation], and some other nodes are rewritten based on the parameters of [SkeletonProfile].
class_name SkeletonProfile

#desc Direction to the average coordinates of bone children.
const TAIL_DIRECTION_AVERAGE_CHILDREN = 0;

#desc Direction to the coordinates of specified bone child.
const TAIL_DIRECTION_SPECIFIC_CHILD = 1;

#desc Direction is not calculated.
const TAIL_DIRECTION_END = 2;


var bone_size: int;

var group_size: int;

#desc A name of bone that will be used as the root bone in [AnimationTree].
#desc [b]Note:[/b] In most cases, it is the bone of the parent of the hips that exists at the world origin in the humanoid model.
var root_bone: StringName;

#desc A name of bone which height will be used as the coefficient for normalization.
#desc [b]Note:[/b] In most cases, it is hips in the humanoid model.
var scale_base_bone: StringName;



#desc Returns the bone index that matches [param bone_name] as its name.
func find_bone(bone_name: StringName) -> int:
	pass;

#desc Returns the name of the bone at [param bone_idx] that will be the key name in the [BoneMap].
#desc In the retargeting process, the returned bone name is the bone name of the target skeleton.
func get_bone_name(bone_idx: int) -> StringName:
	pass;

#desc Returns the name of the bone which is the parent to the bone at [param bone_idx]. The result is empty if the bone has no parent.
func get_bone_parent(bone_idx: int) -> StringName:
	pass;

#desc Returns the name of the bone which is the tail of the bone at [param bone_idx].
func get_bone_tail(bone_idx: int) -> StringName:
	pass;

#desc Returns the group of the bone at [param bone_idx].
func get_group(bone_idx: int) -> StringName:
	pass;

#desc Returns the name of the group at [param group_idx] that will be the drawing group in the [BoneMap] editor.
func get_group_name(group_idx: int) -> StringName:
	pass;

#desc Returns the offset of the bone at [param bone_idx] that will be the button position in the [BoneMap] editor.
#desc This is the offset with origin at the top left corner of the square.
func get_handle_offset(bone_idx: int) -> Vector2:
	pass;

#desc Returns the reference pose transform for bone [param bone_idx].
func get_reference_pose(bone_idx: int) -> Transform3D:
	pass;

#desc Returns the tail direction of the bone at [param bone_idx].
func get_tail_direction(bone_idx: int) -> int:
	pass;

#desc Returns the texture of the group at [param group_idx] that will be the drawing group background image in the [BoneMap] editor.
func get_texture(group_idx: int) -> Texture2D:
	pass;

#desc Sets the name of the bone at [param bone_idx] that will be the key name in the [BoneMap].
#desc In the retargeting process, the setting bone name is the bone name of the target skeleton.
func set_bone_name(bone_idx: int, bone_name: StringName) -> void:
	pass;

#desc Sets the bone with name [param bone_parent] as the parent of the bone at [param bone_idx]. If an empty string is passed, then the bone has no parent.
func set_bone_parent(bone_idx: int, bone_parent: StringName) -> void:
	pass;

#desc Sets the bone with name [param bone_tail] as the tail of the bone at [param bone_idx].
func set_bone_tail(bone_idx: int, bone_tail: StringName) -> void:
	pass;

#desc Sets the group of the bone at [param bone_idx].
func set_group(bone_idx: int, group: StringName) -> void:
	pass;

#desc Sets the name of the group at [param group_idx] that will be the drawing group in the [BoneMap] editor.
func set_group_name(group_idx: int, group_name: StringName) -> void:
	pass;

#desc Sets the offset of the bone at [param bone_idx] that will be the button position in the [BoneMap] editor.
#desc This is the offset with origin at the top left corner of the square.
func set_handle_offset(bone_idx: int, handle_offset: Vector2) -> void:
	pass;

#desc Sets the reference pose transform for bone [param bone_idx].
func set_reference_pose(bone_idx: int, bone_name: Transform3D) -> void:
	pass;

#desc Sets the tail direction of the bone at [param bone_idx].
#desc [b]Note:[/b] This only specifies the method of calculation. The actual coordinates required should be stored in an external skeleton, so the calculation itself needs to be done externally.
func set_tail_direction(bone_idx: int, tail_direction: int) -> void:
	pass;

#desc Sets the texture of the group at [param group_idx] that will be the drawing group background image in the [BoneMap] editor.
func set_texture(group_idx: int, texture: Texture2D) -> void:
	pass;


