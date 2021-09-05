extends StyleBox
class_name StyleBoxTexture

const AXIS_STRETCH_MODE_STRETCH = 0;
const AXIS_STRETCH_MODE_TILE = 1;
const AXIS_STRETCH_MODE_TILE_FIT = 2;

var axis_stretch_horizontal: int setget set_h_axis_stretch_mode, get_h_axis_stretch_mode;
var axis_stretch_vertical: int setget set_v_axis_stretch_mode, get_v_axis_stretch_mode;
var draw_center: bool setget set_draw_center, is_draw_center_enabled;
var expand_margin_bottom: float setget set_expand_margin_size, get_expand_margin_size;
var expand_margin_left: float setget set_expand_margin_size, get_expand_margin_size;
var expand_margin_right: float setget set_expand_margin_size, get_expand_margin_size;
var expand_margin_top: float setget set_expand_margin_size, get_expand_margin_size;
var margin_bottom: float setget set_margin_size, get_margin_size;
var margin_left: float setget set_margin_size, get_margin_size;
var margin_right: float setget set_margin_size, get_margin_size;
var margin_top: float setget set_margin_size, get_margin_size;
var modulate_color: Color setget set_modulate, get_modulate;
var region_rect: Rect2 setget set_region_rect, get_region_rect;
var texture: Texture2D setget set_texture, get_texture;

func get_expand_margin_size(margin: int) -> float:
    pass;

func get_margin_size(margin: int) -> float:
    pass;

func set_expand_margin_all(size: float) -> void:
    pass;

func set_expand_margin_individual(size_left: float, size_top: float, size_right: float, size_bottom: float) -> void:
    pass;

func set_expand_margin_size(margin: int, size: float) -> void:
    pass;

func set_margin_size(margin: int, size: float) -> void:
    pass;

