extends Node3D
class_name SpringArm3D

var collision_mask: int;
var margin: float;
var shape: Shape3D;
var spring_length: float;

func add_excluded_object(RID: RID) -> void:
    pass;
func clear_excluded_objects() -> void:
    pass;
func get_hit_length() -> float:
    pass;
func remove_excluded_object(RID: RID) -> bool:
    pass;
