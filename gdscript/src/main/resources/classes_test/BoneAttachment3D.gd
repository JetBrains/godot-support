#brief A node that will attach to a bone.
#desc This node will allow you to select a bone for this node to attach to. The BoneAttachment3D node can copy the transform of the select bone, or can override the transform of the selected bone.
#desc The BoneAttachment3D node must either be a child of a [Skeleton3D] node or be given an external [Skeleton3D] to use in order to function properly.
class_name BoneAttachment3D


#desc The index of the attached bone.
var bone_idx: int;

#desc The name of the attached bone.
var bone_name: String;



#desc Returns the [NodePath] to the external [Skeleton3D] node, if one has been set.
func get_external_skeleton() -> NodePath:
	pass;

#desc Deprecated. Local pose overrides will be removed.
#desc Returns the override mode for the BoneAttachment3D node (0=global / 1=local).
func get_override_mode() -> int:
	pass;

#desc Returns whether the BoneAttachment3D node is overriding the bone pose of the bone it's attached to.
func get_override_pose() -> bool:
	pass;

#desc Returns whether the BoneAttachment3D node is using an external [Skeleton3D] rather than attempting to use its parent node as the [Skeleton3D].
func get_use_external_skeleton() -> bool:
	pass;

#desc A function that is called automatically when the [Skeleton3D] the BoneAttachment3D node is using has a bone that has changed its pose. This function is where the BoneAttachment3D node updates its position so it is correctly bound when it is [i]not[/i] set to override the bone pose.
func on_bone_pose_update(bone_index: int) -> void:
	pass;

#desc Sets the [NodePath] to the external skeleton that the BoneAttachment3D node should use. The external [Skeleton3D] node is only used when [code]use_external_skeleton[/code] is set to [code]true[/code].
func set_external_skeleton(external_skeleton: NodePath) -> void:
	pass;

#desc Deprecated. Local pose overrides will be removed.
#desc Sets the override mode for the BoneAttachment3D node (0=global / 1=local). The override mode defines which of the bone poses the BoneAttachment3D node will override.
func set_override_mode(override_mode: int) -> void:
	pass;

#desc Sets whether the BoneAttachment3D node will override the bone pose of the bone it is attached to. When set to [code]true[/code], the BoneAttachment3D node can change the pose of the bone.
func set_override_pose(override_pose: bool) -> void:
	pass;

#desc Sets whether the BoneAttachment3D node will use an extenral [Skeleton3D] node rather than attenpting to use its parent node as the [Skeleton3D]. When set to [code]true[/code], the BoneAttachment3D node will use the external [Skeleton3D] node set in [code]set_external_skeleton[/code].
func set_use_external_skeleton(use_external_skeleton: bool) -> void:
	pass;


