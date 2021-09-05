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

var antialiased: bool setget set_antialiased, get_antialiased;
var begin_cap_mode: int setget set_begin_cap_mode, get_begin_cap_mode;
var default_color: Color setget set_default_color, get_default_color;
var end_cap_mode: int setget set_end_cap_mode, get_end_cap_mode;
var gradient: Gradient setget set_gradient, get_gradient;
var joint_mode: int setget set_joint_mode, get_joint_mode;
var points: PackedVector2Array setget set_points, get_points;
var round_precision: int setget set_round_precision, get_round_precision;
var sharp_limit: float setget set_sharp_limit, get_sharp_limit;
var texture: Texture2D setget set_texture, get_texture;
var texture_mode: int setget set_texture_mode, get_texture_mode;
var width: float setget set_width, get_width;
var width_curve: Curve setget set_curve, get_curve;

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

