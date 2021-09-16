extends Object
class_name TreeItem
const CELL_MODE_STRING = 0;
const CELL_MODE_CHECK = 1;
const CELL_MODE_RANGE = 2;
const CELL_MODE_ICON = 3;
const CELL_MODE_CUSTOM = 4;
const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;

var collapsed: bool;
var custom_minimum_height: int;
var disable_folding: bool;

func add_button(column: int, button: Texture2D, button_idx: int, disabled: bool, tooltip: String) -> void:
    pass;
func call_recursive(method: StringName) -> Variant:
    pass;
func clear_custom_bg_color(column: int) -> void:
    pass;
func clear_custom_color(column: int) -> void:
    pass;
func clear_opentype_features(column: int) -> void:
    pass;
func create_child(idx: int) -> TreeItem:
    pass;
func deselect(column: int) -> void:
    pass;
func erase_button(column: int, button_idx: int) -> void:
    pass;
func get_button(column: int, button_idx: int) -> Texture2D:
    pass;
func get_button_count(column: int) -> int:
    pass;
func get_button_tooltip(column: int, button_idx: int) -> String:
    pass;
func get_cell_mode(column: int) -> int:
    pass;
func get_child(idx: int) -> TreeItem:
    pass;
func get_child_count() -> int:
    pass;
func get_children() -> Array:
    pass;
func get_custom_bg_color(column: int) -> Color:
    pass;
func get_custom_color(column: int) -> Color:
    pass;
func get_custom_font(column: int) -> Font:
    pass;
func get_expand_right(column: int) -> bool:
    pass;
func get_first_child() -> TreeItem:
    pass;
func get_icon(column: int) -> Texture2D:
    pass;
func get_icon_max_width(column: int) -> int:
    pass;
func get_icon_modulate(column: int) -> Color:
    pass;
func get_icon_region(column: int) -> Rect2:
    pass;
func get_index() -> int:
    pass;
func get_language(column: int) -> String:
    pass;
func get_metadata(column: int) -> Variant:
    pass;
func get_next() -> TreeItem:
    pass;
func get_next_visible(wrap: bool) -> TreeItem:
    pass;
func get_opentype_feature(column: int, tag: String) -> int:
    pass;
func get_parent() -> TreeItem:
    pass;
func get_prev() -> TreeItem:
    pass;
func get_prev_visible(wrap: bool) -> TreeItem:
    pass;
func get_range(column: int) -> float:
    pass;
func get_range_config(column: int) -> Dictionary:
    pass;
func get_structured_text_bidi_override(column: int) -> int:
    pass;
func get_structured_text_bidi_override_options(column: int) -> Array:
    pass;
func get_suffix(column: int) -> String:
    pass;
func get_text(column: int) -> String:
    pass;
func get_text_align(column: int) -> int:
    pass;
func get_text_direction(column: int) -> int:
    pass;
func get_tooltip(column: int) -> String:
    pass;
func get_tree() -> Tree:
    pass;
func is_button_disabled(column: int, button_idx: int) -> bool:
    pass;
func is_checked(column: int) -> bool:
    pass;
func is_custom_set_as_button(column: int) -> bool:
    pass;
func is_editable(column: int) -> bool:
    pass;
func is_selectable(column: int) -> bool:
    pass;
func is_selected(column: int) -> bool:
    pass;
func move_after(item: Object) -> void:
    pass;
func move_before(item: Object) -> void:
    pass;
func remove_child(child: Object) -> void:
    pass;
func select(column: int) -> void:
    pass;
func set_button(column: int, button_idx: int, button: Texture2D) -> void:
    pass;
func set_button_disabled(column: int, button_idx: int, disabled: bool) -> void:
    pass;
func set_cell_mode(column: int, mode: int) -> void:
    pass;
func set_checked(column: int, checked: bool) -> void:
    pass;
func set_custom_as_button(column: int, enable: bool) -> void:
    pass;
func set_custom_bg_color(column: int, color: Color, just_outline: bool) -> void:
    pass;
func set_custom_color(column: int, color: Color) -> void:
    pass;
func set_custom_draw(column: int, object: Object, callback: StringName) -> void:
    pass;
func set_custom_font(column: int, font: Font) -> void:
    pass;
func set_editable(column: int, enabled: bool) -> void:
    pass;
func set_expand_right(column: int, enable: bool) -> void:
    pass;
func set_icon(column: int, texture: Texture2D) -> void:
    pass;
func set_icon_max_width(column: int, width: int) -> void:
    pass;
func set_icon_modulate(column: int, modulate: Color) -> void:
    pass;
func set_icon_region(column: int, region: Rect2) -> void:
    pass;
func set_language(column: int, language: String) -> void:
    pass;
func set_metadata(column: int, meta: Variant) -> void:
    pass;
func set_opentype_feature(column: int, tag: String, value: int) -> void:
    pass;
func set_range(column: int, value: float) -> void:
    pass;
func set_range_config(column: int, min: float, max: float, step: float, expr: bool) -> void:
    pass;
func set_selectable(column: int, selectable: bool) -> void:
    pass;
func set_structured_text_bidi_override(column: int, parser: int) -> void:
    pass;
func set_structured_text_bidi_override_options(column: int, args: Array) -> void:
    pass;
func set_suffix(column: int, text: String) -> void:
    pass;
func set_text(column: int, text: String) -> void:
    pass;
func set_text_align(column: int, text_align: int) -> void:
    pass;
func set_text_direction(column: int, direction: int) -> void:
    pass;
func set_tooltip(column: int, tooltip: String) -> void:
    pass;
func uncollapse_tree() -> void:
    pass;
