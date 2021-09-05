extends RefCounted
class_name PhysicsShapeQueryParameters2D


var collide_with_areas: bool setget set_collide_with_areas, is_collide_with_areas_enabled;
var collide_with_bodies: bool setget set_collide_with_bodies, is_collide_with_bodies_enabled;
var collision_layer: int setget set_collision_layer, get_collision_layer;
var exclude: Array setget set_exclude, get_exclude;
var margin: float setget set_margin, get_margin;
var motion: Vector2 setget set_motion, get_motion;
var shape: Resource setget set_shape, get_shape;
var shape_rid: RID setget set_shape_rid, get_shape_rid;
var transform: Transform2D setget set_transform, get_transform;

