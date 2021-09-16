extends Texture
class_name Texture2D


func draw(canvas_item: RID, position: Vector2, modulate: Color, transpose: bool) -> void:
    pass;
func draw_rect(canvas_item: RID, rect: Rect2, tile: bool, modulate: Color, transpose: bool) -> void:
    pass;
func draw_rect_region(canvas_item: RID, rect: Rect2, src_rect: Rect2, modulate: Color, transpose: bool, clip_uv: bool) -> void:
    pass;
func get_height() -> int:
    pass;
func get_image() -> Image:
    pass;
func get_size() -> Vector2:
    pass;
func get_width() -> int:
    pass;
func has_alpha() -> bool:
    pass;
