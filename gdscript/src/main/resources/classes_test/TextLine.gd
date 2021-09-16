extends RefCounted
class_name TextLine

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
func draw(canvas: RID, pos: Vector2, color: Color) -> void:
    pass;
func draw_outline(canvas: RID, pos: Vector2, outline_size: int, color: Color) -> void:
    pass;
func get_line_ascent() -> float:
    pass;
func get_line_descent() -> float:
    pass;
func get_line_underline_position() -> float:
    pass;
func get_line_underline_thickness() -> float:
    pass;
func get_line_width() -> float:
    pass;
func get_object_rect(key: Variant) -> Rect2:
    pass;
func get_objects() -> Array:
    pass;
func get_rid() -> RID:
    pass;
func get_size() -> Vector2:
    pass;
func hit_test(coords: float) -> int:
    pass;
func resize_object(key: Variant, size: Vector2, inline_align: int) -> bool:
    pass;
func set_bidi_override(override: Array) -> void:
    pass;
func tab_align(tab_stops: PackedFloat32Array) -> void:
    pass;
