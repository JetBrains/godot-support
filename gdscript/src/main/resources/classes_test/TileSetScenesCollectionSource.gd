#brief Exposes a set of scenes as tiles for a [TileSet] resource.
#desc When placed on a [TileMap], tiles from [TileSetScenesCollectionSource] will automatically instantiate an associated scene at the cell's position in the TileMap.
#desc Scenes are instantiated as children of the [TileMap] when it enters the tree. If you add/remove a scene tile in the [TileMap] that is already inside the tree, the [TileMap] will automatically instantiate/free the scene accordingly.
class_name TileSetScenesCollectionSource




#desc Creates a scene-based tile out of the given scene.
#desc Returns a newly generated unique ID.
func create_scene_tile(packed_scene: PackedScene, id_override: int) -> int:
	pass;

#desc Returns the scene ID a following call to [method create_scene_tile] would return.
func get_next_scene_tile_id() -> int:
	pass;

#desc Returns whether the scene tile with [param id] displays a placeholder in the editor.
func get_scene_tile_display_placeholder() -> bool:
	pass;

#desc Returns the scene tile ID of the scene tile at [param index].
func get_scene_tile_id() -> int:
	pass;

#desc Returns the [PackedScene] resource of scene tile with [param id].
func get_scene_tile_scene() -> PackedScene:
	pass;

#desc Returns the number or scene tiles this TileSet source has.
func get_scene_tiles_count() -> int:
	pass;

#desc Returns whether this TileSet source has a scene tile with [param id].
func has_scene_tile_id() -> bool:
	pass;

#desc Remove the scene tile with [param id].
func remove_scene_tile() -> void:
	pass;

#desc Sets whether or not the scene tile with [param id] should display a placeholder in the editor. This might be useful for scenes that are not visible.
func set_scene_tile_display_placeholder(id: int, display_placeholder: bool) -> void:
	pass;

#desc Changes a scene tile's ID from [param id] to [param new_id]. This will fail if there is already a tile with a ID equal to [param new_id].
func set_scene_tile_id(id: int, new_id: int) -> void:
	pass;

#desc Assigns a [PackedScene] resource to the scene tile with [param id]. This will fail if the scene does not extend CanvasItem, as positioning properties are needed to place the scene on the TileMap.
func set_scene_tile_scene(id: int, packed_scene: PackedScene) -> void:
	pass;


