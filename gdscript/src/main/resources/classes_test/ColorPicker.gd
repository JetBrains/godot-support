extends BoxContainer
class_name ColorPicker

const SHAPE_HSV_RECTANGLE = 0;
const SHAPE_HSV_WHEEL = 1;
const SHAPE_VHS_CIRCLE = 2;

var color: Color setget set_pick_color, get_pick_color;
var deferred_mode: bool setget set_deferred_mode, is_deferred_mode;
var edit_alpha: bool setget set_edit_alpha, is_editing_alpha;
var hsv_mode: bool setget set_hsv_mode, is_hsv_mode;
var picker_shape: int setget set_picker_shape, get_picker_shape;
var presets_enabled: bool setget set_presets_enabled, are_presets_enabled;
var presets_visible: bool setget set_presets_visible, are_presets_visible;
var raw_mode: bool setget set_raw_mode, is_raw_mode;

func add_preset(color: Color) -> void:
    pass;

func erase_preset(color: Color) -> void:
    pass;

func get_presets() -> PackedColorArray:
    pass;

