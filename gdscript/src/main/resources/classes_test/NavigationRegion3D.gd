extends Node3D
class_name NavigationRegion3D


var enabled: bool setget set_enabled, is_enabled;
var layers: int setget set_layers, get_layers;
var navmesh: NavigationMesh setget set_navigation_mesh, get_navigation_mesh;

func bake_navigation_mesh() -> void:
    pass;

