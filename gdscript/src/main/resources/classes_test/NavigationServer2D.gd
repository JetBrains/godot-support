extends Object
class_name NavigationServer2D


func agent_create() -> RID:
    pass;
func agent_is_map_changed(agent: RID) -> bool:
    pass;
func agent_set_callback(agent: RID, receiver: Object, method: StringName, userdata: Variant) -> void:
    pass;
func agent_set_map(agent: RID, map: RID) -> void:
    pass;
func agent_set_max_neighbors(agent: RID, count: int) -> void:
    pass;
func agent_set_max_speed(agent: RID, max_speed: float) -> void:
    pass;
func agent_set_neighbor_dist(agent: RID, dist: float) -> void:
    pass;
func agent_set_position(agent: RID, position: Vector2) -> void:
    pass;
func agent_set_radius(agent: RID, radius: float) -> void:
    pass;
func agent_set_target_velocity(agent: RID, target_velocity: Vector2) -> void:
    pass;
func agent_set_time_horizon(agent: RID, time: float) -> void:
    pass;
func agent_set_velocity(agent: RID, velocity: Vector2) -> void:
    pass;
func free(object: RID) -> void:
    pass;
func map_create() -> RID:
    pass;
func map_get_cell_size(map: RID) -> float:
    pass;
func map_get_closest_point(map: RID, to_point: Vector2) -> Vector2:
    pass;
func map_get_closest_point_owner(map: RID, to_point: Vector2) -> RID:
    pass;
func map_get_edge_connection_margin(map: RID) -> float:
    pass;
func map_get_path(map: RID, origin: Vector2, destination: Vector2, optimize: bool, layers: int) -> PackedVector2Array:
    pass;
func map_is_active(nap: RID) -> bool:
    pass;
func map_set_active(map: RID, active: bool) -> void:
    pass;
func map_set_cell_size(map: RID, cell_size: float) -> void:
    pass;
func map_set_edge_connection_margin(map: RID, margin: float) -> void:
    pass;
func region_create() -> RID:
    pass;
func region_get_connection_pathway_end(region: RID, connection: int) -> Vector2:
    pass;
func region_get_connection_pathway_start(region: RID, connection: int) -> Vector2:
    pass;
func region_get_connections_count(region: RID) -> int:
    pass;
func region_get_layers(region: RID) -> int:
    pass;
func region_set_layers(region: RID, layers: int) -> void:
    pass;
func region_set_map(region: RID, map: RID) -> void:
    pass;
func region_set_navpoly(region: RID, nav_poly: NavigationPolygon) -> void:
    pass;
func region_set_transform(region: RID, transform: Transform2D) -> void:
    pass;
