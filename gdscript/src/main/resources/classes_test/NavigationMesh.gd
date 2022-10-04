#brief A mesh to approximate the walkable areas and obstacles.
#desc A navigation mesh is a collection of polygons that define which areas of an environment are traversable to aid agents in pathfinding through complicated spaces.
class_name NavigationMesh

#desc Watershed partitioning. Generally the best choice if you precompute the navigation mesh, use this if you have large open areas.
const SAMPLE_PARTITION_WATERSHED = 0;

#desc Monotone partitioning. Use this if you want fast navigation mesh generation.
const SAMPLE_PARTITION_MONOTONE = 1;

#desc Layer partitioning. Good choice to use for tiled navigation mesh with medium and small sized tiles.
const SAMPLE_PARTITION_LAYERS = 2;

#desc Represents the size of the [enum SamplePartitionType] enum.
const SAMPLE_PARTITION_MAX = 3;

#desc Parses mesh instances as geometry. This includes [MeshInstance3D], [CSGShape3D], and [GridMap] nodes.
const PARSED_GEOMETRY_MESH_INSTANCES = 0;

#desc Parses [StaticBody3D] colliders as geometry. The collider should be in any of the layers specified by [member geometry_collision_mask].
const PARSED_GEOMETRY_STATIC_COLLIDERS = 1;

#desc Both [constant PARSED_GEOMETRY_MESH_INSTANCES] and [constant PARSED_GEOMETRY_STATIC_COLLIDERS].
const PARSED_GEOMETRY_BOTH = 2;

#desc Represents the size of the [enum ParsedGeometryType] enum.
const PARSED_GEOMETRY_MAX = 3;

#desc Scans the child nodes of [NavigationRegion3D] recursively for geometry.
const SOURCE_GEOMETRY_NAVMESH_CHILDREN = 0;

#desc Scans nodes in a group and their child nodes recursively for geometry. The group is specified by [member geometry_source_group_name].
const SOURCE_GEOMETRY_GROUPS_WITH_CHILDREN = 1;

#desc Uses nodes in a group for geometry. The group is specified by [member geometry_source_group_name].
const SOURCE_GEOMETRY_GROUPS_EXPLICIT = 2;

#desc Represents the size of the [enum SourceGeometryMode] enum.
const SOURCE_GEOMETRY_MAX = 3;


#desc The minimum floor to ceiling height that will still allow the floor area to be considered walkable.
#desc [b]Note:[/b] While baking, this value will be rounded up to the nearest multiple of [member cell_height].
var agent_height: float;

#desc The minimum ledge height that is considered to still be traversable.
#desc [b]Note:[/b] While baking, this value will be rounded down to the nearest multiple of [member cell_height].
var agent_max_climb: float;

#desc The maximum slope that is considered walkable, in degrees.
var agent_max_slope: float;

#desc The distance to erode/shrink the walkable area of the heightfield away from obstructions.
#desc [b]Note:[/b] While baking, this value will be rounded up to the nearest multiple of [member cell_size].
var agent_radius: float;

#desc The Y axis cell size to use for fields.
var cell_height: float;

#desc The XZ plane cell size to use for fields.
var cell_size: float;

#desc The sampling distance to use when generating the detail mesh, in cell unit.
var detail_sample_distance: float;

#desc The maximum distance the detail mesh surface should deviate from heightfield, in cell unit.
var detail_sample_max_error: float;

#desc The maximum distance a simplfied contour's border edges should deviate the original raw contour.
var edge_max_error: float;

#desc The maximum allowed length for contour edges along the border of the mesh.
#desc [b]Note:[/b] While baking, this value will be rounded up to the nearest multiple of [member cell_size].
var edge_max_length: float;

#desc If the baking [AABB] has a volume the navigation mesh baking will be restricted to its enclosing area.
var filter_baking_aabb: AABB;

#desc The position offset applied to the [member filter_baking_aabb] [AABB].
var filter_baking_aabb_offset: Vector3;

#desc If [code]true[/code], marks spans that are ledges as non-walkable.
var filter_ledge_spans: bool;

#desc If [code]true[/code], marks non-walkable spans as walkable if their maximum is within [member agent_max_climb] of a walkable neighbor.
var filter_low_hanging_obstacles: bool;

#desc If [code]true[/code], marks walkable spans as not walkable if the clearance above the span is less than [member agent_height].
var filter_walkable_low_height_spans: bool;

#desc The physics layers to scan for static colliders.
#desc Only used when [member geometry_parsed_geometry_type] is [constant PARSED_GEOMETRY_STATIC_COLLIDERS] or [constant PARSED_GEOMETRY_BOTH].
var geometry_collision_mask: int;

#desc Determines which type of nodes will be parsed as geometry. See [enum ParsedGeometryType] for possible values.
var geometry_parsed_geometry_type: int;

#desc The source of the geometry used when baking. See [enum SourceGeometryMode] for possible values.
var geometry_source_geometry_mode: int;

#desc The name of the group to scan for geometry.
#desc Only used when [member geometry_source_geometry_mode] is [constant SOURCE_GEOMETRY_GROUPS_WITH_CHILDREN] or [constant SOURCE_GEOMETRY_GROUPS_EXPLICIT].
var geometry_source_group_name: StringName;

#desc The maximum number of vertices allowed for polygons generated during the contour to polygon conversion process.
var polygon_verts_per_poly: float;

#desc Any regions with a size smaller than this will be merged with larger regions if possible.
#desc [b]Note:[/b] This value will be squared to calculate the number of cells. For example, a value of 20 will set the number of cells to 400.
var region_merge_size: float;

#desc The minimum size of a region for it to be created.
#desc [b]Note:[/b] This value will be squared to calculate the minimum number of cells allowed to form isolated island areas. For example, a value of 8 will set the number of cells to 64.
var region_min_size: float;

#desc Partitioning algorithm for creating the navigation mesh polys. See [enum SamplePartitionType] for possible values.
var sample_partition_type: int;



#desc Adds a polygon using the indices of the vertices you get when calling [method get_vertices].
func add_polygon() -> void:
	pass;

#desc Clears the array of polygons, but it doesn't clear the array of vertices.
func clear_polygons() -> void:
	pass;

#desc Initializes the navigation mesh by setting the vertices and indices according to a [Mesh].
#desc [b]Note:[/b] The given [param mesh] must be of type [constant Mesh.PRIMITIVE_TRIANGLES] and have an index array.
func create_from_mesh() -> void:
	pass;

#desc Returns whether or not the specified layer of the [member geometry_collision_mask] is enabled, given a [param layer_number] between 1 and 32.
func get_collision_mask_value() -> bool:
	pass;

#desc Returns a [PackedInt32Array] containing the indices of the vertices of a created polygon.
func get_polygon() -> PackedInt32Array:
	pass;

#desc Returns the number of polygons in the navigation mesh.
func get_polygon_count() -> int:
	pass;

#desc Returns a [PackedVector3Array] containing all the vertices being used to create the polygons.
func get_vertices() -> PackedVector3Array:
	pass;

#desc Based on [param value], enables or disables the specified layer in the [member geometry_collision_mask], given a [param layer_number] between 1 and 32.
func set_collision_mask_value(layer_number: int, value: bool) -> void:
	pass;

#desc Sets the vertices that can be then indexed to create polygons with the [method add_polygon] method.
func set_vertices() -> void:
	pass;


