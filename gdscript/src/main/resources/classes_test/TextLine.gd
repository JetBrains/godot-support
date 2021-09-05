extends RefCounted
class_name TextLine


var align: int setget set_align, get_align;
var direction: int setget set_direction, get_direction;
var flags: int setget set_flags, get_flags;
var orientation: int setget set_orientation, get_orientation;
var preserve_control: bool setget set_preserve_control, get_preserve_control;
var preserve_invalid: bool setget set_preserve_invalid, get_preserve_invalid;
var width: float setget set_width, get_width;

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

