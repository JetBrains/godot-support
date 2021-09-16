extends Node3D
class_name RayCast3D

var collide_with_areas: bool;
var collide_with_bodies: bool;
var collision_mask: int;
var debug_shape_custom_color: Color;
var debug_shape_thickness: float;
var enabled: bool;
var exclude_parent: bool;
var target_position: Vector3;

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
func get_collision_normal() -> Vector3:
    pass;
func get_collision_point() -> Vector3:
    pass;
func is_colliding() -> bool:
    pass;
func remove_exception(node: Object) -> void:
    pass;
func remove_exception_rid(rid: RID) -> void:
    pass;
func set_collision_mask_bit(bit: int, value: bool) -> void:
    pass;
