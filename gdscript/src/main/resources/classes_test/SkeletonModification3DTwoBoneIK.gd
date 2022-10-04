#brief A modification that moves two bones to reach the target.
#desc This [SkeletonModification3D] uses an algorithm typically called TwoBoneIK. This algorithm works by leveraging the law of cosigns and the lengths of the bones to figure out what rotation the bones currently have, and what rotation they need to make a complete triangle, where the first bone, the second bone, and the target form the three vertices of the triangle. Because the algorithm works by making a triangle, it can only operate on two bones.
#desc TwoBoneIK is great for arms, legs, and really any joints that can be represented by just two bones that bend to reach a target. This solver is more lightweight than [SkeletonModification3DFABRIK], but gives similar, natural looking results.
#desc A [Node3D]-based node can be used to define the pole, or bend direction, allowing control over which direction the joint takes when bending to reach the target when the target is within reach.
class_name SkeletonModification3DTwoBoneIK


#desc The NodePath to the node that is the target for the TwoBoneIK modification. This node is what the modification will attempt to rotate the bones to reach.
var target_nodepath: NodePath;



#desc Returns whether the TwoBoneIK modification will attempt to autocalculate the lengths of the two bones.
func get_auto_calculate_joint_length() -> bool:
	pass;

#desc Returns the bone index of the first bone in the TwoBoneIK modification.
func get_joint_one_bone_idx() -> int:
	pass;

#desc Returns the name of the first bone in the TwoBoneIK modification.
func get_joint_one_bone_name() -> String:
	pass;

#desc Returns the length of the first bone in the TwoBoneIK modification.
func get_joint_one_length() -> float:
	pass;

#desc Returns the amount of roll/twist applied to the first bone in the TwoBoneIK modification.
func get_joint_one_roll() -> float:
	pass;

#desc Returns the bone index of the second bone in the TwoBoneIK modification.
func get_joint_two_bone_idx() -> int:
	pass;

#desc Returns the name of the second bone in the TwoBoneIK modification.
func get_joint_two_bone_name() -> String:
	pass;

#desc Returns the length of the second bone in the TwoBoneIK modification.
func get_joint_two_length() -> float:
	pass;

#desc Returns the amount of roll/twist applied to the second bone in the TwoBoneIK modification.
func get_joint_two_roll() -> float:
	pass;

#desc Returns the node that is being used as the pole node for the TwoBoneIK modification, if a pole node has been set.
func get_pole_node() -> NodePath:
	pass;

#desc Returns the node that is being used to calculate the tip position of the second bone in the TwoBoneIK modification, if a tip node has been set.
func get_tip_node() -> NodePath:
	pass;

#desc Returns whether the TwoBoneIK modification will attempt to use the pole node to figure out which direction to bend, if a pole node has been set.
func get_use_pole_node() -> bool:
	pass;

#desc Returns whether the TwoBoneIK modification will attempt to use the tip node to figure out the length and position of the tip of the second bone.
func get_use_tip_node() -> bool:
	pass;

#desc If true, the TwoBoneIK modification will attempt to autocalculate the lengths of the bones being used. The first bone will be calculated by using the distance from the origin of the first bone to the origin of the second bone.
#desc The second bone will be calculated either using the tip node if that setting is enabled, or by using the distances of the second bone's children. If the tip node is not enabled and the bone has no children, then the length cannot be autocalculated. In this case, the length will either have to be manually inputted or a tip node used to calculate the length.
func set_auto_calculate_joint_length(auto_calculate_joint_length: bool) -> void:
	pass;

#desc Sets the bone index, [param bone_idx], of the first bone. When possible, this will also update the [code]bone_name[/code] of the first bone based on data provided by the [Skeleton3D].
func set_joint_one_bone_idx(bone_idx: int) -> void:
	pass;

#desc Sets the bone name, [param bone_name], of the first bone. When possible, this will also update the [code]bone_index[/code] of the first bone based on data provided by the [Skeleton3D].
func set_joint_one_bone_name(bone_name: String) -> void:
	pass;

#desc Sets the length of the first bone in the TwoBoneIK modification.
func set_joint_one_length(bone_length: float) -> void:
	pass;

#desc Sets the amount of roll/twist applied to the first bone in the TwoBoneIK modification.
func set_joint_one_roll(roll: float) -> void:
	pass;

#desc Sets the bone index, [param bone_idx], of the second bone. When possible, this will also update the [code]bone_name[/code] of the second bone based on data provided by the [Skeleton3D].
func set_joint_two_bone_idx(bone_idx: int) -> void:
	pass;

#desc Sets the bone name, [param bone_name], of the second bone. When possible, this will also update the [code]bone_index[/code] of the second bone based on data provided by the [Skeleton3D].
func set_joint_two_bone_name(bone_name: String) -> void:
	pass;

#desc Sets the length of the second bone in the TwoBoneIK modification.
func set_joint_two_length(bone_length: float) -> void:
	pass;

#desc Sets the amount of roll/twist applied to the second bone in the TwoBoneIK modification.
func set_joint_two_roll(roll: float) -> void:
	pass;

#desc Sets the node to be used as the for the pole of the TwoBoneIK. When a node is set and the modification is set to use the pole node, the TwoBoneIK modification will bend the nodes in the direction towards this node when the bones need to bend.
func set_pole_node(pole_nodepath: NodePath) -> void:
	pass;

#desc Sets the node to be used as the tip for the second bone. This is used to calculate the length and position of the end of the second bone in the TwoBoneIK modification.
#desc [b]Note:[/b] The tip node should generally be a child node of a [BoneAttachment3D] node attached to the second bone, with the child node being offset so it is at the end of the bone.
func set_tip_node(tip_nodepath: NodePath) -> void:
	pass;

#desc When [code]true[/code], the TwoBoneIK modification will bend the bones towards the pole node, if one has been set. This gives control over the direction the TwoBoneIK solver will bend, which is helpful for joints like elbows that only bend in certain directions.
func set_use_pole_node(use_pole_node: bool) -> void:
	pass;

#desc When [code]true[/code], the TwoBoneIK modification will use the tip node to calculate the distance and position of the end/tip of the second bone. This is the most stable solution for knowing the tip position and length of the second bone.
func set_use_tip_node(use_tip_node: bool) -> void:
	pass;


