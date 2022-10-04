extends SkeletonModification2D
#brief A modification that rotates a [Bone2D] node to look at a target.
#desc This [SkeletonModification2D] rotates a bone to look a target. This is extremely helpful for moving character's head to look at the player, rotating a turret to look at a target, or any other case where you want to make a bone rotate towards something quickly and easily.
class_name SkeletonModification2DLookAt


#desc The [Bone2D] node that the modification will operate on.
var bone2d_node: NodePath;

#desc The index of the [Bone2D] node that the modification will oeprate on.
var bone_index: int;

#desc The NodePath to the node that is the target for the LookAt modification. This node is what the modification will rotate the [Bone2D] to.
var target_nodepath: NodePath;



#desc Returns the amount of additional rotation that is applied after the LookAt modification executes.
func get_additional_rotation() -> float:
	pass;

#desc Returns whether the constraints to this modification are inverted or not.
func get_constraint_angle_invert() -> bool:
	pass;

#desc Returns the constraint's maximum allowed angle.
func get_constraint_angle_max() -> float:
	pass;

#desc Returns the constraint's minimum allowed angle.
func get_constraint_angle_min() -> float:
	pass;

#desc Returns [code]true[/code] if the LookAt modification is using constraints.
func get_enable_constraint() -> bool:
	pass;

#desc Sets the amount of additional rotation that is to be applied after executing the modification. This allows for offsetting the results by the inputted rotation amount.
func set_additional_rotation(rotation: float) -> void:
	pass;

#desc When [code]true[/code], the modification will use an inverted joint constraint.
#desc An inverted joint constraint only constraints the [Bone2D] to the angles [i]outside of[/i] the inputted minimum and maximum angles. For this reason, it is referred to as an inverted joint constraint, as it constraints the joint to the outside of the inputted values.
func set_constraint_angle_invert(invert: bool) -> void:
	pass;

#desc Sets the constraint's maximum allowed angle.
func set_constraint_angle_max(angle_max: float) -> void:
	pass;

#desc Sets the constraint's minimum allowed angle.
func set_constraint_angle_min(angle_min: float) -> void:
	pass;

#desc Sets whether this modification will use constraints or not. When [code]true[/code], constraints will be applied when solving the LookAt modification.
func set_enable_constraint(enable_constraint: bool) -> void:
	pass;


