#brief A modification that uses CCDIK to manipulate a series of bones to reach a target in 2D.
#desc This [SkeletonModification2D] uses an algorithm called Cyclic Coordinate Descent Inverse Kinematics, or CCDIK, to manipulate a chain of bones in a [Skeleton2D] so it reaches a defined target.
#desc CCDIK works by rotating a set of bones, typically called a "bone chain", on a single axis. Each bone is rotated to face the target from the tip (by default), which over a chain of bones allow it to rotate properly to reach the target. Because the bones only rotate on a single axis, CCDIK [i]can[/i] look more robotic than other IK solvers.
#desc [b]Note:[/b] The CCDIK modifier has [code]ccdik_joints[/code], which are the data objects that hold the data for each joint in the CCDIK chain. This is different from a bone! CCDIK joints hold the data needed for each bone in the bone chain used by CCDIK.
#desc CCDIK also fully supports angle constraints, allowing for more control over how a solution is met.
class_name SkeletonModification2DCCDIK


#desc The number of CCDIK joints in the CCDIK modification.
var ccdik_data_chain_length: int;

#desc The NodePath to the node that is the target for the CCDIK modification. This node is what the CCDIK chain will attempt to rotate the bone chain to.
var target_nodepath: NodePath;

#desc The end position of the CCDIK chain. Typically, this should be a child of a [Bone2D] node attached to the final [Bone2D] in the CCDIK chain.
var tip_nodepath: NodePath;



#desc Returns the [Bone2D] node assigned to the CCDIK joint at [param joint_idx].
func get_ccdik_joint_bone2d_node() -> NodePath:
	pass;

#desc Returns the index of the [Bone2D] node assigned to the CCDIK joint at [param joint_idx].
func get_ccdik_joint_bone_index() -> int:
	pass;

#desc Returns whether the CCDIK joint at [param joint_idx] uses an inverted joint constraint. See [method set_ccdik_joint_constraint_angle_invert] for details.
func get_ccdik_joint_constraint_angle_invert() -> bool:
	pass;

#desc Returns the maximum angle constraint for the joint at [param joint_idx].
func get_ccdik_joint_constraint_angle_max() -> float:
	pass;

#desc Returns the minimum angle constraint for the joint at [param joint_idx].
func get_ccdik_joint_constraint_angle_min() -> float:
	pass;

#desc Returns whether angle constraints on the CCDIK joint at [param joint_idx] are enabled.
func get_ccdik_joint_enable_constraint() -> bool:
	pass;

#desc Returns whether the joint at [param joint_idx] is set to rotate from the joint, [code]true[/code], or to rotate from the tip, [code]false[/code]. The default is to rotate from the tip.
func get_ccdik_joint_rotate_from_joint() -> bool:
	pass;

#desc Sets the [Bone2D] node assigned to the CCDIK joint at [param joint_idx].
func set_ccdik_joint_bone2d_node(joint_idx: int, bone2d_nodepath: NodePath) -> void:
	pass;

#desc Sets the bone index, [param bone_idx], of the CCDIK joint at [param joint_idx]. When possible, this will also update the [code]bone2d_node[/code] of the CCDIK joint based on data provided by the linked skeleton.
func set_ccdik_joint_bone_index(joint_idx: int, bone_idx: int) -> void:
	pass;

#desc Sets whether the CCDIK joint at [param joint_idx] uses an inverted joint constraint.
#desc An inverted joint constraint only constraints the CCDIK joint to the angles [i]outside of[/i] the inputted minimum and maximum angles. For this reason, it is referred to as an inverted joint constraint, as it constraints the joint to the outside of the inputted values.
func set_ccdik_joint_constraint_angle_invert(joint_idx: int, invert: bool) -> void:
	pass;

#desc Sets the maximum angle constraint for the joint at [param joint_idx].
func set_ccdik_joint_constraint_angle_max(joint_idx: int, angle_max: float) -> void:
	pass;

#desc Sets the minimum angle constraint for the joint at [param joint_idx].
func set_ccdik_joint_constraint_angle_min(joint_idx: int, angle_min: float) -> void:
	pass;

#desc Determines whether angle constraints on the CCDIK joint at [param joint_idx] are enabled. When [code]true[/code], constraints will be enabled and taken into account when solving.
func set_ccdik_joint_enable_constraint(joint_idx: int, enable_constraint: bool) -> void:
	pass;

#desc Sets whether the joint at [param joint_idx] is set to rotate from the joint, [code]true[/code], or to rotate from the tip, [code]false[/code].
func set_ccdik_joint_rotate_from_joint(joint_idx: int, rotate_from_joint: bool) -> void:
	pass;


