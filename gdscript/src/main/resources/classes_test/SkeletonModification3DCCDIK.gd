#brief A modification that uses CCDIK to manipulate a series of bones to reach a target.
#desc This [SkeletonModification3D] uses an algorithm called Cyclic Coordinate Descent Inverse Kinematics, or CCDIK, to manipulate a chain of bones in a Skeleton so it reaches a defined target.
#desc CCDIK works by rotating a set of bones, typically called a "bone chain", on a single axis. Each bone is rotated to face the target from the tip (by default), which over a chain of bones allow it to rotate properly to reach the target. Because the bones only rotate on a single axis, CCDIK [i]can[/i] look more robotic than other IK solvers.
#desc [b]Note:[/b] The CCDIK modifier has [code]ccdik_joints[/code], which are the data objects that hold the data for each joint in the CCDIK chain. This is different from a bone! CCDIK joints hold the data needed for each bone in the bone chain used by CCDIK.
#desc CCDIK also fully supports angle constraints, allowing for more control over how a solution is met.
class_name SkeletonModification3DCCDIK


#desc The number of CCDIK joints in the CCDIK modification.
var ccdik_data_chain_length: int;

#desc When true, the CCDIK algorithm will perform a higher quality solve that returns more natural results. A high quality solve requires more computation power to solve though, and therefore can be disabled to save performance.
var high_quality_solve: bool;

#desc The NodePath to the node that is the target for the CCDIK modification. This node is what the CCDIK chain will attempt to rotate the bone chain to.
var target_nodepath: NodePath;

#desc The end position of the CCDIK chain. Typically, this should be a child of a [BoneAttachment3D] node attached to the final bone in the CCDIK chain, where the child node is offset so it is at the end of the final bone.
var tip_nodepath: NodePath;



#desc Returns the bone index of the bone assigned to the CCDIK joint at [param joint_idx].
func get_ccdik_joint_bone_index(joint_idx: int) -> int:
	pass;

#desc Returns the name of the bone that is assigned to the CCDIK joint at [param joint_idx].
func get_ccdik_joint_bone_name(joint_idx: int) -> String:
	pass;

#desc Returns the integer representing the joint axis of the CCDIK joint at [param joint_idx].
func get_ccdik_joint_ccdik_axis(joint_idx: int) -> int:
	pass;

#desc Returns the maximum angle constraint for the joint at [param joint_idx]. [b]Note:[/b] This angle is in degrees!
func get_ccdik_joint_constraint_angle_max(joint_idx: int) -> float:
	pass;

#desc Returns the minimum angle constraint for the joint at [param joint_idx]. [b]Note:[/b] This angle is in degrees!
func get_ccdik_joint_constraint_angle_min(joint_idx: int) -> float:
	pass;

#desc Returns whether the CCDIK joint at [param joint_idx] uses an inverted joint constraint. See [method set_ccdik_joint_constraint_invert] for details.
func get_ccdik_joint_constraint_invert(joint_idx: int) -> bool:
	pass;

#desc Enables angle constraints to the CCDIK joint at [param joint_idx].
func get_ccdik_joint_enable_joint_constraint(joint_idx: int) -> bool:
	pass;

#desc Sets the bone index, [param bone_index], of the CCDIK joint at [param joint_idx]. When possible, this will also update the [code]bone_name[/code] of the CCDIK joint based on data provided by the linked skeleton.
func set_ccdik_joint_bone_index(joint_idx: int, bone_index: int) -> void:
	pass;

#desc Sets the bone name, [param bone_name], of the CCDIK joint at [param joint_idx]. When possible, this will also update the [code]bone_index[/code] of the CCDIK joint based on data provided by the linked skeleton.
func set_ccdik_joint_bone_name(joint_idx: int, bone_name: String) -> void:
	pass;

#desc Sets the joint axis of the CCDIK joint at [param joint_idx] to the passed-in joint axis, [param axis].
func set_ccdik_joint_ccdik_axis(joint_idx: int, axis: int) -> void:
	pass;

#desc Sets the maximum angle constraint for the joint at [param joint_idx]. [b]Note:[/b] This angle must be in radians!
func set_ccdik_joint_constraint_angle_max(joint_idx: int, max_angle: float) -> void:
	pass;

#desc Sets the minimum angle constraint for the joint at [param joint_idx]. [b]Note:[/b] This angle must be in radians!
func set_ccdik_joint_constraint_angle_min(joint_idx: int, min_angle: float) -> void:
	pass;

#desc Sets whether the CCDIK joint at [param joint_idx] uses an inverted joint constraint.
#desc An inverted joint constraint only constraints the CCDIK joint to the angles [i]outside of[/i] the inputted minimum and maximum angles. For this reason, it is referred to as an inverted joint constraint, as it constraints the joint to the outside of the inputted values.
func set_ccdik_joint_constraint_invert(joint_idx: int, invert: bool) -> void:
	pass;

#desc Sets whether joint constraints are enabled for the CCDIK joint at [param joint_idx].
func set_ccdik_joint_enable_joint_constraint(joint_idx: int, enable: bool) -> void:
	pass;


