#brief Holds a pattern to be copied from or pasted into [TileMap]s.
#desc This resource holds a set of cells to help bulk manipulations of [TileMap].
#desc A pattern always start at the [code](0,0)[/code] coordinates and cannot have cells with negative coordinates.
class_name TileMapPattern




#desc Returns the tile alternative ID of the cell at [param coords].
func get_cell_alternative_tile(coords: Vector2i) -> int:
	pass;

#desc Returns the tile atlas coordinates ID of the cell at [param coords].
func get_cell_atlas_coords(coords: Vector2i) -> Vector2i:
	pass;

#desc Returns the tile source ID of the cell at [param coords].
func get_cell_source_id(coords: Vector2i) -> int:
	pass;

#desc Returns the size, in cells, of the pattern.
func get_size() -> Vector2i:
	pass;

#desc Returns the list of used cell coordinates in the pattern.
func get_used_cells() -> Vector2i[]:
	pass;

#desc Returns whether the pattern has a tile at the given coordinates.
func has_cell(coords: Vector2i) -> bool:
	pass;

#desc Returns whether the pattern is empty or not.
func is_empty() -> bool:
	pass;

#desc Remove the cell at the given coordinates.
func remove_cell(coords: Vector2i, update_size: bool) -> void:
	pass;

#desc Sets the tile indentifiers for the cell at coordinates [param coords]. See [method TileMap.set_cell].
func set_cell(coords: Vector2i, source_id: int, atlas_coords: Vector2i, alternative_tile: int) -> void:
	pass;

#desc Sets the size of the pattern.
func set_size(size: Vector2i) -> void:
	pass;


