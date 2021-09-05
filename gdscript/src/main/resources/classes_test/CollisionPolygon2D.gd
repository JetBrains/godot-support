extends Node2D
class_name CollisionPolygon2D

const BUILD_SOLIDS = 0;
const BUILD_SEGMENTS = 1;

var build_mode: int setget set_build_mode, get_build_mode;
var disabled: bool setget set_disabled, is_disabled;
var one_way_collision: bool setget set_one_way_collision, is_one_way_collision_enabled;
var one_way_collision_margin: float setget set_one_way_collision_margin, get_one_way_collision_margin;
var polygon: PackedVector2Array setget set_polygon, get_polygon;

