extends Resource
class_name NavigationMesh

const SAMPLE_PARTITION_WATERSHED = 0;
const SAMPLE_PARTITION_MONOTONE = 1;
const SAMPLE_PARTITION_LAYERS = 2;
const PARSED_GEOMETRY_MESH_INSTANCES = 0;
const PARSED_GEOMETRY_STATIC_COLLIDERS = 1;
const PARSED_GEOMETRY_BOTH = 2;

var agent/height: float setget set_agent_height, get_agent_height;
var agent/max_climb: float setget set_agent_max_climb, get_agent_max_climb;
var agent/max_slope: float setget set_agent_max_slope, get_agent_max_slope;
var agent/radius: float setget set_agent_radius, get_agent_radius;
var cell/height: float setget set_cell_height, get_cell_height;
var cell/size: float setget set_cell_size, get_cell_size;
var detail/sample_distance: float setget set_detail_sample_distance, get_detail_sample_distance;
var detail/sample_max_error: float setget set_detail_sample_max_error, get_detail_sample_max_error;
var edge/max_error: float setget set_edge_max_error, get_edge_max_error;
var edge/max_length: float setget set_edge_max_length, get_edge_max_length;
var filter/filter_walkable_low_height_spans: bool setget set_filter_walkable_low_height_spans, get_filter_walkable_low_height_spans;
var filter/ledge_spans: bool setget set_filter_ledge_spans, get_filter_ledge_spans;
var filter/low_hanging_obstacles: bool setget set_filter_low_hanging_obstacles, get_filter_low_hanging_obstacles;
var geometry/collision_mask: int setget set_collision_mask, get_collision_mask;
var geometry/parsed_geometry_type: int setget set_parsed_geometry_type, get_parsed_geometry_type;
var geometry/source_geometry_mode: int setget set_source_geometry_mode, get_source_geometry_mode;
var geometry/source_group_name: String setget set_source_group_name, get_source_group_name;
var polygon/verts_per_poly: float setget set_verts_per_poly, get_verts_per_poly;
var region/merge_size: float setget set_region_merge_size, get_region_merge_size;
var region/min_size: float setget set_region_min_size, get_region_min_size;
var sample_partition_type/sample_partition_type: int setget set_sample_partition_type, get_sample_partition_type;

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

