extends Control
class_name Label

const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;
const ALIGN_FILL = 3;
const VALIGN_TOP = 0;
const VALIGN_CENTER = 1;
const VALIGN_BOTTOM = 2;
const VALIGN_FILL = 3;
const AUTOWRAP_OFF = 0;
const AUTOWRAP_ARBITRARY = 1;
const AUTOWRAP_WORD = 2;
const AUTOWRAP_WORD_SMART = 3;
const OVERRUN_NO_TRIMMING = 0;
const OVERRUN_TRIM_CHAR = 1;
const OVERRUN_TRIM_WORD = 2;
const OVERRUN_TRIM_ELLIPSIS = 3;
const OVERRUN_TRIM_WORD_ELLIPSIS = 4;

var align: int setget set_align, get_align;
var autowrap_mode: int setget set_autowrap_mode, get_autowrap_mode;
var clip_text: bool setget set_clip_text, is_clipping_text;
var language: String setget set_language, get_language;
var lines_skipped: int setget set_lines_skipped, get_lines_skipped;
var max_lines_visible: int setget set_max_lines_visible, get_max_lines_visible;
var mouse_filter: int setget set_mouse_filter, get_mouse_filter;
var percent_visible: float setget set_percent_visible, get_percent_visible;
var size_flags_vertical: int setget set_v_size_flags, get_v_size_flags;
var structured_text_bidi_override: int setget set_structured_text_bidi_override, get_structured_text_bidi_override;
var structured_text_bidi_override_options: Array setget set_structured_text_bidi_override_options, get_structured_text_bidi_override_options;
var text: String setget set_text, get_text;
var text_direction: int setget set_text_direction, get_text_direction;
var text_overrun_behavior: int setget set_text_overrun_behavior, get_text_overrun_behavior;
var uppercase: bool setget set_uppercase, is_uppercase;
var valign: int setget set_valign, get_valign;
var visible_characters: int setget set_visible_characters, get_visible_characters;

func clear_opentype_features() -> void:
    pass;

func get_line_count() -> int:
    pass;

func get_line_height(line: int) -> int:
    pass;

func get_opentype_feature(tag: String) -> int:
    pass;

func get_total_character_count() -> int:
    pass;

func get_visible_line_count() -> int:
    pass;

func set_opentype_feature(tag: String, value: int) -> void:
    pass;

