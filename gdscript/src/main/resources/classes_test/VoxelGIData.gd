extends Resource
class_name VoxelGIData

var bias: float;
var dynamic_range: float;
var energy: float;
var interior: bool;
var normal_bias: float;
var propagation: float;
var use_two_bounces: bool;

func allocate(to_cell_xform: Transform3D, aabb: AABB, octree_size: Vector3, octree_cells: PackedByteArray, data_cells: PackedByteArray, distance_field: PackedByteArray, level_counts: PackedInt32Array) -> void:
    pass;
func get_bounds() -> AABB:
    pass;
func get_data_cells() -> PackedByteArray:
    pass;
func get_level_counts() -> PackedInt32Array:
    pass;
func get_octree_cells() -> PackedByteArray:
    pass;
func get_octree_size() -> Vector3:
    pass;
func get_to_cell_xform() -> Transform3D:
    pass;
