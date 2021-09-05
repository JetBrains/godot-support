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

var fill_mode: int setget set_fill_mode, get_fill_mode;
var mouse_filter: int setget set_mouse_filter, get_mouse_filter;
var nine_patch_stretch: bool setget set_nine_patch_stretch, get_nine_patch_stretch;
var radial_center_offset: Vector2 setget set_radial_center_offset, get_radial_center_offset;
var radial_fill_degrees: float setget set_fill_degrees, get_fill_degrees;
var radial_initial_angle: float setget set_radial_initial_angle, get_radial_initial_angle;
var stretch_margin_bottom: int setget set_stretch_margin, get_stretch_margin;
var stretch_margin_left: int setget set_stretch_margin, get_stretch_margin;
var stretch_margin_right: int setget set_stretch_margin, get_stretch_margin;
var stretch_margin_top: int setget set_stretch_margin, get_stretch_margin;
var texture_over: Texture2D setget set_over_texture, get_over_texture;
var texture_progress: Texture2D setget set_progress_texture, get_progress_texture;
var texture_under: Texture2D setget set_under_texture, get_under_texture;
var tint_over: Color setget set_tint_over, get_tint_over;
var tint_progress: Color setget set_tint_progress, get_tint_progress;
var tint_under: Color setget set_tint_under, get_tint_under;

func get_stretch_margin(margin: int) -> int:
    pass;

func set_stretch_margin(margin: int, value: int) -> void:
    pass;

