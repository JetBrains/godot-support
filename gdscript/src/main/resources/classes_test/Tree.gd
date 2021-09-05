extends Control
class_name Tree

const SELECT_SINGLE = 0;
const SELECT_ROW = 1;
const SELECT_MULTI = 2;
const DROP_MODE_DISABLED = 0;
const DROP_MODE_ON_ITEM = 1;
const DROP_MODE_INBETWEEN = 2;

var allow_reselect: bool setget set_allow_reselect, get_allow_reselect;
var allow_rmb_select: bool setget set_allow_rmb_select, get_allow_rmb_select;
var columns: int setget set_columns, get_columns;
var drop_mode_flags: int setget set_drop_mode_flags, get_drop_mode_flags;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var hide_folding: bool setget set_hide_folding, is_folding_hidden;
var hide_root: bool setget set_hide_root, is_root_hidden;
var rect_clip_content: bool setget set_clip_contents, is_clipping_contents;
var scroll_horizontal_enabled: bool setget set_h_scroll_enabled, is_h_scroll_enabled;
var scroll_vertical_enabled: bool setget set_v_scroll_enabled, is_v_scroll_enabled;
var select_mode: int setget set_select_mode, get_select_mode;

func are_column_titles_visible() -> bool:
    pass;

func clear() -> void:
    pass;

func clear_column_title_opentype_features(column: int) -> void:
    pass;

func create_item(parent: Object, idx: int) -> TreeItem:
    pass;

func edit_selected() -> bool:
    pass;

func ensure_cursor_is_visible() -> void:
    pass;

func get_column_at_position(position: Vector2) -> int:
    pass;

func get_column_expand_ratio(column: int) -> int:
    pass;

func get_column_title(column: int) -> String:
    pass;

func get_column_title_direction(column: int) -> int:
    pass;

func get_column_title_language(column: int) -> String:
    pass;

func get_column_title_opentype_feature(column: int, tag: String) -> int:
    pass;

func get_column_width(column: int) -> int:
    pass;

func get_custom_popup_rect() -> Rect2:
    pass;

func get_drop_section_at_position(position: Vector2) -> int:
    pass;

func get_edited() -> TreeItem:
    pass;

func get_edited_column() -> int:
    pass;

func get_item_area_rect(item: Object, column: int) -> Rect2:
    pass;

func get_item_at_position(position: Vector2) -> TreeItem:
    pass;

func get_next_selected(from: Object) -> TreeItem:
    pass;

func get_pressed_button() -> int:
    pass;

func get_root() -> TreeItem:
    pass;

func get_scroll() -> Vector2:
    pass;

func get_selected() -> TreeItem:
    pass;

func get_selected_column() -> int:
    pass;

func is_column_clipping_content(column: int) -> bool:
    pass;

func is_column_expanding(column: int) -> bool:
    pass;

func scroll_to_item(item: Object) -> void:
    pass;

func set_column_clip_content(column: int, enable: bool) -> void:
    pass;

func set_column_custom_minimum_width(column: int, min_width: int) -> void:
    pass;

func set_column_expand(column: int, expand: bool) -> void:
    pass;

func set_column_expand_ratio(column: int, ratio: int) -> void:
    pass;

func set_column_title(column: int, title: String) -> void:
    pass;

func set_column_title_direction(column: int, direction: int) -> void:
    pass;

func set_column_title_language(column: int, language: String) -> void:
    pass;

func set_column_title_opentype_feature(column: int, tag: String, value: int) -> void:
    pass;

func set_column_titles_visible(visible: bool) -> void:
    pass;

