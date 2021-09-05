extends Node3D
class_name OccluderInstance3D


var bake_mask: int setget set_bake_mask, get_bake_mask;
var occluder: Occluder3D setget set_occluder, get_occluder;

func get_bake_mask_bit(layer: int) -> bool:
    pass;

func set_bake_mask_bit(layer: int, enabled: bool) -> void:
    pass;

