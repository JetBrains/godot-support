extends Resource
#brief Playback control for [AnimationNodeStateMachine].
#desc Allows control of [AnimationTree] state machines created with [AnimationNodeStateMachine]. Retrieve with [code]$AnimationTree.get("parameters/playback")[/code].
#desc [b]Example:[/b]
#desc [codeblocks]
#desc [gdscript]
#desc var state_machine = $AnimationTree.get("parameters/playback")
#desc state_machine.travel("some_state")
#desc [/gdscript]
#desc [csharp]
#desc var stateMachine = GetNode<AnimationTree>("AnimationTree").Get("parameters/playback") as AnimationNodeStateMachinePlayback;
#desc stateMachine.Travel("some_state");
#desc [/csharp]
#desc [/codeblocks]
class_name AnimationNodeStateMachinePlayback


var resource_local_to_scene: bool;



func get_current_length() -> float:
	pass;

#desc Returns the currently playing animation state.
func get_current_node() -> StringName:
	pass;

#desc Returns the playback position within the current animation state.
func get_current_play_position() -> float:
	pass;

#desc Returns the current travel path as computed internally by the A* algorithm.
func get_travel_path() -> PackedStringArray:
	pass;

#desc Returns [code]true[/code] if an animation is playing.
func is_playing() -> bool:
	pass;

#desc Starts playing the given animation.
func start(node: StringName) -> void:
	pass;

#desc Stops the currently playing animation.
func stop() -> void:
	pass;

#desc Transitions from the current state to another one, following the shortest path.
func travel(to_node: StringName) -> void:
	pass;


