extends Node3D
class_name NavigationRegion3D

var enabled: bool;
var layers: int;
var navmesh: NavigationMesh;

func bake_navigation_mesh() -> void:
    pass;
