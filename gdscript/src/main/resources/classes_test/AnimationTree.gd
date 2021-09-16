extends Node
class_name AnimationTree
const ANIMATION_PROCESS_PHYSICS = 0;
const ANIMATION_PROCESS_IDLE = 1;
const ANIMATION_PROCESS_MANUAL = 2;

var active: bool;
var anim_player: NodePath;
var process_callback: int;
var root_motion_track: NodePath;
var tree_root: AnimationNode;

func advance(delta: float) -> void:
    pass;
func get_root_motion_transform() -> Transform3D:
    pass;
func rename_parameter(old_name: String, new_name: String) -> void:
    pass;
