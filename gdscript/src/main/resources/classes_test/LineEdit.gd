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

var align: int setget set_align, get_align;
var caret_blink: bool setget set_caret_blink_enabled, is_caret_blink_enabled;
var caret_blink_speed: float setget set_caret_blink_speed, get_caret_blink_speed;
var caret_column: int setget set_caret_column, get_caret_column;
var caret_force_displayed: bool setget set_caret_force_displayed, is_caret_force_displayed;
var caret_mid_grapheme: bool setget set_caret_mid_grapheme_enabled, is_caret_mid_grapheme_enabled;
var clear_button_enabled: bool setget set_clear_button_enabled, is_clear_button_enabled;
var context_menu_enabled: bool setget set_context_menu_enabled, is_context_menu_enabled;
var draw_control_chars: bool setget set_draw_control_chars, get_draw_control_chars;
var editable: bool setget set_editable, is_editable;
var expand_to_text_length: bool setget set_expand_to_text_length_enabled, is_expand_to_text_length_enabled;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var language: String setget set_language, get_language;
var max_length: int setget set_max_length, get_max_length;
var mouse_default_cursor_shape: int setget set_default_cursor_shape, get_default_cursor_shape;
var placeholder_alpha: float setget set_placeholder_alpha, get_placeholder_alpha;
var placeholder_text: String setget set_placeholder, get_placeholder;
var right_icon: Texture2D setget set_right_icon, get_right_icon;
var secret: bool setget set_secret, is_secret;
var secret_character: String setget set_secret_character, get_secret_character;
var selecting_enabled: bool setget set_selecting_enabled, is_selecting_enabled;
var shortcut_keys_enabled: bool setget set_shortcut_keys_enabled, is_shortcut_keys_enabled;
var structured_text_bidi_override: int setget set_structured_text_bidi_override, get_structured_text_bidi_override;
var structured_text_bidi_override_options: Array setget set_structured_text_bidi_override_options, get_structured_text_bidi_override_options;
var text: String setget set_text, get_text;
var text_direction: int setget set_text_direction, get_text_direction;
var virtual_keyboard_enabled: bool setget set_virtual_keyboard_enabled, is_virtual_keyboard_enabled;

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

