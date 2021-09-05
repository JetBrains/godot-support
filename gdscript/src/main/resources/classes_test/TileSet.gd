extends Resource
class_name TileSet

const TILE_SHAPE_SQUARE = 0;
const TILE_SHAPE_ISOMETRIC = 1;
const TILE_SHAPE_HALF_OFFSET_SQUARE = 2;
const TILE_SHAPE_HEXAGON = 3;
const TILE_LAYOUT_STACKED = 0;
const TILE_LAYOUT_STACKED_OFFSET = 1;
const TILE_LAYOUT_STAIRS_RIGHT = 2;
const TILE_LAYOUT_STAIRS_DOWN = 3;
const TILE_LAYOUT_DIAMOND_RIGHT = 4;
const TILE_LAYOUT_DIAMOND_DOWN = 5;
const TILE_OFFSET_AXIS_HORIZONTAL = 0;
const TILE_OFFSET_AXIS_VERTICAL = 1;
const CELL_NEIGHBOR_RIGHT_SIDE = 0;
const CELL_NEIGHBOR_RIGHT_CORNER = 1;
const CELL_NEIGHBOR_BOTTOM_RIGHT_SIDE = 2;
const CELL_NEIGHBOR_BOTTOM_RIGHT_CORNER = 3;
const CELL_NEIGHBOR_BOTTOM_SIDE = 4;
const CELL_NEIGHBOR_BOTTOM_CORNER = 5;
const CELL_NEIGHBOR_BOTTOM_LEFT_SIDE = 6;
const CELL_NEIGHBOR_BOTTOM_LEFT_CORNER = 7;
const CELL_NEIGHBOR_LEFT_SIDE = 8;
const CELL_NEIGHBOR_LEFT_CORNER = 9;
const CELL_NEIGHBOR_TOP_LEFT_SIDE = 10;
const CELL_NEIGHBOR_TOP_LEFT_CORNER = 11;
const CELL_NEIGHBOR_TOP_SIDE = 12;
const CELL_NEIGHBOR_TOP_CORNER = 13;
const CELL_NEIGHBOR_TOP_RIGHT_SIDE = 14;
const CELL_NEIGHBOR_TOP_RIGHT_CORNER = 15;
const TERRAIN_MODE_MATCH_CORNERS_AND_SIDES = 0;
const TERRAIN_MODE_MATCH_CORNERS = 1;
const TERRAIN_MODE_MATCH_SIDES = 2;

var custom_data_layers_count: int setget set_custom_data_layers_count, get_custom_data_layers_count;
var navigation_layers_count: int setget set_navigation_layers_count, get_navigation_layers_count;
var occlusion_layers_count: int setget set_occlusion_layers_count, get_occlusion_layers_count;
var physics_layers_count: int setget set_physics_layers_count, get_physics_layers_count;
var terrains_sets_count: int setget set_terrain_sets_count, get_terrain_sets_count;
var tile_layout: int setget set_tile_layout, get_tile_layout;
var tile_offset_axis: int setget set_tile_offset_axis, get_tile_offset_axis;
var tile_shape: int setget set_tile_shape, get_tile_shape;
var tile_size: Vector2i setget set_tile_size, get_tile_size;
var uv_clipping: bool setget set_uv_clipping, is_uv_clipping;
var y_sorting: bool setget set_y_sorting, is_y_sorting;

func add_source(atlas_source_id_override: TileSetSource, arg1: int) -> int:
    pass;

func get_navigation_layer_layers(layer_index: int) -> int:
    pass;

func get_next_source_id() -> int:
    pass;

func get_occlusion_layer_light_mask(arg0: int) -> int:
    pass;

func get_occlusion_layer_sdf_collision(arg0: int) -> bool:
    pass;

func get_physics_layer_collision_layer(layer_index: int) -> int:
    pass;

func get_physics_layer_collision_mask(layer_index: int) -> int:
    pass;

func get_physics_layer_physics_material(layer_index: int) -> PhysicsMaterial:
    pass;

func get_source(index: int) -> TileSetSource:
    pass;

func get_source_count() -> int:
    pass;

func get_source_id(index: int) -> int:
    pass;

func get_terrain_color(terrain_set: int, terrain_index: int) -> Color:
    pass;

func get_terrain_name(terrain_set: int, terrain_index: int) -> String:
    pass;

func get_terrain_set_mode(terrain_set: int) -> int:
    pass;

func get_terrains_count(terrain_set: int) -> int:
    pass;

func has_source(index: int) -> bool:
    pass;

func remove_source(source_id: int) -> void:
    pass;

func set_navigation_layer_layers(layer_index: int, layers: int) -> void:
    pass;

func set_occlusion_layer_light_mask(layer_index: int, light_mask: int) -> void:
    pass;

func set_occlusion_layer_sdf_collision(layer_index: int, sdf_collision: int) -> void:
    pass;

func set_physics_layer_collision_layer(layer_index: int, layer: int) -> void:
    pass;

func set_physics_layer_collision_mask(layer_index: int, mask: int) -> void:
    pass;

func set_physics_layer_physics_material(layer_index: int, physics_material: PhysicsMaterial) -> void:
    pass;

func set_source_id(source_id: int, arg1: int) -> void:
    pass;

func set_terrain_color(terrain_set: int, terrain_index: int, color: Color) -> void:
    pass;

func set_terrain_name(terrain_set: int, terrain_index: int, name: String) -> void:
    pass;

func set_terrain_set_mode(terrain_set: int, mode: int) -> void:
    pass;

func set_terrains_count(terrain_set: int, terrains_count: int) -> void:
    pass;

