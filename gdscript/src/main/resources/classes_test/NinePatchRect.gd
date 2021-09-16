extends Control
class_name NinePatchRect
const AXIS_STRETCH_MODE_STRETCH = 0;
const AXIS_STRETCH_MODE_TILE = 1;
const AXIS_STRETCH_MODE_TILE_FIT = 2;

var axis_stretch_horizontal: int;
var axis_stretch_vertical: int;
var draw_center: bool;
var mouse_filter: int;
var patch_margin_bottom: int;
var patch_margin_left: int;
var patch_margin_right: int;
var patch_margin_top: int;
var region_rect: Rect2;
var texture: Texture2D;

func get_patch_margin(margin: int) -> int:
    pass;
func set_patch_margin(margin: int, value: int) -> void:
    pass;
