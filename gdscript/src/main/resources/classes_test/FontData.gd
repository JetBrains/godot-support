extends Resource
class_name FontData
const SPACING_GLYPH = 0;
const SPACING_SPACE = 1;

var antialiased: bool;
var data_path: String;
var distance_field_hint: bool;
var extra_spacing_glyph: int;
var extra_spacing_space: int;
var force_autohinter: bool;
var hinting: int;

func bitmap_add_char(char: int, texture_idx: int, rect: Rect2, align: Vector2, advance: float) -> void:
    pass;
func bitmap_add_kerning_pair(A: int, B: int, kerning: int) -> void:
    pass;
func bitmap_add_texture(texture: Texture) -> void:
    pass;
func draw_glyph(canvas: RID, size: int, pos: Vector2, index: int, color: Color) -> Vector2:
    pass;
func draw_glyph_outline(canvas: RID, size: int, outline_size: int, pos: Vector2, index: int, color: Color) -> Vector2:
    pass;
func get_ascent(size: int) -> float:
    pass;
func get_base_size() -> float:
    pass;
func get_descent(size: int) -> float:
    pass;
func get_glyph_advance(index: int, size: int) -> Vector2:
    pass;
func get_glyph_index(char: int, variation_selector: int) -> int:
    pass;
func get_glyph_kerning(index_a: int, index_b: int, size: int) -> Vector2:
    pass;
func get_height(size: int) -> float:
    pass;
func get_language_support_override(language: String) -> bool:
    pass;
func get_language_support_overrides() -> PackedStringArray:
    pass;
func get_script_support_override(script: String) -> bool:
    pass;
func get_script_support_overrides() -> PackedStringArray:
    pass;
func get_spacing(type: int) -> int:
    pass;
func get_supported_chars() -> String:
    pass;
func get_underline_position(size: int) -> float:
    pass;
func get_underline_thickness(size: int) -> float:
    pass;
func get_variation(tag: String) -> float:
    pass;
func get_variation_list() -> Dictionary:
    pass;
func has_char(char: int) -> bool:
    pass;
func has_outline() -> bool:
    pass;
func is_language_supported(language: String) -> bool:
    pass;
func is_script_supported(script: String) -> bool:
    pass;
func load_memory(data: PackedByteArray, type: String, base_size: int) -> void:
    pass;
func load_resource(filename: String, base_size: int) -> void:
    pass;
func new_bitmap(height: float, ascent: float, base_size: int) -> void:
    pass;
func remove_language_support_override(language: String) -> void:
    pass;
func remove_script_support_override(script: String) -> void:
    pass;
func set_language_support_override(language: String, supported: bool) -> void:
    pass;
func set_script_support_override(script: String, supported: bool) -> void:
    pass;
func set_spacing(type: int, value: int) -> void:
    pass;
func set_variation(tag: String, value: float) -> void:
    pass;
