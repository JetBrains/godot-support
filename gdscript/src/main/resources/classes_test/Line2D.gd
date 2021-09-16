extends Node2D
class_name Line2D
const LINE_JOINT_SHARP = 0;
const LINE_JOINT_BEVEL = 1;
const LINE_JOINT_ROUND = 2;
const LINE_CAP_NONE = 0;
const LINE_CAP_BOX = 1;
const LINE_CAP_ROUND = 2;
const LINE_TEXTURE_NONE = 0;
const LINE_TEXTURE_TILE = 1;
const LINE_TEXTURE_STRETCH = 2;

var antialiased: bool;
var begin_cap_mode: int;
var default_color: Color;
var end_cap_mode: int;
var gradient: Gradient;
var joint_mode: int;
var points: PackedVector2Array;
var round_precision: int;
var sharp_limit: float;
var texture: Texture2D;
var texture_mode: int;
var width: float;
var width_curve: Curve;

func add_point(position: Vector2, at_position: int) -> void:
    pass;
func clear_points() -> void:
    pass;
func get_point_count() -> int:
    pass;
func get_point_position(i: int) -> Vector2:
    pass;
func remove_point(i: int) -> void:
    pass;
func set_point_position(i: int, position: Vector2) -> void:
    pass;
