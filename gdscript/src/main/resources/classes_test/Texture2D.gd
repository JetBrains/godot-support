#brief Texture for 2D and 3D.
#desc A texture works by registering an image in the video hardware, which then can be used in 3D models or 2D [Sprite2D] or GUI [Control].
#desc Textures are often created by loading them from a file. See [method @GDScript.load].
#desc [Texture2D] is a base for other resources. It cannot be used directly.
#desc [b]Note:[/b] The maximum texture size is 16384×16384 pixels due to graphics hardware limitations. Larger textures may fail to import.
class_name Texture2D




virtual const func _draw(to_canvas_item: RID, pos: Vector2, modulate: Color, transpose: bool) -> void:
	pass;

virtual const func _draw_rect(to_canvas_item: RID, rect: Rect2, tile: bool, modulate: Color, transpose: bool) -> void:
	pass;

virtual const func _draw_rect_region(tp_canvas_item: RID, rect: Rect2, src_rect: Rect2, modulate: Color, transpose: bool, clip_uv: bool) -> void:
	pass;

virtual const func _get_height() -> int:
	pass;

virtual const func _get_width() -> int:
	pass;

virtual const func _has_alpha() -> bool:
	pass;

virtual const func _is_pixel_opaque(x: int, y: int) -> bool:
	pass;

#desc Draws the texture using a [CanvasItem] with the [RenderingServer] API at the specified [param position].
func draw(canvas_item: RID, position: Vector2, modulate: Color, transpose: bool) -> void:
	pass;

#desc Draws the texture using a [CanvasItem] with the [RenderingServer] API.
func draw_rect(canvas_item: RID, rect: Rect2, tile: bool, modulate: Color, transpose: bool) -> void:
	pass;

#desc Draws a part of the texture using a [CanvasItem] with the [RenderingServer] API.
func draw_rect_region(canvas_item: RID, rect: Rect2, src_rect: Rect2, modulate: Color, transpose: bool, clip_uv: bool) -> void:
	pass;

#desc Returns the texture height.
func get_height() -> int:
	pass;

#desc Returns an [Image] that is a copy of data from this [Texture2D] (a new [Image] is created each time). [Image]s can be accessed and manipulated directly.
#desc [b]Note:[/b] This will fetch the texture data from the GPU, which might cause performance problems when overused.
func get_image() -> Image:
	pass;

#desc Returns the texture size.
func get_size() -> Vector2:
	pass;

#desc Returns the texture width.
func get_width() -> int:
	pass;

#desc Returns [code]true[/code] if this [Texture2D] has an alpha channel.
func has_alpha() -> bool:
	pass;


