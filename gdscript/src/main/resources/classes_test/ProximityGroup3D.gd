extends Node3D
class_name ProximityGroup3D
const MODE_PROXY = 0;
const MODE_SIGNAL = 1;

var dispatch_mode: int;
var grid_radius: Vector3;
var group_name: String;

func broadcast(method: String, parameters: Variant) -> void:
    pass;
