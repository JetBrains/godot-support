extends Control
class_name TextEdit
const SEARCH_MATCH_CASE = 1;
const SEARCH_WHOLE_WORDS = 2;
const SEARCH_BACKWARDS = 4;
const SELECTION_MODE_NONE = 0;
const SELECTION_MODE_SHIFT = 1;
const SELECTION_MODE_POINTER = 2;
const SELECTION_MODE_WORD = 3;
const SELECTION_MODE_LINE = 4;
const GUTTER_TYPE_STRING = 0;
const GUTTER_TPYE_ICON = 1;
const GUTTER_TPYE_CUSTOM = 2;
const MENU_CUT = 0;
const MENU_COPY = 1;
const MENU_PASTE = 2;
const MENU_CLEAR = 3;
const MENU_SELECT_ALL = 4;
const MENU_UNDO = 5;
const MENU_REDO = 6;
const MENU_DIR_INHERITED = 7;
const MENU_DIR_AUTO = 8;
const MENU_DIR_LTR = 9;
const MENU_DIR_RTL = 10;
const MENU_DISPLAY_UCC = 11;
const MENU_INSERT_LRM = 12;
const MENU_INSERT_RLM = 13;
const MENU_INSERT_LRE = 14;
const MENU_INSERT_RLE = 15;
const MENU_INSERT_LRO = 16;
const MENU_INSERT_RLO = 17;
const MENU_INSERT_PDF = 18;
const MENU_INSERT_ALM = 19;
const MENU_INSERT_LRI = 20;
const MENU_INSERT_RLI = 21;
const MENU_INSERT_FSI = 22;
const MENU_INSERT_PDI = 23;
const MENU_INSERT_ZWJ = 24;
const MENU_INSERT_ZWNJ = 25;
const MENU_INSERT_WJ = 26;
const MENU_INSERT_SHY = 27;
const MENU_MAX = 28;

var caret_blink: bool;
var caret_blink_speed: float;
var caret_block_mode: bool;
var caret_mid_grapheme: bool;
var caret_moving_by_right_click: bool;
var context_menu_enabled: bool;
var draw_control_chars: bool;
var draw_spaces: bool;
var draw_tabs: bool;
var focus_mode: int;
var highlight_all_occurrences: bool;
var highlight_current_line: bool;
var language: String;
var minimap_draw: bool;
var minimap_width: int;
var mouse_default_cursor_shape: int;
var override_selected_font_color: bool;
var readonly: bool;
var scroll_horizontal: int;
var scroll_vertical: float;
var selecting_enabled: bool;
var shortcut_keys_enabled: bool;
var smooth_scrolling: bool;
var structured_text_bidi_override: int;
var structured_text_bidi_override_options: Array;
var syntax_highlighter: SyntaxHighlighter;
var text: String;
var text_direction: int;
var v_scroll_speed: float;
var virtual_keyboard_enabled: bool;
var wrap_enabled: bool;

func _backspace() -> void:
    pass;
func add_gutter(at: int) -> void:
    pass;
func backspace() -> void:
    pass;
func center_viewport_to_cursor() -> void:
    pass;
func clear_opentype_features() -> void:
    pass;
func clear_undo_history() -> void:
    pass;
func copy() -> void:
    pass;
func cursor_get_column() -> int:
    pass;
func cursor_get_line() -> int:
    pass;
func cursor_set_column(column: int, adjust_viewport: bool) -> void:
    pass;
func cursor_set_line(line: int, adjust_viewport: bool, can_be_hidden: bool, wrap_index: int) -> void:
    pass;
func cut() -> void:
    pass;
func delete_selection() -> void:
    pass;
func deselect() -> void:
    pass;
func get_caret_draw_pos() -> Vector2:
    pass;
func get_first_non_whitespace_column(line: int) -> int:
    pass;
func get_gutter_count() -> int:
    pass;
func get_gutter_name(gutter: int) -> String:
    pass;
func get_gutter_type(gutter: int) -> int:
    pass;
func get_gutter_width(gutter: int) -> int:
    pass;
func get_indent_level(line: int) -> int:
    pass;
func get_line(line: int) -> String:
    pass;
func get_line_background_color(line: int) -> Color:
    pass;
func get_line_count() -> int:
    pass;
func get_line_gutter_icon(line: int, gutter: int) -> Texture2D:
    pass;
func get_line_gutter_item_color(line: int, gutter: int) -> Color:
    pass;
func get_line_gutter_metadata(line: int, gutter: int) -> Variant:
    pass;
func get_line_gutter_text(line: int, gutter: int) -> String:
    pass;
func get_menu() -> PopupMenu:
    pass;
func get_opentype_feature(tag: String) -> int:
    pass;
func get_selection_column() -> int:
    pass;
func get_selection_from_column() -> int:
    pass;
func get_selection_from_line() -> int:
    pass;
func get_selection_line() -> int:
    pass;
func get_selection_mode() -> int:
    pass;
func get_selection_text() -> String:
    pass;
func get_selection_to_column() -> int:
    pass;
func get_selection_to_line() -> int:
    pass;
func get_tab_size() -> int:
    pass;
func get_visible_line_count() -> int:
    pass;
func get_word_under_cursor() -> String:
    pass;
func insert_text_at_cursor(text: String) -> void:
    pass;
func is_caret_visible() -> bool:
    pass;
func is_gutter_clickable(gutter: int) -> bool:
    pass;
func is_gutter_drawn(gutter: int) -> bool:
    pass;
func is_gutter_overwritable(gutter: int) -> bool:
    pass;
func is_line_gutter_clickable(line: int, gutter: int) -> bool:
    pass;
func is_selection_active() -> bool:
    pass;
func menu_option(option: int) -> void:
    pass;
func merge_gutters(from_line: int, to_line: int) -> void:
    pass;
func paste() -> void:
    pass;
func redo() -> void:
    pass;
func remove_gutter(gutter: int) -> void:
    pass;
func search(key: String, flags: int, from_line: int, from_column: int) -> Dictionary:
    pass;
func select(from_line: int, from_column: int, to_line: int, to_column: int) -> void:
    pass;
func select_all() -> void:
    pass;
func set_gutter_clickable(gutter: int, clickable: bool) -> void:
    pass;
func set_gutter_custom_draw(column: int, object: Object, callback: StringName) -> void:
    pass;
func set_gutter_draw(gutter: int, draw: bool) -> void:
    pass;
func set_gutter_name(gutter: int, name: String) -> void:
    pass;
func set_gutter_overwritable(gutter: int, overwritable: bool) -> void:
    pass;
func set_gutter_type(gutter: int, type: int) -> void:
    pass;
func set_gutter_width(gutter: int, width: int) -> void:
    pass;
func set_line(line: int, new_text: String) -> void:
    pass;
func set_line_background_color(line: int, color: Color) -> void:
    pass;
func set_line_gutter_clickable(line: int, gutter: int, clickable: bool) -> void:
    pass;
func set_line_gutter_icon(line: int, gutter: int, icon: Texture2D) -> void:
    pass;
func set_line_gutter_item_color(line: int, gutter: int, color: Color) -> void:
    pass;
func set_line_gutter_metadata(line: int, gutter: int, metadata: Variant) -> void:
    pass;
func set_line_gutter_text(line: int, gutter: int, text: String) -> void:
    pass;
func set_opentype_feature(tag: String, value: int) -> void:
    pass;
func set_selection_mode(mode: int, line: int, column: int) -> void:
    pass;
func set_tab_size(size: int) -> void:
    pass;
func undo() -> void:
    pass;
