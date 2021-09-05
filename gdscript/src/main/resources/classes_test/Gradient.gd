extends Resource
class_name Gradient


var colors: PackedColorArray setget set_colors, get_colors;
var offsets: PackedFloat32Array setget set_offsets, get_offsets;

func add_point(offset: float, color: Color) -> void:
    pass;

func get_color(point: int) -> Color:
    pass;

func get_offset(point: int) -> float:
    pass;

func get_point_count() -> int:
    pass;

func interpolate(offset: float) -> Color:
    pass;

func remove_point(point: int) -> void:
    pass;

func set_color(point: int, color: Color) -> void:
    pass;

func set_offset(point: int, offset: float) -> void:
    pass;

