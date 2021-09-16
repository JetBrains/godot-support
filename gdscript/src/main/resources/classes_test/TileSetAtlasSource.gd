extends TileSetSource
class_name TileSetAtlasSource

var margins: Vector2i;
var separation: Vector2i;
var texture: Texture2D;
var tile_size: Vector2i;

func can_move_tile_in_atlas(atlas_coords: Vector2i, new_atlas_coords: Vector2i, new_size: Vector2i) -> bool:
    pass;
func clear_tiles_outside_texture() -> void:
    pass;
func create_alternative_tile(atlas_coords: Vector2i, alternative_id_override: int) -> int:
    pass;
func create_tile(atlas_coords: Vector2i, size: Vector2i) -> void:
    pass;
func get_alternative_tile_id(atlas_coords: Vector2i, index: int) -> int:
    pass;
func get_alternative_tiles_count(atlas_coords: Vector2i) -> int:
    pass;
func get_atlas_grid_size() -> Vector2i:
    pass;
func get_next_alternative_tile_id(atlas_coords: Vector2i) -> int:
    pass;
func get_tile_at_coords(atlas_coords: Vector2i) -> Vector2i:
    pass;
func get_tile_data(atlas_coords: Vector2i, index: int) -> Object:
    pass;
func get_tile_id(index: int) -> Vector2i:
    pass;
func get_tile_size_in_atlas(atlas_coords: Vector2i) -> Vector2i:
    pass;
func get_tile_texture_region(atlas_coords: Vector2i) -> Rect2i:
    pass;
func get_tiles_count() -> int:
    pass;
func has_alternative_tile(atlas_coords: Vector2i, alternative_tile: int) -> bool:
    pass;
func has_tile(atlas_coords: Vector2i) -> bool:
    pass;
func has_tiles_outside_texture() -> bool:
    pass;
func move_tile_in_atlas(atlas_coords: Vector2i, new_atlas_coords: Vector2i, new_size: Vector2i) -> void:
    pass;
func remove_alternative_tile(atlas_coords: Vector2i, alternative_tile: int) -> void:
    pass;
func remove_tile(atlas_coords: Vector2i) -> void:
    pass;
func set_alternative_tile_id(atlas_coords: Vector2i, alternative_tile: int, new_id: int) -> void:
    pass;
