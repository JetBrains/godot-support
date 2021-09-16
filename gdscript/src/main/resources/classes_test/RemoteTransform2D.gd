extends Node2D
class_name RemoteTransform2D

var remote_path: NodePath;
var update_position: bool;
var update_rotation: bool;
var update_scale: bool;
var use_global_coordinates: bool;

func force_update_cache() -> void:
    pass;
