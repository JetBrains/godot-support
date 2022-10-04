#brief A modification that uses FABRIK to manipulate a series of bones to reach a target.
#desc This [SkeletonModification3D] uses an algorithm called Forward And Backward Reaching Inverse Kinematics, or FABRIK, to rotate a bone chain so that it reaches a target.
#desc FABRIK works by knowing the positions and lengths of a series of bones, typically called a "bone chain". It first starts by running a forward pass, which places the final bone at the target's position. Then all other bones are moved towards the tip bone, so they stay at the defined bone length away. Then a backwards pass is performed, where the root/first bone in the FABRIK chain is placed back at the origin. then all other bones are moved so they stay at the defined bone length away. This positions the bone chain so that it reaches the target when possible, but all of the bones stay the correct length away from each other.
#desc Because of how FABRIK works, it often gives more natural results than those seen in [SkeletonModification3DCCDIK], though FABRIK currently does not support joint constraints.
#desc [b]Note:[/b] The FABRIK modifier has [code]fabrik_joints[/code], which are the data objects that hold the data for each joint in the FABRIK chain. This is different from a bone! FABRIK joints hold the data needed for each bone in the bone chain used by FABRIK.
#desc To help control how the FABRIK joints move, a magnet vector can be passed, which can nudge the bones in a certain direction prior to solving, giving a level of control over the final result.
class_name SkeletonModification3DFABRIK


#desc The number of times FABRIK will try to solve each time the [code]execute[/code] function is called. Setting this value to a lower number will be result in better performance, but this can also result in harsher movements and slower solves.
var chain_max_iterations: int;

#desc The minimum distance the target has to be from the tip of the final bone in the bone chain. Setting this value to a higher number allows for greater performance, but less accurate solves.
var chain_tolerance: float;

#desc The amount of FABRIK joints in the FABRIK modification.
var fabrik_data_chain_length: int;

#desc The NodePath to the node that is the target for the FABRIK modification. This node is what the FABRIK chain will attempt to rotate the bone chain to.
var target_nodepath: NodePath;



#desc Will attempt to automatically calculate the length of the bone assigned to the FABRIK joint at [param joint_idx].
func fabrik_joint_auto_calculate_length(joint_idx: int) -> void:
	pass;

#desc Returns a boolean that indicates whether this modification will attempt to autocalculate the length of the bone assigned to the FABRIK joint at [param joint_idx].
func get_fabrik_joint_auto_calculate_length(joint_idx: int) -> bool:
	pass;

#desc Returns the bone index of the bone assigned to the FABRIK joint at [param joint_idx].
func get_fabrik_joint_bone_index(joint_idx: int) -> int:
	pass;

#desc Returns the name of the bone that is assigned to the FABRIK joint at [param joint_idx].
func get_fabrik_joint_bone_name(joint_idx: int) -> String:
	pass;

#desc Returns the length of the FABRIK joint at [param joint_idx].
func get_fabrik_joint_length(joint_idx: int) -> float:
	pass;

#desc Returns the magnet vector of the FABRIK joint at [param joint_idx].
func get_fabrik_joint_magnet(joint_idx: int) -> Vector3:
	pass;

#desc Returns the [Node3D]-based node placed at the tip of the FABRIK joint at [param joint_idx], if one has been set.
func get_fabrik_joint_tip_node(joint_idx: int) -> NodePath:
	pass;

#desc Returns a boolean indicating whether the FABRIK joint uses the target's [Basis] for its rotation.
#desc [b]Note:[/b] This option is only available for the final bone in the FABRIK chain, with this setting being ignored for all other bones.
func get_fabrik_joint_use_target_basis(joint_idx: int) -> bool:
	pass;

#desc Sets the [Node3D]-based node that will be used as the tip of the FABRIK joint at [param joint_idx].
func get_fabrik_joint_use_tip_node(joint_idx: int) -> bool:
	pass;

#desc When [code]true[/code], this modification will attempt to automatically calculate the length of the bone for the FABRIK joint at [param joint_idx]. It does this by either using the tip node assigned, if there is one assigned, or the distance the of the bone's children, if the bone has any. If the bone has no children and no tip node is assigned, then the modification [b]cannot[/b] autocalculate the joint's length. In this case, the joint length should be entered manually or a tip node assigned.
func set_fabrik_joint_auto_calculate_length(joint_idx: int, auto_calculate_length: bool) -> void:
	pass;

#desc Sets the bone index, [param bone_index], of the FABRIK joint at [param joint_idx]. When possible, this will also update the [code]bone_name[/code] of the FABRIK joint based on data provided by the [Skeleton3D].
func set_fabrik_joint_bone_index(joint_idx: int, bone_index: int) -> void:
	pass;

#desc Sets the bone name, [param bone_name], of the FABRIK joint at [param joint_idx]. When possible, this will also update the [code]bone_index[/code] of the FABRIK joint based on data provided by the [Skeleton3D].
func set_fabrik_joint_bone_name(joint_idx: int, bone_name: String) -> void:
	pass;

#desc Sets the joint length, [param length], of the FABRIK joint at [param joint_idx].
func set_fabrik_joint_length(joint_idx: int, length: float) -> void:
	pass;

#desc Sets the magenet position to [param magnet_position] for the joint at [param joint_idx]. The magnet position is used to nudge the joint in that direction when solving, which gives some control over how that joint will bend when being solved.
func set_fabrik_joint_magnet(joint_idx: int, magnet_position: Vector3) -> void:
	pass;

#desc Sets the nodepath of the FARIK joint at [param joint_idx] to [param tip_node]. The tip node is used to calculate the length of the FABRIK joint when set to automatically calculate joint length.
#desc [b]Note:[/b] The tip node should generally be a child node of a [BoneAttachment3D] node attached to the bone that this FABRIK joint operates on, with the child node being offset so it is at the end of the bone.
func set_fabrik_joint_tip_node(joint_idx: int, tip_node: NodePath) -> void:
	pass;

#desc Sets whether the FABRIK joint at [param joint_idx] uses the target's [Basis] for its rotation.
#desc [b]Note:[/b] This option is only available for the final bone in the FABRIK chain, with this setting being ignored for all other bones.
func set_fabrik_joint_use_target_basis(joint_idx: int, use_target_basis: bool) -> void:
	pass;

#desc Sets whether the tip node should be used when autocalculating the joint length for the FABRIK joint at [param joint_idx]. This will only work if there is a node assigned to the tip nodepath for this joint.
func set_fabrik_joint_use_tip_node(joint_idx: int, use_tip_node: bool) -> void:
	pass;


