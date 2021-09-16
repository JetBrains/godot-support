extends Node3D
class_name OccluderInstance3D

var bake_mask: int;
var occluder: Occluder3D;

func get_bake_mask_bit(layer: int) -> bool:
    pass;
func set_bake_mask_bit(layer: int, enabled: bool) -> void:
    pass;
