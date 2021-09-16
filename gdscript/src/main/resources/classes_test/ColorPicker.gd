extends BoxContainer
class_name ColorPicker
const SHAPE_HSV_RECTANGLE = 0;
const SHAPE_HSV_WHEEL = 1;
const SHAPE_VHS_CIRCLE = 2;

var color: Color;
var deferred_mode: bool;
var edit_alpha: bool;
var hsv_mode: bool;
var picker_shape: int;
var presets_enabled: bool;
var presets_visible: bool;
var raw_mode: bool;

func add_preset(color: Color) -> void:
    pass;
func erase_preset(color: Color) -> void:
    pass;
func get_presets() -> PackedColorArray:
    pass;
