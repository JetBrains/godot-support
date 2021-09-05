extends StyleBox
class_name StyleBoxFlat


var anti_aliasing: bool setget set_anti_aliased, is_anti_aliased;
var anti_aliasing_size: int setget set_aa_size, get_aa_size;
var bg_color: Color setget set_bg_color, get_bg_color;
var border_blend: bool setget set_border_blend, get_border_blend;
var border_color: Color setget set_border_color, get_border_color;
var border_width_bottom: int setget set_border_width, get_border_width;
var border_width_left: int setget set_border_width, get_border_width;
var border_width_right: int setget set_border_width, get_border_width;
var border_width_top: int setget set_border_width, get_border_width;
var corner_detail: int setget set_corner_detail, get_corner_detail;
var corner_radius_bottom_left: int setget set_corner_radius, get_corner_radius;
var corner_radius_bottom_right: int setget set_corner_radius, get_corner_radius;
var corner_radius_top_left: int setget set_corner_radius, get_corner_radius;
var corner_radius_top_right: int setget set_corner_radius, get_corner_radius;
var draw_center: bool setget set_draw_center, is_draw_center_enabled;
var expand_margin_bottom: float setget set_expand_margin, get_expand_margin;
var expand_margin_left: float setget set_expand_margin, get_expand_margin;
var expand_margin_right: float setget set_expand_margin, get_expand_margin;
var expand_margin_top: float setget set_expand_margin, get_expand_margin;
var shadow_color: Color setget set_shadow_color, get_shadow_color;
var shadow_offset: Vector2 setget set_shadow_offset, get_shadow_offset;
var shadow_size: int setget set_shadow_size, get_shadow_size;

func get_border_width(margin: int) -> int:
    pass;

func get_border_width_min() -> int:
    pass;

func get_corner_radius(corner: int) -> int:
    pass;

func get_expand_margin(margin: int) -> float:
    pass;

func set_border_width(margin: int, width: int) -> void:
    pass;

func set_border_width_all(width: int) -> void:
    pass;

func set_corner_radius(corner: int, radius: int) -> void:
    pass;

func set_corner_radius_all(radius: int) -> void:
    pass;

func set_corner_radius_individual(radius_top_left: int, radius_top_right: int, radius_bottom_right: int, radius_bottom_left: int) -> void:
    pass;

func set_expand_margin(margin: int, size: float) -> void:
    pass;

func set_expand_margin_all(size: float) -> void:
    pass;

func set_expand_margin_individual(size_left: float, size_top: float, size_right: float, size_bottom: float) -> void:
    pass;

