extends Object
class_name TextServer
const DIRECTION_AUTO = 0;
const DIRECTION_LTR = 1;
const DIRECTION_RTL = 2;
const ORIENTATION_HORIZONTAL = 0;
const ORIENTATION_VERTICAL = 1;
const JUSTIFICATION_NONE = 0;
const JUSTIFICATION_KASHIDA = 1;
const JUSTIFICATION_WORD_BOUND = 2;
const JUSTIFICATION_TRIM_EDGE_SPACES = 4;
const JUSTIFICATION_AFTER_LAST_TAB = 8;
const BREAK_NONE = 0;
const BREAK_MANDATORY = 16;
const BREAK_WORD_BOUND = 32;
const BREAK_GRAPHEME_BOUND = 64;
const OVERRUN_NO_TRIMMING = 0;
const OVERRUN_TRIM = 1;
const OVERRUN_TRIM_WORD_ONLY = 2;
const OVERRUN_ADD_ELLIPSIS = 4;
const OVERRUN_ENFORCE_ELLIPSIS = 8;
const GRAPHEME_IS_RTL = 2;
const GRAPHEME_IS_VIRTUAL = 4;
const GRAPHEME_IS_SPACE = 8;
const GRAPHEME_IS_BREAK_HARD = 16;
const GRAPHEME_IS_BREAK_SOFT = 32;
const GRAPHEME_IS_TAB = 64;
const GRAPHEME_IS_ELONGATION = 128;
const GRAPHEME_IS_PUNCTUATION = 256;
const HINTING_NONE = 0;
const HINTING_LIGHT = 1;
const HINTING_NORMAL = 2;
const FEATURE_BIDI_LAYOUT = 1;
const FEATURE_VERTICAL_LAYOUT = 2;
const FEATURE_SHAPING = 4;
const FEATURE_KASHIDA_JUSTIFICATION = 8;
const FEATURE_BREAK_ITERATORS = 16;
const FEATURE_FONT_SYSTEM = 32;
const FEATURE_FONT_VARIABLE = 64;
const FEATURE_USE_SUPPORT_DATA = 128;
const CONTOUR_CURVE_TAG_ON = 1;
const CONTOUR_CURVE_TAG_OFF_CONIC = 0;
const CONTOUR_CURVE_TAG_OFF_CUBIC = 2;


func create_font_bitmap(height: float, ascent: float, base_size: int) -> RID:
    pass;
func create_font_memory(data: PackedByteArray, type: String, base_size: int) -> RID:
    pass;
func create_font_resource(filename: String, base_size: int) -> RID:
    pass;
func create_font_system(name: String, base_size: int) -> RID:
    pass;
func create_shaped_text(direction: int, orientation: int) -> RID:
    pass;
func draw_hex_code_box(canvas: RID, size: int, pos: Vector2, index: int, color: Color) -> void:
    pass;
func font_bitmap_add_char(font: RID, char: int, texture_idx: int, rect: Rect2, align: Vector2, advance: float) -> void:
    pass;
func font_bitmap_add_kerning_pair(font: RID, A: int, B: int, kerning: int) -> void:
    pass;
func font_bitmap_add_texture(font: RID, texture: Texture) -> void:
    pass;
func font_draw_glyph(font: RID, canvas: RID, size: int, pos: Vector2, index: int, color: Color) -> Vector2:
    pass;
func font_draw_glyph_outline(font: RID, canvas: RID, size: int, outline_size: int, pos: Vector2, index: int, color: Color) -> Vector2:
    pass;
func font_get_antialiased(font: RID) -> bool:
    pass;
func font_get_ascent(font: RID, size: int) -> float:
    pass;
func font_get_base_size(font: RID) -> float:
    pass;
func font_get_descent(font: RID, size: int) -> float:
    pass;
func font_get_distance_field_hint(font: RID) -> bool:
    pass;
func font_get_feature_list(font: RID) -> Dictionary:
    pass;
func font_get_force_autohinter(font: RID) -> bool:
    pass;
func font_get_glyph_advance(font: RID, index: int, size: int) -> Vector2:
    pass;
func font_get_glyph_contours(font: RID, size: int, index: int) -> Dictionary:
    pass;
func font_get_glyph_index(font: RID, char: int, variation_selector: int) -> int:
    pass;
func font_get_glyph_kerning(font: RID, index_a: int, index_b: int, size: int) -> Vector2:
    pass;
func font_get_height(font: RID, size: int) -> float:
    pass;
func font_get_hinting(font: RID) -> int:
    pass;
func font_get_language_support_override(font: RID, language: String) -> bool:
    pass;
func font_get_language_support_overrides(font: RID) -> PackedStringArray:
    pass;
func font_get_oversampling() -> float:
    pass;
func font_get_script_support_override(font: RID, script: String) -> bool:
    pass;
func font_get_script_support_overrides(font: RID) -> PackedStringArray:
    pass;
func font_get_spacing_glyph(font: RID) -> int:
    pass;
func font_get_spacing_space(font: RID) -> int:
    pass;
func font_get_supported_chars(font: RID) -> String:
    pass;
func font_get_underline_position(font: RID, size: int) -> float:
    pass;
func font_get_underline_thickness(font: RID, size: int) -> float:
    pass;
func font_get_variation(font: RID, tag: String) -> float:
    pass;
func font_get_variation_list(font: RID) -> Dictionary:
    pass;
func font_has_char(font: RID, char: int) -> bool:
    pass;
func font_has_outline(font: RID) -> bool:
    pass;
func font_is_language_supported(font: RID, language: String) -> bool:
    pass;
func font_is_script_supported(font: RID, script: String) -> bool:
    pass;
func font_remove_language_support_override(font: RID, language: String) -> void:
    pass;
func font_remove_script_support_override(font: RID, script: String) -> void:
    pass;
func font_set_antialiased(font: RID, antialiased: bool) -> void:
    pass;
func font_set_distance_field_hint(font: RID, distance_field: bool) -> void:
    pass;
func font_set_force_autohinter(font: RID, enabeld: bool) -> void:
    pass;
func font_set_hinting(font: RID, hinting: int) -> void:
    pass;
func font_set_language_support_override(font: RID, language: String, supported: bool) -> void:
    pass;
func font_set_oversampling(oversampling: float) -> void:
    pass;
func font_set_script_support_override(font: RID, script: String, supported: bool) -> void:
    pass;
func font_set_spacing_glyph(font: RID, value: int) -> void:
    pass;
func font_set_spacing_space(font: RID, value: int) -> void:
    pass;
func font_set_variation(font: RID, tag: String, value: float) -> void:
    pass;
func format_number(number: String, language: String) -> String:
    pass;
func free_rid(rid: RID) -> void:
    pass;
func get_hex_code_box_size(size: int, index: int) -> Vector2:
    pass;
func get_name() -> String:
    pass;
func get_system_fonts() -> PackedStringArray:
    pass;
func has(rid: RID) -> bool:
    pass;
func has_feature(feature: int) -> bool:
    pass;
func is_locale_right_to_left(locale: String) -> bool:
    pass;
func load_support_data(filename: String) -> bool:
    pass;
func name_to_tag(name: String) -> int:
    pass;
func parse_number(number: String, language: String) -> String:
    pass;
func percent_sign(language: String) -> String:
    pass;
func shaped_text_add_object(shaped: RID, key: Variant, size: Vector2, inline_align: int, length: int) -> bool:
    pass;
func shaped_text_add_string(shaped: RID, text: String, fonts: Array, size: int, opentype_features: Dictionary, language: String) -> bool:
    pass;
func shaped_text_clear(rid: RID) -> void:
    pass;
func shaped_text_draw(shaped: RID, canvas: RID, pos: Vector2, clip_l: float, clip_r: float, color: Color) -> void:
    pass;
func shaped_text_draw_outline(shaped: RID, canvas: RID, pos: Vector2, clip_l: float, clip_r: float, outline_size: int, color: Color) -> void:
    pass;
func shaped_text_fit_to_width(shaped: RID, width: float, jst_flags: int) -> float:
    pass;
func shaped_text_get_ascent(shaped: RID) -> float:
    pass;
func shaped_text_get_carets(shaped: RID, position: int) -> Dictionary:
    pass;
func shaped_text_get_descent(shaped: RID) -> float:
    pass;
func shaped_text_get_direction(shaped: RID) -> int:
    pass;
func shaped_text_get_dominant_direciton_in_range(shaped: RID, start: int, end: int) -> int:
    pass;
func shaped_text_get_glyphs(shaped: RID) -> Array:
    pass;
func shaped_text_get_line_breaks(shaped: RID, width: float, start: int, break_flags: int) -> Array:
    pass;
func shaped_text_get_line_breaks_adv(shaped: RID, width: PackedFloat32Array, start: int, once: bool, break_flags: int) -> Array:
    pass;
func shaped_text_get_object_rect(shaped: RID, key: Variant) -> Rect2:
    pass;
func shaped_text_get_objects(shaped: RID) -> Array:
    pass;
func shaped_text_get_orientation(shaped: RID) -> int:
    pass;
func shaped_text_get_parent(shaped: RID) -> RID:
    pass;
func shaped_text_get_preserve_control(shaped: RID) -> bool:
    pass;
func shaped_text_get_preserve_invalid(shaped: RID) -> bool:
    pass;
func shaped_text_get_range(shaped: RID) -> Vector2i:
    pass;
func shaped_text_get_selection(shaped: RID, start: int, end: int) -> Array:
    pass;
func shaped_text_get_size(shaped: RID) -> Vector2:
    pass;
func shaped_text_get_underline_position(shaped: RID) -> float:
    pass;
func shaped_text_get_underline_thickness(shaped: RID) -> float:
    pass;
func shaped_text_get_width(shaped: RID) -> float:
    pass;
func shaped_text_get_word_breaks(shaped: RID) -> Array:
    pass;
func shaped_text_hit_test_grapheme(shaped: RID, coords: float) -> int:
    pass;
func shaped_text_hit_test_position(shaped: RID, coords: float) -> int:
    pass;
func shaped_text_is_ready(shaped: RID) -> bool:
    pass;
func shaped_text_next_grapheme_pos(shaped: RID, pos: int) -> int:
    pass;
func shaped_text_overrun_trim_to_width(shaped: RID, width: float, overrun_trim_flags: int) -> void:
    pass;
func shaped_text_prev_grapheme_pos(shaped: RID, pos: int) -> int:
    pass;
func shaped_text_resize_object(shaped: RID, key: Variant, size: Vector2, inline_align: int) -> bool:
    pass;
func shaped_text_set_bidi_override(shaped: RID, override: Array) -> void:
    pass;
func shaped_text_set_direction(shaped: RID, direction: int) -> void:
    pass;
func shaped_text_set_orientation(shaped: RID, orientation: int) -> void:
    pass;
func shaped_text_set_preserve_control(shaped: RID, enabled: bool) -> void:
    pass;
func shaped_text_set_preserve_invalid(shaped: RID, enabled: bool) -> void:
    pass;
func shaped_text_shape(shaped: RID) -> bool:
    pass;
func shaped_text_substr(shaped: RID, start: int, length: int) -> RID:
    pass;
func shaped_text_tab_align(shaped: RID, tab_stops: PackedFloat32Array) -> float:
    pass;
func tag_to_name(tag: int) -> String:
    pass;
