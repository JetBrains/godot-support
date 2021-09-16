extends RefCounted
class_name TextParagraph

var align: int;
var direction: int;
var flags: int;
var orientation: int;
var preserve_control: bool;
var preserve_invalid: bool;
var width: float;

func add_object(key: Variant, size: Vector2, inline_align: int, length: int) -> bool:
    pass;
func add_string(text: String, fonts: Font, size: int, opentype_features: Dictionary, language: String) -> bool:
    pass;
func clear() -> void:
    pass;
func clear_dropcap() -> void:
    pass;
func draw(canvas: RID, pos: Vector2, color: Color, dc_color: Color) -> void:
    pass;
func draw_dropcap(canvas: RID, pos: Vector2, color: Color) -> void:
    pass;
func draw_dropcap_outline(canvas: RID, pos: Vector2, outline_size: int, color: Color) -> void:
    pass;
func draw_line(canvas: RID, pos: Vector2, line: int, color: Color) -> void:
    pass;
func draw_line_outline(canvas: RID, pos: Vector2, line: int, outline_size: int, color: Color) -> void:
    pass;
func draw_outline(canvas: RID, pos: Vector2, outline_size: int, color: Color, dc_color: Color) -> void:
    pass;
func get_dropcap_lines() -> int:
    pass;
func get_dropcap_rid() -> RID:
    pass;
func get_dropcap_size() -> Vector2:
    pass;
func get_line_ascent(line: int) -> float:
    pass;
func get_line_count() -> int:
    pass;
func get_line_descent(line: int) -> float:
    pass;
func get_line_object_rect(line: int, key: Variant) -> Rect2:
    pass;
func get_line_objects(line: int) -> Array:
    pass;
func get_line_range(line: int) -> Vector2i:
    pass;
func get_line_rid(line: int) -> RID:
    pass;
func get_line_size(line: int) -> Vector2:
    pass;
func get_line_underline_position(line: int) -> float:
    pass;
func get_line_underline_thickness(line: int) -> float:
    pass;
func get_line_width(line: int) -> float:
    pass;
func get_non_wraped_size() -> Vector2:
    pass;
func get_rid() -> RID:
    pass;
func get_size() -> Vector2:
    pass;
func get_spacing_bottom() -> int:
    pass;
func get_spacing_top() -> int:
    pass;
func hit_test(coords: Vector2) -> int:
    pass;
func resize_object(key: Variant, size: Vector2, inline_align: int) -> bool:
    pass;
func set_bidi_override(override: Array) -> void:
    pass;
func set_dropcap(text: String, fonts: Font, size: int, dropcap_margins: Rect2, opentype_features: Dictionary, language: String) -> bool:
    pass;
func tab_align(tab_stops: PackedFloat32Array) -> void:
    pass;
