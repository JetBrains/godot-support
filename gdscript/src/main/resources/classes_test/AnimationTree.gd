extends Node
class_name AnimationTree

const ANIMATION_PROCESS_PHYSICS = 0;
const ANIMATION_PROCESS_IDLE = 1;
const ANIMATION_PROCESS_MANUAL = 2;

var active: bool setget set_active, is_active;
var anim_player: NodePath setget set_animation_player, get_animation_player;
var process_callback: int setget set_process_callback, get_process_callback;
var root_motion_track: NodePath setget set_root_motion_track, get_root_motion_track;
var tree_root: AnimationNode setget set_tree_root, get_tree_root;

func advance(delta: float) -> void:
    pass;

func get_root_motion_transform() -> Transform3D:
    pass;

func rename_parameter(old_name: String, new_name: String) -> void:
    pass;

