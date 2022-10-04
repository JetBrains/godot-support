extends SkeletonModification2D
#brief A modification that rotates two bones using the law of cosigns to reach the target.
#desc This [SkeletonModification2D] uses an algorithm typically called TwoBoneIK. This algorithm works by leveraging the law of cosigns and the lengths of the bones to figure out what rotation the bones currently have, and what rotation they need to make a complete triangle, where the first bone, the second bone, and the target form the three vertices of the triangle. Because the algorithm works by making a triangle, it can only operate on two bones.
#desc TwoBoneIK is great for arms, legs, and really any joints that can be represented by just two bones that bend to reach a target. This solver is more lightweight than [SkeletonModification2DFABRIK], but gives similar, natural looking results.
class_name SkeletonModification2DTwoBoneIK


#desc If [code]true[/code], the bones in the modification will blend outward as opposed to inwards when contracting. If [code]false[/code], the bones will bend inwards when contracting.
var flip_bend_direction: bool;

#desc The maximum distance the target can be at. If the target is farther than this distance, the modification will solve as if it's at this maximum distance. When set to [code]0[/code], the modification will solve without distance constraints.
var target_maximum_distance: float;

#desc The minimum distance the target can be at. If the target is closer than this distance, the modification will solve as if it's at this minimum distance. When set to [code]0[/code], the modification will solve without distance constraints.
var target_minimum_distance: float;

#desc The NodePath to the node that is the target for the TwoBoneIK modification. This node is what the modification will use when bending the [Bone2D] nodes.
var target_nodepath: NodePath;



#desc Returns the [Bone2D] node that is being used as the first bone in the TwoBoneIK modification.
func get_joint_one_bone2d_node() -> NodePath:
	pass;

#desc Returns the index of the [Bone2D] node that is being used as the first bone in the TwoBoneIK modification.
func get_joint_one_bone_idx() -> int:
	pass;

#desc Returns the [Bone2D] node that is being used as the second bone in the TwoBoneIK modification.
func get_joint_two_bone2d_node() -> NodePath:
	pass;

#desc Returns the index of the [Bone2D] node that is being used as the second bone in the TwoBoneIK modification.
func get_joint_two_bone_idx() -> int:
	pass;

#desc Sets the [Bone2D] node that is being used as the first bone in the TwoBoneIK modification.
func set_joint_one_bone2d_node(bone2d_node: NodePath) -> void:
	pass;

#desc Sets the index of the [Bone2D] node that is being used as the first bone in the TwoBoneIK modification.
func set_joint_one_bone_idx(bone_idx: int) -> void:
	pass;

#desc Sets the [Bone2D] node that is being used as the second bone in the TwoBoneIK modification.
func set_joint_two_bone2d_node(bone2d_node: NodePath) -> void:
	pass;

#desc Sets the index of the [Bone2D] node that is being used as the second bone in the TwoBoneIK modification.
func set_joint_two_bone_idx(bone_idx: int) -> void:
	pass;


