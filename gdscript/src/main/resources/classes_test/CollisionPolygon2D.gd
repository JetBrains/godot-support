extends Node2D
class_name CollisionPolygon2D
const BUILD_SOLIDS = 0;
const BUILD_SEGMENTS = 1;

var build_mode: int;
var disabled: bool;
var one_way_collision: bool;
var one_way_collision_margin: float;
var polygon: PackedVector2Array;

