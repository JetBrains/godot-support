extends Control
class_name RichTextLabel
const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;
const ALIGN_FILL = 3;
const LIST_NUMBERS = 0;
const LIST_LETTERS = 1;
const LIST_ROMAN = 2;
const LIST_DOTS = 3;
const ITEM_FRAME = 0;
const ITEM_TEXT = 1;
const ITEM_IMAGE = 2;
const ITEM_NEWLINE = 3;
const ITEM_FONT = 4;
const ITEM_FONT_SIZE = 5;
const ITEM_FONT_FEATURES = 6;
const ITEM_COLOR = 7;
const ITEM_OUTLINE_SIZE = 8;
const ITEM_OUTLINE_COLOR = 9;
const ITEM_UNDERLINE = 10;
const ITEM_STRIKETHROUGH = 11;
const ITEM_PARAGRAPH = 12;
const ITEM_INDENT = 13;
const ITEM_LIST = 14;
const ITEM_TABLE = 15;
const ITEM_FADE = 16;
const ITEM_SHAKE = 17;
const ITEM_WAVE = 18;
const ITEM_TORNADO = 19;
const ITEM_RAINBOW = 20;
const ITEM_BGCOLOR = 21;
const ITEM_FGCOLOR = 22;
const ITEM_META = 23;
const ITEM_DROPCAP = 24;
const ITEM_CUSTOMFX = 25;

var bbcode_enabled: bool;
var bbcode_text: String;
var custom_effects: Array;
var fit_content_height: bool;
var language: String;
var meta_underlined: bool;
var override_selected_font_color: bool;
var percent_visible: float;
var rect_clip_content: bool;
var scroll_active: bool;
var scroll_following: bool;
var selection_enabled: bool;
var structured_text_bidi_override: int;
var structured_text_bidi_override_options: Array;
var tab_size: int;
var text: String;
var text_direction: int;
var visible_characters: int;

func add_image(image: Texture2D, width: int, height: int, color: Color, inline_align: int) -> void:
    pass;
func add_text(text: String) -> void:
    pass;
func append_bbcode(bbcode: String) -> int:
    pass;
func clear() -> void:
    pass;
func get_content_height() -> int:
    pass;
func get_line_count() -> int:
    pass;
func get_paragraph_count() -> int:
    pass;
func get_selected_text() -> String:
    pass;
func get_selection_from() -> int:
    pass;
func get_selection_to() -> int:
    pass;
func get_total_character_count() -> int:
    pass;
func get_v_scroll() -> VScrollBar:
    pass;
func get_visible_line_count() -> int:
    pass;
func get_visible_paragraph_count() -> int:
    pass;
func install_effect(effect: Variant) -> void:
    pass;
func newline() -> void:
    pass;
func parse_bbcode(bbcode: String) -> int:
    pass;
func parse_expressions_for_values(expressions: PackedStringArray) -> Dictionary:
    pass;
func pop() -> void:
    pass;
func push_bgcolor(bgcolor: Color) -> void:
    pass;
func push_bold() -> void:
    pass;
func push_bold_italics() -> void:
    pass;
func push_cell() -> void:
    pass;
func push_color(color: Color) -> void:
    pass;
func push_dropcap(string: String, font: Font, size: int, dropcap_margins: Rect2, color: Color, outline_size: int, outline_color: Color) -> void:
    pass;
func push_fgcolor(fgcolor: Color) -> void:
    pass;
func push_font(font: Font) -> void:
    pass;
func push_font_features(opentype_features: Dictionary) -> void:
    pass;
func push_font_size(font_size: int) -> void:
    pass;
func push_indent(level: int) -> void:
    pass;
func push_italics() -> void:
    pass;
func push_list(level: int, type: int, capitalize: bool) -> void:
    pass;
func push_meta(data: Variant) -> void:
    pass;
func push_mono() -> void:
    pass;
func push_normal() -> void:
    pass;
func push_outline_color(color: Color) -> void:
    pass;
func push_outline_size(outline_size: int) -> void:
    pass;
func push_paragraph(align: int, base_direction: int, language: String, st_parser: int) -> void:
    pass;
func push_strikethrough() -> void:
    pass;
func push_table(columns: int, inline_align: int) -> void:
    pass;
func push_underline() -> void:
    pass;
func remove_line(line: int) -> bool:
    pass;
func scroll_to_line(line: int) -> void:
    pass;
func scroll_to_paragraph(paragraph: int) -> void:
    pass;
func set_cell_border_color(color: Color) -> void:
    pass;
func set_cell_padding(padding: Rect2) -> void:
    pass;
func set_cell_row_background_color(odd_row_bg: Color, even_row_bg: Color) -> void:
    pass;
func set_cell_size_override(min_size: Vector2, max_size: Vector2) -> void:
    pass;
func set_table_column_expand(column: int, expand: bool, ratio: int) -> void:
    pass;
