extends Control
class_name NinePatchRect

const AXIS_STRETCH_MODE_STRETCH = 0;
const AXIS_STRETCH_MODE_TILE = 1;
const AXIS_STRETCH_MODE_TILE_FIT = 2;

var axis_stretch_horizontal: int setget set_h_axis_stretch_mode, get_h_axis_stretch_mode;
var axis_stretch_vertical: int setget set_v_axis_stretch_mode, get_v_axis_stretch_mode;
var draw_center: bool setget set_draw_center, is_draw_center_enabled;
var mouse_filter: int setget set_mouse_filter, get_mouse_filter;
var patch_margin_bottom: int setget set_patch_margin, get_patch_margin;
var patch_margin_left: int setget set_patch_margin, get_patch_margin;
var patch_margin_right: int setget set_patch_margin, get_patch_margin;
var patch_margin_top: int setget set_patch_margin, get_patch_margin;
var region_rect: Rect2 setget set_region_rect, get_region_rect;
var texture: Texture2D setget set_texture, get_texture;

func get_patch_margin(margin: int) -> int:
    pass;

func set_patch_margin(margin: int, value: int) -> void:
    pass;

