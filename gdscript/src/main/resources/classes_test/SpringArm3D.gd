extends Node3D
class_name SpringArm3D


var collision_mask: int setget set_collision_mask, get_collision_mask;
var margin: float setget set_margin, get_margin;
var shape: Shape3D setget set_shape, get_shape;
var spring_length: float setget set_length, get_length;

func add_excluded_object(RID: RID) -> void:
    pass;

func clear_excluded_objects() -> void:
    pass;

func get_hit_length() -> float:
    pass;

func remove_excluded_object(RID: RID) -> bool:
    pass;

