extends BaseButton
class_name Button
const ALIGN_LEFT = 0;
const ALIGN_CENTER = 1;
const ALIGN_RIGHT = 2;

var align: int;
var clip_text: bool;
var expand_icon: bool;
var flat: bool;
var icon: Texture2D;
var icon_align: int;
var language: String;
var text: String;
var text_direction: int;

func clear_opentype_features() -> void:
    pass;
func get_opentype_feature(tag: String) -> int:
    pass;
func set_opentype_feature(tag: String, value: int) -> void:
    pass;
