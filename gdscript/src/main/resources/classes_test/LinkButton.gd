extends BaseButton
class_name LinkButton

const UNDERLINE_MODE_ALWAYS = 0;
const UNDERLINE_MODE_ON_HOVER = 1;
const UNDERLINE_MODE_NEVER = 2;

var focus_mode: int setget set_focus_mode, get_focus_mode;
var language: String setget set_language, get_language;
var mouse_default_cursor_shape: int setget set_default_cursor_shape, get_default_cursor_shape;
var structured_text_bidi_override: int setget set_structured_text_bidi_override, get_structured_text_bidi_override;
var structured_text_bidi_override_options: Array setget set_structured_text_bidi_override_options, get_structured_text_bidi_override_options;
var text: String setget set_text, get_text;
var text_direction: int setget set_text_direction, get_text_direction;
var underline: int setget set_underline_mode, get_underline_mode;

func clear_opentype_features() -> void:
    pass;

func get_opentype_feature(tag: String) -> int:
    pass;

func set_opentype_feature(tag: String, value: int) -> void:
    pass;

