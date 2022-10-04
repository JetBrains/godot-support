#brief A modification that jiggles bones as they move towards a target.
#desc This modification moves a series of bones, typically called a bone chain, towards a target. What makes this modification special is that it calculates the velocity and acceleration for each bone in the bone chain, and runs a very light physics-like calculation using the inputted values. This allows the bones to overshoot the target and "jiggle" around. It can be configured to act more like a spring, or sway around like cloth might.
#desc This modification is useful for adding additional motion to things like hair, the edges of clothing, and more. It has several settings to that allow control over how the joint moves when the target moves.
#desc [b]Note:[/b] The Jiggle modifier has [code]jiggle_joints[/code], which are the data objects that hold the data for each joint in the Jiggle chain. This is different from a bone! Jiggle joints hold the data needed for each bone in the bone chain used by the Jiggle modification.
class_name SkeletonModification3DJiggle


#desc The default amount of dampening applied to the Jiggle joints, if they are not overridden. Higher values lead to more of the calculated velocity being applied.
var damping: float;

#desc The default amount of gravity applied to the Jiggle joints, if they are not overridden.
var gravity: Vector3;

#desc The amount of Jiggle joints in the Jiggle modification.
var jiggle_data_chain_length: int;

#desc The default amount of mass assigned to the Jiggle joints, if they are not overridden. Higher values lead to faster movements and more overshooting.
var mass: float;

#desc The default amount of stiffness assigned to the Jiggle joints, if they are not overridden. Higher values act more like springs, quickly moving into the correct position.
var stiffness: float;

#desc The NodePath to the node that is the target for the Jiggle modification. This node is what the Jiggle chain will attempt to rotate the bone chain to.
var target_nodepath: NodePath;

#desc Whether the gravity vector, [member gravity], should be applied to the Jiggle joints, assuming they are not overriding the default settings.
var use_gravity: bool;



#desc Returns the collision mask that the Jiggle modifier will take into account when performing physics calculations.
func get_collision_mask() -> int:
	pass;

#desc Returns the bone index of the bone assigned to the Jiggle joint at [param joint_idx].
func get_jiggle_joint_bone_index() -> int:
	pass;

#desc Returns the name of the bone that is assigned to the Jiggle joint at [param joint_idx].
func get_jiggle_joint_bone_name() -> String:
	pass;

#desc Returns the amount of dampening of the Jiggle joint at [param joint_idx].
func get_jiggle_joint_damping() -> float:
	pass;

#desc Returns a [Vector3] representign the amount of gravity the Jiggle joint at [param joint_idx] is influenced by.
func get_jiggle_joint_gravity() -> Vector3:
	pass;

#desc Returns the amount of mass of the Jiggle joint at [param joint_idx].
func get_jiggle_joint_mass() -> float:
	pass;

#desc Returns a boolean that indicates whether the joint at [param joint_idx] is overriding the default jiggle joint data defined in the modification.
func get_jiggle_joint_override() -> bool:
	pass;

#desc Returns the amount of roll/twist applied to the bone that the Jiggle joint is applied to.
func get_jiggle_joint_roll() -> float:
	pass;

#desc Returns the stiffness of the Jiggle joint at [param joint_idx].
func get_jiggle_joint_stiffness() -> float:
	pass;

#desc Returns a boolean that indicates whether the joint at [param joint_idx] is using gravity or not.
func get_jiggle_joint_use_gravity() -> bool:
	pass;

#desc Returns whether the Jiggle modifier is taking physics colliders into account when solving.
func get_use_colliders() -> bool:
	pass;

#desc Sets the collision mask that the Jiggle modifier takes into account when performing physics calculations.
func set_collision_mask() -> void:
	pass;

#desc Sets the bone index, [param bone_idx], of the Jiggle joint at [param joint_idx]. When possible, this will also update the [code]bone_name[/code] of the Jiggle joint based on data provided by the [Skeleton3D].
func set_jiggle_joint_bone_index(joint_idx: int, bone_idx: int) -> void:
	pass;

#desc Sets the bone name, [param name], of the Jiggle joint at [param joint_idx]. When possible, this will also update the [code]bone_index[/code] of the Jiggle joint based on data provided by the [Skeleton3D].
func set_jiggle_joint_bone_name(joint_idx: int, name: String) -> void:
	pass;

#desc Sets the amount of dampening of the Jiggle joint at [param joint_idx].
func set_jiggle_joint_damping(joint_idx: int, damping: float) -> void:
	pass;

#desc Sets the gravity vector of the Jiggle joint at [param joint_idx].
func set_jiggle_joint_gravity(joint_idx: int, gravity: Vector3) -> void:
	pass;

#desc Sets the of mass of the Jiggle joint at [param joint_idx].
func set_jiggle_joint_mass(joint_idx: int, mass: float) -> void:
	pass;

#desc Sets whether the Jiggle joint at [param joint_idx] should override the default Jiggle joint settings. Setting this to true will make the joint use its own settings rather than the default ones attached to the modification.
func set_jiggle_joint_override(joint_idx: int, override: bool) -> void:
	pass;

#desc Sets the amount of roll/twist on the bone the Jiggle joint is attached to.
func set_jiggle_joint_roll(joint_idx: int, roll: float) -> void:
	pass;

#desc Sets the of stiffness of the Jiggle joint at [param joint_idx].
func set_jiggle_joint_stiffness(joint_idx: int, stiffness: float) -> void:
	pass;

#desc Sets whether the Jiggle joint at [param joint_idx] should use gravity.
func set_jiggle_joint_use_gravity(joint_idx: int, use_gravity: bool) -> void:
	pass;

#desc When [code]true[/code], the Jiggle modifier will use raycasting to prevent the Jiggle joints from rotating themselves into collision objects when solving.
func set_use_colliders() -> void:
	pass;


