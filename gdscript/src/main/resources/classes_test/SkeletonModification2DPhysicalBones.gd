#brief A modification that applies the transforms of [PhysicalBone2D] nodes to [Bone2D] nodes.
#desc This modification takes the transforms of [PhysicalBone2D] nodes and applies them to [Bone2D] nodes. This allows the [Bone2D] nodes to react to physics thanks to the linked [PhysicalBone2D] nodes.
#desc Experimental. Physical bones may be changed in the future to perform the position update of [Bone2D] on their own.
class_name SkeletonModification2DPhysicalBones


#desc The number of [PhysicalBone2D] nodes linked in this modification.
var physical_bone_chain_length: int;



#desc Empties the list of [PhysicalBone2D] nodes and populates it will all [PhysicalBone2D] nodes that are children of the [Skeleton2D].
func fetch_physical_bones() -> void:
	pass;

#desc Returns the [PhysicalBone2D] node at [param joint_idx].
func get_physical_bone_node(joint_idx: int) -> NodePath:
	pass;

#desc Sets the [PhysicalBone2D] node at [param joint_idx].
#desc [b]Note:[/b] This is just the index used for this modification, not the bone index used in the [Skeleton2D].
func set_physical_bone_node(joint_idx: int, physicalbone2d_node: NodePath) -> void:
	pass;

#desc Tell the [PhysicalBone2D] nodes to start simulating and interacting with the physics world.
#desc Optionally, an array of bone names can be passed to this function, and that will cause only [PhysicalBone2D] nodes with those names to start simulating.
func start_simulation(bones: StringName[]) -> void:
	pass;

#desc Tell the [PhysicalBone2D] nodes to stop simulating and interacting with the physics world.
#desc Optionally, an array of bone names can be passed to this function, and that will cause only [PhysicalBone2D] nodes with those names to stop simulating.
func stop_simulation(bones: StringName[]) -> void:
	pass;


