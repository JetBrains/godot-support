extends Node3D
class_name ProximityGroup3D

const MODE_PROXY = 0;
const MODE_SIGNAL = 1;

var dispatch_mode: int setget set_dispatch_mode, get_dispatch_mode;
var grid_radius: Vector3 setget set_grid_radius, get_grid_radius;
var group_name: String setget set_group_name, get_group_name;

func broadcast(method: String, parameters: Variant) -> void:
    pass;

