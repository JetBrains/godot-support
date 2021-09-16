extends GeometryInstance3D
class_name SpriteBase3D
const FLAG_TRANSPARENT = 0;
const FLAG_SHADED = 1;
const FLAG_DOUBLE_SIDED = 2;
const FLAG_MAX = 3;
const ALPHA_CUT_DISABLED = 0;
const ALPHA_CUT_DISCARD = 1;
const ALPHA_CUT_OPAQUE_PREPASS = 2;

var alpha_cut: int;
var axis: int;
var billboard: int;
var centered: bool;
var double_sided: bool;
var flip_h: bool;
var flip_v: bool;
var modulate: Color;
var offset: Vector2;
var opacity: float;
var pixel_size: float;
var shaded: bool;
var transparent: bool;

func generate_triangle_mesh() -> TriangleMesh:
    pass;
func get_draw_flag(flag: int) -> bool:
    pass;
func get_item_rect() -> Rect2:
    pass;
func set_draw_flag(flag: int, enabled: bool) -> void:
    pass;
