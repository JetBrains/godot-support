extends Node2D
class_name RayCast2D


var collide_with_areas: bool setget set_collide_with_areas, is_collide_with_areas_enabled;
var collide_with_bodies: bool setget set_collide_with_bodies, is_collide_with_bodies_enabled;
var collision_mask: int setget set_collision_mask, get_collision_mask;
var enabled: bool setget set_enabled, is_enabled;
var exclude_parent: bool setget set_exclude_parent_body, get_exclude_parent_body;
var target_position: Vector2 setget set_target_position, get_target_position;

func add_exception(node: Object) -> void:
    pass;

func add_exception_rid(rid: RID) -> void:
    pass;

func clear_exceptions() -> void:
    pass;

func force_raycast_update() -> void:
    pass;

func get_collider() -> Object:
    pass;

func get_collider_shape() -> int:
    pass;

func get_collision_mask_bit(bit: int) -> bool:
    pass;

func get_collision_normal() -> Vector2:
    pass;

func get_collision_point() -> Vector2:
    pass;

func is_colliding() -> bool:
    pass;

func remove_exception(node: Object) -> void:
    pass;

func remove_exception_rid(rid: RID) -> void:
    pass;

func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;

