extends Node
class_name SkeletonIK3D


var interpolation: float setget set_interpolation, get_interpolation;
var magnet: Vector3 setget set_magnet_position, get_magnet_position;
var max_iterations: int setget set_max_iterations, get_max_iterations;
var min_distance: float setget set_min_distance, get_min_distance;
var override_tip_basis: bool setget set_override_tip_basis, is_override_tip_basis;
var root_bone: String setget set_root_bone, get_root_bone;
var target: Transform3D setget set_target_transform, get_target_transform;
var target_node: NodePath setget set_target_node, get_target_node;
var tip_bone: String setget set_tip_bone, get_tip_bone;
var use_magnet: bool setget set_use_magnet, is_using_magnet;

func get_parent_skeleton() -> Skeleton3D:
    pass;

func is_running() -> bool:
    pass;

func start(one_time: bool) -> void:
    pass;

func stop() -> void:
    pass;

