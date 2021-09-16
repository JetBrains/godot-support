extends Resource
class_name Font
const SPACING_TOP = 0;
const SPACING_BOTTOM = 1;

var extra_spacing_bottom: int;
var extra_spacing_top: int;

func add_data(data: FontData) -> void:
    pass;
func draw_char(canvas_item: RID, pos: Vector2, char: int, next: int, size: int, modulate: Color, outline_size: int, outline_modulate: Color) -> float:
    pass;
func draw_multiline_string(canvas_item: RID, pos: Vector2, text: String, align: int, width: float, max_lines: int, size: int, modulate: Color, outline_size: int, outline_modulate: Color, flags: int) -> void:
    pass;
func draw_string(canvas_item: RID, pos: Vector2, text: String, align: int, width: float, size: int, modulate: Color, outline_size: int, outline_modulate: Color, flags: int) -> void:
    pass;
func get_ascent(size: int) -> float:
    pass;
func get_char_size(char: int, next: int, size: int) -> Vector2:
    pass;
func get_data(idx: int) -> FontData:
    pass;
func get_data_count() -> int:
    pass;
func get_descent(size: int) -> float:
    pass;
func get_height(size: int) -> float:
    pass;
func get_multiline_string_size(text: String, width: float, size: int, flags: int) -> Vector2:
    pass;
func get_spacing(type: int) -> int:
    pass;
func get_string_size(text: String, size: int) -> Vector2:
    pass;
func get_supported_chars() -> String:
    pass;
func get_underline_position(size: int) -> float:
    pass;
func get_underline_thickness(size: int) -> float:
    pass;
func has_char(char: int) -> bool:
    pass;
func remove_data(idx: int) -> void:
    pass;
func set_data(idx: int, data: FontData) -> void:
    pass;
func set_spacing(type: int, value: int) -> void:
    pass;
func update_changes() -> void:
    pass;
