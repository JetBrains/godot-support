extends GeometryInstance3D
class_name SpriteBase3D

const FLAG_TRANSPARENT = 0;
const FLAG_SHADED = 1;
const FLAG_DOUBLE_SIDED = 2;
const FLAG_MAX = 3;
const ALPHA_CUT_DISABLED = 0;
const ALPHA_CUT_DISCARD = 1;
const ALPHA_CUT_OPAQUE_PREPASS = 2;

var alpha_cut: int setget set_alpha_cut_mode, get_alpha_cut_mode;
var axis: int setget set_axis, get_axis;
var billboard: int setget set_billboard_mode, get_billboard_mode;
var centered: bool setget set_centered, is_centered;
var double_sided: bool setget set_draw_flag, get_draw_flag;
var flip_h: bool setget set_flip_h, is_flipped_h;
var flip_v: bool setget set_flip_v, is_flipped_v;
var modulate: Color setget set_modulate, get_modulate;
var offset: Vector2 setget set_offset, get_offset;
var opacity: float setget set_opacity, get_opacity;
var pixel_size: float setget set_pixel_size, get_pixel_size;
var shaded: bool setget set_draw_flag, get_draw_flag;
var transparent: bool setget set_draw_flag, get_draw_flag;

func generate_triangle_mesh() -> TriangleMesh:
    pass;

func get_draw_flag(flag: int) -> bool:
    pass;

func get_item_rect() -> Rect2:
    pass;

func set_draw_flag(flag: int, enabled: bool) -> void:
    pass;

