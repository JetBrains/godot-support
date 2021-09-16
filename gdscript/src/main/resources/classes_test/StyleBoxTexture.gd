extends StyleBox
class_name StyleBoxTexture
const AXIS_STRETCH_MODE_STRETCH = 0;
const AXIS_STRETCH_MODE_TILE = 1;
const AXIS_STRETCH_MODE_TILE_FIT = 2;

var axis_stretch_horizontal: int;
var axis_stretch_vertical: int;
var draw_center: bool;
var expand_margin_bottom: float;
var expand_margin_left: float;
var expand_margin_right: float;
var expand_margin_top: float;
var margin_bottom: float;
var margin_left: float;
var margin_right: float;
var margin_top: float;
var modulate_color: Color;
var region_rect: Rect2;
var texture: Texture2D;

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
