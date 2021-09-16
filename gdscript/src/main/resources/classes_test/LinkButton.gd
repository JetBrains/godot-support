extends BaseButton
class_name LinkButton
const UNDERLINE_MODE_ALWAYS = 0;
const UNDERLINE_MODE_ON_HOVER = 1;
const UNDERLINE_MODE_NEVER = 2;

var focus_mode: int;
var language: String;
var mouse_default_cursor_shape: int;
var structured_text_bidi_override: int;
var structured_text_bidi_override_options: Array;
var text: String;
var text_direction: int;
var underline: int;

func clear_opentype_features() -> void:
    pass;
func get_opentype_feature(tag: String) -> int:
    pass;
func set_opentype_feature(tag: String, value: int) -> void:
    pass;
