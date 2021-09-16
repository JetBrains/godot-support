extends StyleBox
class_name StyleBoxFlat

var anti_aliasing: bool;
var anti_aliasing_size: int;
var bg_color: Color;
var border_blend: bool;
var border_color: Color;
var border_width_bottom: int;
var border_width_left: int;
var border_width_right: int;
var border_width_top: int;
var corner_detail: int;
var corner_radius_bottom_left: int;
var corner_radius_bottom_right: int;
var corner_radius_top_left: int;
var corner_radius_top_right: int;
var draw_center: bool;
var expand_margin_bottom: float;
var expand_margin_left: float;
var expand_margin_right: float;
var expand_margin_top: float;
var shadow_color: Color;
var shadow_offset: Vector2;
var shadow_size: int;

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
