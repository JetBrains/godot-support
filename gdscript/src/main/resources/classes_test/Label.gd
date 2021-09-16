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

var align: int;
var autowrap_mode: int;
var clip_text: bool;
var language: String;
var lines_skipped: int;
var max_lines_visible: int;
var mouse_filter: int;
var percent_visible: float;
var size_flags_vertical: int;
var structured_text_bidi_override: int;
var structured_text_bidi_override_options: Array;
var text: String;
var text_direction: int;
var text_overrun_behavior: int;
var uppercase: bool;
var valign: int;
var visible_characters: int;

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
