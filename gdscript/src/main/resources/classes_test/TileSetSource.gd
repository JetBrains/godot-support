#brief Exposes a set of tiles for a [TileSet] resource.
#desc Exposes a set of tiles for a [TileSet] resource.
#desc Tiles in a source are indexed with two IDs, coordinates ID (of type Vector2i) and an alternative ID (of type int), named according to their use in the [TileSetAtlasSource] class.
#desc Depending on the TileSet source type, those IDs might have restrictions on their values, this is why the base [TileSetSource] class only exposes getters for them.
#desc 
#desc You can iterate over all tiles exposed by a TileSetSource by first iterating over coordinates IDs using [method get_tiles_count] and [method get_tile_id], then over alternative IDs using [method get_alternative_tiles_count] and [method get_alternative_tile_id].
class_name TileSetSource




#desc Returns the alternative ID for the tile with coordinates ID [param atlas_coords] at index [param index].
func get_alternative_tile_id(atlas_coords: Vector2i, index: int) -> int:
	pass;

#desc Returns the number of alternatives tiles for the coordinates ID [param atlas_coords].
#desc For [TileSetAtlasSource], this always return at least 1, as the base tile with ID 0 is always part of the alternatives list.
#desc Returns -1 if there is not tile at the given coords.
func get_alternative_tiles_count(atlas_coords: Vector2i) -> int:
	pass;

#desc Returns the tile coordinates ID of the tile with index [param index].
func get_tile_id(index: int) -> Vector2i:
	pass;

#desc Returns how many tiles this atlas source defines (not including alternative tiles).
func get_tiles_count() -> int:
	pass;

#desc Returns if the base tile at coordinates [param atlas_coords] has an alternative with ID [param alternative_tile].
func has_alternative_tile(atlas_coords: Vector2i, alternative_tile: int) -> bool:
	pass;

#desc Returns if this atlas has a tile with coordinates ID [param atlas_coords].
func has_tile(atlas_coords: Vector2i) -> bool:
	pass;


