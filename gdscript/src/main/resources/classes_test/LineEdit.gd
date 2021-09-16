extends Control
class_name LineEdit
const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;
const ALIGN_FILL = 3;
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

var align: int;
var caret_blink: bool;
var caret_blink_speed: float;
var caret_column: int;
var caret_force_displayed: bool;
var caret_mid_grapheme: bool;
var clear_button_enabled: bool;
var context_menu_enabled: bool;
var draw_control_chars: bool;
var editable: bool;
var expand_to_text_length: bool;
var focus_mode: int;
var language: String;
var max_length: int;
var mouse_default_cursor_shape: int;
var placeholder_alpha: float;
var placeholder_text: String;
var right_icon: Texture2D;
var secret: bool;
var secret_character: String;
var selecting_enabled: bool;
var shortcut_keys_enabled: bool;
var structured_text_bidi_override: int;
var structured_text_bidi_override_options: Array;
var text: String;
var text_direction: int;
var virtual_keyboard_enabled: bool;

func clear() -> void:
    pass;
func clear_opentype_features() -> void:
    pass;
func delete_char_at_caret() -> void:
    pass;
func delete_text(from_column: int, to_column: int) -> void:
    pass;
func deselect() -> void:
    pass;
func get_menu() -> PopupMenu:
    pass;
func get_opentype_feature(tag: String) -> int:
    pass;
func get_scroll_offset() -> int:
    pass;
func insert_text_at_caret(text: String) -> void:
    pass;
func menu_option(option: int) -> void:
    pass;
func select(from: int, to: int) -> void:
    pass;
func select_all() -> void:
    pass;
func set_opentype_feature(tag: String, value: int) -> void:
    pass;
