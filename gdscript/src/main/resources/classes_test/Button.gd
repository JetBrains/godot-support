extends BaseButton
class_name Button

const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;

var align: int setget set_text_align, get_text_align;
var clip_text: bool setget set_clip_text, get_clip_text;
var expand_icon: bool setget set_expand_icon, is_expand_icon;
var flat: bool setget set_flat, is_flat;
var icon: Texture2D setget set_button_icon, get_button_icon;
var icon_align: int setget set_icon_align, get_icon_align;
var language: String setget set_language, get_language;
var text: String setget set_text, get_text;
var text_direction: int setget set_text_direction, get_text_direction;

func clear_opentype_features() -> void:
    pass;

func get_opentype_feature(tag: String) -> int:
    pass;

func set_opentype_feature(tag: String, value: int) -> void:
    pass;

