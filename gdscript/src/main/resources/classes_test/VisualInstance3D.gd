extends Node3D
class_name VisualInstance3D


var layers: int setget set_layer_mask, get_layer_mask;

func get_aabb() -> AABB:
    pass;

func get_base() -> RID:
    pass;

func get_instance() -> RID:
    pass;

func get_layer_mask_bit(layer: int) -> bool:
    pass;

func get_transformed_aabb() -> AABB:
    pass;

func set_base(base: RID) -> void:
    pass;

func set_layer_mask_bit(layer: int, enabled: bool) -> void:
    pass;

