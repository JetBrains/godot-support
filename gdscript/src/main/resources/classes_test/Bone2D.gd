extends Node2D
class_name Bone2D


var rest: Transform2D setget set_rest, get_rest;

func apply_rest() -> void:
    pass;

func get_autocalculate_length_and_angle() -> bool:
    pass;

func get_bone_angle() -> float:
    pass;

func get_default_length() -> float:
    pass;

func get_index_in_skeleton() -> int:
    pass;

func get_length() -> float:
    pass;

func get_skeleton_rest() -> Transform2D:
    pass;

func set_autocalculate_length_and_angle(auto_calculate: bool) -> void:
    pass;

func set_bone_angle(angle: float) -> void:
    pass;

func set_default_length(default_length: float) -> void:
    pass;

func set_length(length: float) -> void:
    pass;

