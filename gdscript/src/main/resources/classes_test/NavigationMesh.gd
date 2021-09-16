extends Resource
class_name NavigationMesh
const SAMPLE_PARTITION_WATERSHED = 0;
const SAMPLE_PARTITION_MONOTONE = 1;
const SAMPLE_PARTITION_LAYERS = 2;
const PARSED_GEOMETRY_MESH_INSTANCES = 0;
const PARSED_GEOMETRY_STATIC_COLLIDERS = 1;
const PARSED_GEOMETRY_BOTH = 2;

var agent/height: float;
var agent/max_climb: float;
var agent/max_slope: float;
var agent/radius: float;
var cell/height: float;
var cell/size: float;
var detail/sample_distance: float;
var detail/sample_max_error: float;
var edge/max_error: float;
var edge/max_length: float;
var filter/filter_walkable_low_height_spans: bool;
var filter/ledge_spans: bool;
var filter/low_hanging_obstacles: bool;
var geometry/collision_mask: int;
var geometry/parsed_geometry_type: int;
var geometry/source_geometry_mode: int;
var geometry/source_group_name: StringName;
var polygon/verts_per_poly: float;
var region/merge_size: float;
var region/min_size: float;
var sample_partition_type/sample_partition_type: int;

func add_polygon(polygon: PackedInt32Array) -> void:
    pass;
func clear_polygons() -> void:
    pass;
func create_from_mesh(mesh: Mesh) -> void:
    pass;
func get_collision_mask_bit(bit: int) -> bool:
    pass;
func get_polygon(idx: int) -> PackedInt32Array:
    pass;
func get_polygon_count() -> int:
    pass;
func get_vertices() -> PackedVector3Array:
    pass;
func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;
func set_vertices(vertices: PackedVector3Array) -> void:
    pass;
