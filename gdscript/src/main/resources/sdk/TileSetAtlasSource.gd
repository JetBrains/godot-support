extends TileSetSource
#brief Exposes a 2D atlas texture as a set of tiles for a [TileSet] resource.
#desc An atlas is a grid of tiles laid out on a texture. Each tile in the grid must be exposed using [method create_tile]. Those tiles are then indexed using their coordinates in the grid.
#desc Each tile can also have a size in the grid coordinates, making it more or less cells in the atlas.
#desc 
#desc Alternatives version of a tile can be created using [method create_alternative_tile], which are then indexed using an alternative ID. The main tile (the one in the grid), is accessed with an alternative ID equal to 0.
#desc 
#desc Each tile alternate has a set of properties that is defined by the source's [TileSet] layers. Those properties are stored in a TileData object that can be accessed and modified using [method get_tile_data].
#desc As TileData properties are stored directly in the TileSetAtlasSource resource, their properties might also be set using [code]TileSetAtlasSource.set("<coords_x>:<coords_y>/<alternative_id>/<tile_data_property>")[/code].
class_name TileSetAtlasSource


#desc Margins, in pixels, to offset the origin of the grid in the texture.
var margins: Vector2i;

#desc Separation, in pixels, between each tile texture region of the grid.
var separation: Vector2i;

#desc The atlas texture.
var texture: Texture2D;

#desc The base tile size in the texture (in pixel). This size must be bigger than the TileSet's [code]tile_size[/code] value.
var texture_region_size: Vector2i;

#desc If [code]true[/code], generates an internal texture with an additional one pixel padding around each tile. Texture padding avoids a common artifact where lines appear between tiles.
#desc Disabling this setting might lead a small performance improvement, as generating the internal texture requires both memory and processing time when the TileSetAtlasSource resource is modified.
var use_texture_padding: bool;



#desc Creates an alternative tile for the tile at coordinates [param atlas_coords]. If [param alternative_id_override] is -1, give it an automatically generated unique ID, or assigns it the given ID otherwise.
#desc Returns the new alternative identifier, or -1 if the alternative could not be created with a provided [param alternative_id_override].
func create_alternative_tile(atlas_coords: Vector2i, alternative_id_override: int) -> int:
	pass;

#desc Creates a new tile at coordinates [param atlas_coords] with the given [param size].
func create_tile(atlas_coords: Vector2i, size: Vector2i) -> void:
	pass;

#desc Returns the atlas grid size, which depends on how many tiles can fit in the texture. It thus depends on the Texture's size, the atlas [code]margins[/code] the tiles' [code]texture_region_size[/code].
func get_atlas_grid_size() -> Vector2i:
	pass;

#desc Returns the alternative ID a following call to [method create_alternative_tile] would return.
func get_next_alternative_tile_id(atlas_coords: Vector2i) -> int:
	pass;

#desc If [member use_texture_padding] is [code]false[/code], returns [member texture]. Otherwise, returns and internal [ImageTexture] created that includes the padding.
func get_runtime_texture() -> Texture2D:
	pass;

#desc Returns the region of the tile at coordinates [param atlas_coords] for the given [param frame] inside the texture returned by [method get_runtime_texture].
#desc [b]Note:[/b] If [member use_texture_padding] is [code]false[/code], returns the same as [method get_tile_texture_region].
func get_runtime_tile_texture_region(atlas_coords: Vector2i, frame: int) -> Rect2i:
	pass;

#desc Returns how many columns the tile at [param atlas_coords] has in its animation layout.
func get_tile_animation_columns(atlas_coords: Vector2i) -> int:
	pass;

#desc Returns the animation frame duration of frame [param frame_index] for the tile at coordinates [param atlas_coords].
func get_tile_animation_frame_duration(atlas_coords: Vector2i, frame_index: int) -> float:
	pass;

#desc Returns how many animation frames has the tile at coordinates [param atlas_coords].
func get_tile_animation_frames_count(atlas_coords: Vector2i) -> int:
	pass;

#desc Returns the separation (as in the atlas grid) between each frame of an animated tile at coordinates [param atlas_coords].
func get_tile_animation_separation(atlas_coords: Vector2i) -> Vector2i:
	pass;

#desc Returns the animation speed of the tile at coordinates [param atlas_coords].
func get_tile_animation_speed(atlas_coords: Vector2i) -> float:
	pass;

#desc Returns the sum of the sum of the frame durations of the tile at coordinates [param atlas_coords]. This value needs to be divided by the animation speed to get the actual animation loop duration.
func get_tile_animation_total_duration(atlas_coords: Vector2i) -> float:
	pass;

#desc If there is a tile covering the [param atlas_coords] coordinates, returns the top-left coordinates of the tile (thus its coordinate ID). Returns [code]Vector2i(-1, -1)[/code] otherwise.
func get_tile_at_coords(atlas_coords: Vector2i) -> Vector2i:
	pass;

#desc Returns the [TileData] object for the given atlas coordinates and alternative ID.
func get_tile_data(atlas_coords: Vector2i, alternative_tile: int) -> TileData:
	pass;

#desc Returns the size of the tile (in the grid coordinates system) at coordinates [param atlas_coords].
func get_tile_size_in_atlas(atlas_coords: Vector2i) -> Vector2i:
	pass;

#desc Returns a tile's texture region in the atlas texture. For animated tiles, a [param frame] argument might be provided for the different frames of the animation.
func get_tile_texture_region(atlas_coords: Vector2i, frame: int) -> Rect2i:
	pass;

#desc Returns an array of tiles coordinates ID that will be automatically removed when modifying one or several of those properties: [param texture], [param margins], [param separation] or [param texture_region_size]. This can be used to undo changes that would have caused tiles data loss.
func get_tiles_to_be_removed_on_change(texture: Texture2D, margins: Vector2i, separation: Vector2i, texture_region_size: Vector2i) -> PackedVector2Array:
	pass;

#desc Returns whether there is enough room in an atlas to create/modify a tile with the given properties. If [param ignored_tile] is provided, act as is the given tile was not present in the atlas. This may be used when you want to modify a tile's properties.
func has_room_for_tile(atlas_coords: Vector2i, size: Vector2i, animation_columns: int, animation_separation: Vector2i, frames_count: int, ignored_tile: Vector2i) -> bool:
	pass;

#desc Move the tile and its alternatives at the [param atlas_coords] coordinates to the [param new_atlas_coords] coordinates with the [param new_size] size. This functions will fail if a tile is already present in the given area.
#desc If [param new_atlas_coords] is [code]Vector2i(-1, -1)[/code], keeps the tile's coordinates. If [param new_size] is [code]Vector2i(-1, -1)[/code], keeps the tile's size.
#desc To avoid an error, first check if a move is possible using [method has_room_for_tile].
func move_tile_in_atlas(atlas_coords: Vector2i, new_atlas_coords: Vector2i, new_size: Vector2i) -> void:
	pass;

#desc Remove a tile's alternative with alternative ID [param alternative_tile].
#desc Calling this function with [param alternative_tile] equals to 0 will fail, as the base tile alternative cannot be removed.
func remove_alternative_tile(atlas_coords: Vector2i, alternative_tile: int) -> void:
	pass;

#desc Remove a tile and its alternative at coordinates [param atlas_coords].
func remove_tile(atlas_coords: Vector2i) -> void:
	pass;

#desc Change a tile's alternative ID from [param alternative_tile] to [param new_id].
#desc Calling this function with [param new_id] of 0 will fail, as the base tile alternative cannot be moved.
func set_alternative_tile_id(atlas_coords: Vector2i, alternative_tile: int, new_id: int) -> void:
	pass;

#desc Sets the number of columns in the animation layout of the tile at coordinates [param atlas_coords]. If set to 0, then the different frames of the animation are laid out as a single horizontal line in the atlas.
func set_tile_animation_columns(atlas_coords: Vector2i, frame_columns: int) -> void:
	pass;

#desc Sets the animation frame [param duration] of frame [param frame_index] for the tile at coordinates [param atlas_coords].
func set_tile_animation_frame_duration(atlas_coords: Vector2i, frame_index: int, duration: float) -> void:
	pass;

#desc Sets how many animation frames the tile at coordinates [param atlas_coords] has.
func set_tile_animation_frames_count(atlas_coords: Vector2i, frames_count: int) -> void:
	pass;

#desc Sets the margin (in grid tiles) between each tile in the animation layout of the tile at coordinates [param atlas_coords] has.
func set_tile_animation_separation(atlas_coords: Vector2i, separation: Vector2i) -> void:
	pass;

#desc Sets the animation speed of the tile at coordinates [param atlas_coords] has.
func set_tile_animation_speed(atlas_coords: Vector2i, speed: float) -> void:
	pass;


