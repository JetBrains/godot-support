extends Control
class_name ItemList
const ICON_MODE_TOP = 0;
const ICON_MODE_LEFT = 1;
const SELECT_SINGLE = 0;
const SELECT_MULTI = 1;

var allow_reselect: bool;
var allow_rmb_select: bool;
var auto_height: bool;
var fixed_column_width: int;
var fixed_icon_size: Vector2;
var focus_mode: int;
var icon_mode: int;
var icon_scale: float;
var max_columns: int;
var max_text_lines: int;
var rect_clip_content: bool;
var same_column_width: bool;
var select_mode: int;

func add_icon_item(icon: Texture2D, selectable: bool) -> int:
    pass;
func add_item(text: String, icon: Texture2D, selectable: bool) -> int:
    pass;
func clear() -> void:
    pass;
func clear_item_opentype_features(idx: int) -> void:
    pass;
func deselect(idx: int) -> void:
    pass;
func deselect_all() -> void:
    pass;
func ensure_current_is_visible() -> void:
    pass;
func get_item_at_position(position: Vector2, exact: bool) -> int:
    pass;
func get_item_count() -> int:
    pass;
func get_item_custom_bg_color(idx: int) -> Color:
    pass;
func get_item_custom_fg_color(idx: int) -> Color:
    pass;
func get_item_icon(idx: int) -> Texture2D:
    pass;
func get_item_icon_modulate(idx: int) -> Color:
    pass;
func get_item_icon_region(idx: int) -> Rect2:
    pass;
func get_item_language(idx: int) -> String:
    pass;
func get_item_metadata(idx: int) -> Variant:
    pass;
func get_item_opentype_feature(idx: int, tag: String) -> int:
    pass;
func get_item_text(idx: int) -> String:
    pass;
func get_item_text_direction(idx: int) -> int:
    pass;
func get_item_tooltip(idx: int) -> String:
    pass;
func get_selected_items() -> PackedInt32Array:
    pass;
func get_v_scroll() -> VScrollBar:
    pass;
func is_anything_selected() -> bool:
    pass;
func is_item_disabled(idx: int) -> bool:
    pass;
func is_item_icon_transposed(idx: int) -> bool:
    pass;
func is_item_selectable(idx: int) -> bool:
    pass;
func is_item_tooltip_enabled(idx: int) -> bool:
    pass;
func is_selected(idx: int) -> bool:
    pass;
func move_item(from_idx: int, to_idx: int) -> void:
    pass;
func remove_item(idx: int) -> void:
    pass;
func select(idx: int, single: bool) -> void:
    pass;
func set_item_custom_bg_color(idx: int, custom_bg_color: Color) -> void:
    pass;
func set_item_custom_fg_color(idx: int, custom_fg_color: Color) -> void:
    pass;
func set_item_disabled(idx: int, disabled: bool) -> void:
    pass;
func set_item_icon(idx: int, icon: Texture2D) -> void:
    pass;
func set_item_icon_modulate(idx: int, modulate: Color) -> void:
    pass;
func set_item_icon_region(idx: int, rect: Rect2) -> void:
    pass;
func set_item_icon_transposed(idx: int, transposed: bool) -> void:
    pass;
func set_item_language(idx: int, language: String) -> void:
    pass;
func set_item_metadata(idx: int, metadata: Variant) -> void:
    pass;
func set_item_opentype_feature(idx: int, tag: String, value: int) -> void:
    pass;
func set_item_selectable(idx: int, selectable: bool) -> void:
    pass;
func set_item_text(idx: int, text: String) -> void:
    pass;
func set_item_text_direction(idx: int, direction: int) -> void:
    pass;
func set_item_tooltip(idx: int, tooltip: String) -> void:
    pass;
func set_item_tooltip_enabled(idx: int, enable: bool) -> void:
    pass;
func sort_items_by_text() -> void:
    pass;
