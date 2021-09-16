extends Node2D
class_name TileMap
const VISIBILITY_MODE_DEFAULT = 0;
const VISIBILITY_MODE_FORCE_HIDE = 2;
const VISIBILITY_MODE_FORCE_SHOW = 1;

var cell_quadrant_size: int;
var show_collision: int;
var show_navigation: int;
var tile_set: TileSet;

func clear() -> void:
    pass;
func fix_invalid_tiles() -> void:
    pass;
func get_cell_alternative_tile(coords: Vector2i) -> int:
    pass;
func get_cell_atlas_coords(coords: Vector2i) -> Vector2i:
    pass;
func get_cell_source_id(coords: Vector2i) -> int:
    pass;
func get_neighbor_cell(coords: Vector2i, neighbor: int) -> Vector2i:
    pass;
func get_surrounding_tiles(coords: Vector2i) -> Array[Vector2i]:
    pass;
func get_used_cells() -> Array[Vector2i]:
    pass;
func get_used_rect() -> Rect2:
    pass;
func map_to_world(map_position: Vector2i) -> Vector2:
    pass;
func set_cell(coords: Vector2i, source_id: int, atlas_coords: Vector2i, alternative_tile: int) -> void:
    pass;
func update_dirty_quadrants() -> void:
    pass;
func world_to_map(world_position: Vector2) -> Vector2i:
    pass;
