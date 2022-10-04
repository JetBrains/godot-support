#brief A node to be used for advanced animation transitions in an [AnimationPlayer].
#desc A node to be used for advanced animation transitions in an [AnimationPlayer].
#desc [b]Note:[/b] When linked with an [AnimationPlayer], several properties and methods of the corresponding [AnimationPlayer] will not function as expected. Playback and transitions should be handled using only the [AnimationTree] and its constituent [AnimationNode](s). The [AnimationPlayer] node should be used solely for adding, deleting, and editing animations.
class_name AnimationTree

#desc The animations will progress during the physics frame (i.e. [method Node._physics_process]).
const ANIMATION_PROCESS_PHYSICS = 0;

#desc The animations will progress during the idle frame (i.e. [method Node._process]).
const ANIMATION_PROCESS_IDLE = 1;

#desc The animations will only progress manually (see [method advance]).
const ANIMATION_PROCESS_MANUAL = 2;


#desc If [code]true[/code], the [AnimationTree] will be processing.
var active: bool;

#desc The path to the [Node] used to evaluate the AnimationNode [Expression] if one is not explicitly specified internally.
var advance_expression_base_node: NodePath;

#desc The path to the [AnimationPlayer] used for animating.
var anim_player: NodePath;

#desc The process mode of this [AnimationTree]. See [enum AnimationProcessCallback] for available modes.
var process_callback: int;

#desc The path to the Animation track used for root motion. Paths must be valid scene-tree paths to a node, and must be specified starting from the parent node of the node that will reproduce the animation. To specify a track that controls properties or bones, append its name after the path, separated by [code]":"[/code]. For example, [code]"character/skeleton:ankle"[/code] or [code]"character/mesh:transform/local"[/code].
#desc If the track has type [constant Animation.TYPE_POSITION_3D], [constant Animation.TYPE_ROTATION_3D] or [constant Animation.TYPE_SCALE_3D] the transformation will be cancelled visually, and the animation will appear to stay in place. See also [method get_root_motion_transform] and [RootMotionView].
var root_motion_track: NodePath;

#desc The root animation node of this [AnimationTree]. See [AnimationNode].
var tree_root: AnimationNode;



#desc Manually advance the animations by the specified time (in seconds).
func advance() -> void:
	pass;

#desc Retrieve the motion of the [member root_motion_track] as a [Transform3D] that can be used elsewhere. If [member root_motion_track] is not a path to a track of type [constant Animation.TYPE_POSITION_3D], [constant Animation.TYPE_SCALE_3D] or [constant Animation.TYPE_ROTATION_3D], returns an identity transformation. See also [member root_motion_track] and [RootMotionView].
func get_root_motion_transform() -> Transform3D:
	pass;

func rename_parameter(old_name: String, new_name: String) -> void:
	pass;


