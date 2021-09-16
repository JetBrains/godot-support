extends SkeletonModification2D
class_name SkeletonModification2DPhysicalBones

var physical_bone_chain_length: int;

func fetch_physical_bones() -> void:
    pass;
func get_physical_bone_node(joint_idx: int) -> NodePath:
    pass;
func set_physical_bone_node(joint_idx: int, physicalbone2d_node: NodePath) -> void:
    pass;
func start_simulation(bones: Array[StringName]) -> void:
    pass;
func stop_simulation(bones: Array[StringName]) -> void:
    pass;
