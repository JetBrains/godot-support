extends Range
class_name TextureProgressBar
const FILL_LEFT_TO_RIGHT = 0;
const FILL_RIGHT_TO_LEFT = 1;
const FILL_TOP_TO_BOTTOM = 2;
const FILL_BOTTOM_TO_TOP = 3;
const FILL_CLOCKWISE = 4;
const FILL_COUNTER_CLOCKWISE = 5;
const FILL_BILINEAR_LEFT_AND_RIGHT = 6;
const FILL_BILINEAR_TOP_AND_BOTTOM = 7;
const FILL_CLOCKWISE_AND_COUNTER_CLOCKWISE = 8;

var fill_mode: int;
var mouse_filter: int;
var nine_patch_stretch: bool;
var radial_center_offset: Vector2;
var radial_fill_degrees: float;
var radial_initial_angle: float;
var stretch_margin_bottom: int;
var stretch_margin_left: int;
var stretch_margin_right: int;
var stretch_margin_top: int;
var texture_over: Texture2D;
var texture_progress: Texture2D;
var texture_under: Texture2D;
var tint_over: Color;
var tint_progress: Color;
var tint_under: Color;

func get_stretch_margin(margin: int) -> int:
    pass;
func set_stretch_margin(margin: int, value: int) -> void:
    pass;
