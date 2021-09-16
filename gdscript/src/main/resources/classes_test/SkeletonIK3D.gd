extends Node
class_name SkeletonIK3D

var interpolation: float;
var magnet: Vector3;
var max_iterations: int;
var min_distance: float;
var override_tip_basis: bool;
var root_bone: StringName;
var target: Transform3D;
var target_node: NodePath;
var tip_bone: StringName;
var use_magnet: bool;

func get_parent_skeleton() -> Skeleton3D:
    pass;
func is_running() -> bool:
    pass;
func start(one_time: bool) -> void:
    pass;
func stop() -> void:
    pass;
