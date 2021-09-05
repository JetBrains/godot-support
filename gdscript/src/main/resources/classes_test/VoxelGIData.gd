extends Resource
class_name VoxelGIData


var bias: float setget set_bias, get_bias;
var dynamic_range: float setget set_dynamic_range, get_dynamic_range;
var energy: float setget set_energy, get_energy;
var interior: bool setget set_interior, is_interior;
var normal_bias: float setget set_normal_bias, get_normal_bias;
var propagation: float setget set_propagation, get_propagation;
var use_two_bounces: bool setget set_use_two_bounces, is_using_two_bounces;

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

