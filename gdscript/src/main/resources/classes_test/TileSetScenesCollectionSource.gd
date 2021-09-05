extends TileSetSource
class_name TileSetScenesCollectionSource



func create_scene_tile(packed_scene: PackedScene, id_override: int) -> int:
    pass;

func get_alternative_tile_id(atlas_coords: Vector2i, index: int) -> int:
    pass;

func get_alternative_tiles_count(atlas_coords: Vector2i) -> int:
    pass;

func get_next_scene_tile_id() -> int:
    pass;

func get_scene_tile_display_placeholder(id: int) -> bool:
    pass;

func get_scene_tile_id(index: int) -> int:
    pass;

func get_scene_tile_scene(id: int) -> PackedScene:
    pass;

func get_scene_tiles_count() -> int:
    pass;

func get_tile_id(index: int) -> Vector2i:
    pass;

func get_tiles_count() -> int:
    pass;

func has_alternative_tile(atlas_coords: Vector2i, alternative_tile: int) -> bool:
    pass;

func has_scene_tile_id(id: int) -> bool:
    pass;

func has_tile(atlas_coords: Vector2i) -> bool:
    pass;

func remove_scene_tile(id: int) -> void:
    pass;

func set_scene_tile_display_placeholder(id: int, display_placeholder: bool) -> void:
    pass;

func set_scene_tile_id(id: int, new_id: int) -> void:
    pass;

func set_scene_tile_scene(id: int, packed_scene: PackedScene) -> void:
    pass;

