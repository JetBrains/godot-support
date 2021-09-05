extends Node2D
class_name RemoteTransform2D


var remote_path: NodePath setget set_remote_node, get_remote_node;
var update_position: bool setget set_update_position, get_update_position;
var update_rotation: bool setget set_update_rotation, get_update_rotation;
var update_scale: bool setget set_update_scale, get_update_scale;
var use_global_coordinates: bool setget set_use_global_coordinates, get_use_global_coordinates;

func force_update_cache() -> void:
    pass;

