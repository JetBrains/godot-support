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

var caret_blink: bool setget cursor_set_blink_enabled, cursor_get_blink_enabled;
var caret_blink_speed: float setget cursor_set_blink_speed, cursor_get_blink_speed;
var caret_block_mode: bool setget cursor_set_block_mode, cursor_is_block_mode;
var caret_mid_grapheme: bool setget set_mid_grapheme_caret_enabled, get_mid_grapheme_caret_enabled;
var caret_moving_by_right_click: bool setget set_right_click_moves_caret, is_right_click_moving_caret;
var context_menu_enabled: bool setget set_context_menu_enabled, is_context_menu_enabled;
var draw_control_chars: bool setget set_draw_control_chars, get_draw_control_chars;
var draw_spaces: bool setget set_draw_spaces, is_drawing_spaces;
var draw_tabs: bool setget set_draw_tabs, is_drawing_tabs;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var highlight_all_occurrences: bool setget set_highlight_all_occurrences, is_highlight_all_occurrences_enabled;
var highlight_current_line: bool setget set_highlight_current_line, is_highlight_current_line_enabled;
var language: String setget set_language, get_language;
var minimap_draw: bool setget draw_minimap, is_drawing_minimap;
var minimap_width: int setget set_minimap_width, get_minimap_width;
var mouse_default_cursor_shape: int setget set_default_cursor_shape, get_default_cursor_shape;
var override_selected_font_color: bool setget set_override_selected_font_color, is_overriding_selected_font_color;
var readonly: bool setget set_readonly, is_readonly;
var scroll_horizontal: int setget set_h_scroll, get_h_scroll;
var scroll_vertical: float setget set_v_scroll, get_v_scroll;
var selecting_enabled: bool setget set_selecting_enabled, is_selecting_enabled;
var shortcut_keys_enabled: bool setget set_shortcut_keys_enabled, is_shortcut_keys_enabled;
var smooth_scrolling: bool setget set_smooth_scroll_enable, is_smooth_scroll_enabled;
var structured_text_bidi_override: int setget set_structured_text_bidi_override, get_structured_text_bidi_override;
var structured_text_bidi_override_options: Array setget set_structured_text_bidi_override_options, get_structured_text_bidi_override_options;
var syntax_highlighter: SyntaxHighlighter setget set_syntax_highlighter, get_syntax_highlighter;
var text: String setget set_text, get_text;
var text_direction: int setget set_text_direction, get_text_direction;
var v_scroll_speed: float setget set_v_scroll_speed, get_v_scroll_speed;
var virtual_keyboard_enabled: bool setget set_virtual_keyboard_enabled, is_virtual_keyboard_enabled;
var wrap_enabled: bool setget set_wrap_enabled, is_wrap_enabled;

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

