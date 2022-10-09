extends Object
class_name TileData


var flip_h: bool;

var flip_v: bool;

#desc The [Material] to use for this [TileData]. This can be a [CanvasItemMaterial] to use the default shader, or a [ShaderMaterial] to use a custom shader.
var material: Material;

var modulate: Color;

var probability: float;

var terrain: int;

var terrain_set: int;

var texture_offset: Vector2i;

var transpose: bool;

var y_sort_origin: int;

var z_index: int;



#desc Adds a collision polygon to the tile on the given TileSet physics layer.
func add_collision_polygon(layer_id: int) -> void:
	pass;

#desc Returns the one-way margin (for one-way platforms) of the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func get_collision_polygon_one_way_margin(layer_id: int, polygon_index: int) -> float:
	pass;

#desc Returns the points of the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func get_collision_polygon_points(layer_id: int, polygon_index: int) -> PackedVector2Array:
	pass;

#desc Returns how many polygons the tile has for TileSet physics layer with index [param layer_id].
func get_collision_polygons_count(layer_id: int) -> int:
	pass;

#desc Returns the constant angular velocity applied to objects colliding with this tile.
func get_constant_angular_velocity(layer_id: int) -> float:
	pass;

#desc Returns the constant linear velocity applied to objects colliding with this tile.
func get_constant_linear_velocity(layer_id: int) -> Vector2:
	pass;

#desc Returns the custom data value for custom data layer named [param layer_name].
func get_custom_data(layer_name: String) -> Variant:
	pass;

#desc Returns the custom data value for custom data layer with index [param layer_id].
func get_custom_data_by_layer_id(layer_id: int) -> Variant:
	pass;

#desc Returns the navigation polygon of the tile for the TileSet navigation layer with index [param layer_id].
func get_navigation_polygon(layer_id: int) -> NavigationPolygon:
	pass;

#desc Returns the occluder polygon of the tile for the TileSet occlusion layer with index [param layer_id].
func get_occluder(layer_id: int) -> OccluderPolygon2D:
	pass;

#desc Returns the tile's terrain bit for the given [param peering_bit] direction.
func get_terrain_peering_bit(peering_bit: int) -> int:
	pass;

#desc Returns whether one-way collisions are enabled for the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func is_collision_polygon_one_way(layer_id: int, polygon_index: int) -> bool:
	pass;

#desc Removes the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func remove_collision_polygon(layer_id: int, polygon_index: int) -> void:
	pass;

#desc Enables/disables one-way collisions on the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func set_collision_polygon_one_way(layer_id: int, polygon_index: int, one_way: bool) -> void:
	pass;

#desc Enables/disables one-way collisions on the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func set_collision_polygon_one_way_margin(layer_id: int, polygon_index: int, one_way_margin: float) -> void:
	pass;

#desc Sets the points of the polygon at index [param polygon_index] for TileSet physics layer with index [param layer_id].
func set_collision_polygon_points(layer_id: int, polygon_index: int, polygon: PackedVector2Array) -> void:
	pass;

#desc Sets the polygons count for TileSet physics layer with index [param layer_id].
func set_collision_polygons_count(layer_id: int, polygons_count: int) -> void:
	pass;

#desc Sets the constant angular velocity. This does not rotate the tile. This angular velocity is applied to objects colliding with this tile.
func set_constant_angular_velocity(layer_id: int, velocity: float) -> void:
	pass;

#desc Sets the constant linear velocity. This does not move the tile. This linear velocity is applied to objects colliding with this tile. This is useful to create conveyor belts.
func set_constant_linear_velocity(layer_id: int, velocity: Vector2) -> void:
	pass;

#desc Sets the tile's custom data value for the TileSet custom data layer with name [param layer_name].
func set_custom_data(layer_name: String, value: Variant) -> void:
	pass;

#desc Sets the tile's custom data value for the TileSet custom data layer with index [param layer_id].
func set_custom_data_by_layer_id(layer_id: int, value: Variant) -> void:
	pass;

#desc Sets the navigation polygon for the TileSet navigation layer with index [param layer_id].
func set_navigation_polygon(layer_id: int, navigation_polygon: NavigationPolygon) -> void:
	pass;

#desc Sets the occluder for the TileSet occlusion layer with index [param layer_id].
func set_occluder(layer_id: int, occluder_polygon: OccluderPolygon2D) -> void:
	pass;

#desc Sets the tile's terrain bit for the given [param peering_bit] direction.
func set_terrain_peering_bit(peering_bit: int, terrain: int) -> void:
	pass;


