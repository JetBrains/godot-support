extends Node3D
class_name CollisionShape3D


var disabled: bool setget set_disabled, is_disabled;
var shape: Shape3D setget set_shape, get_shape;

func make_convex_from_siblings() -> void:
    pass;

func resource_changed(resource: Resource) -> void:
    pass;

