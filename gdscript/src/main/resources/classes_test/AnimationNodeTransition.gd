extends AnimationNodeSync
#brief A generic animation transition node for [AnimationTree].
#desc Simple state machine for cases which don't require a more advanced [AnimationNodeStateMachine]. Animations can be connected to the inputs and transition times can be specified.
class_name AnimationNodeTransition


#desc The number of enabled input ports for this node.
var enabled_inputs: int;

#desc If [code]true[/code], the destination animation is played back from the beginning when switched.
var from_start: bool;

var xfade_curve: Curve;

#desc Cross-fading time (in seconds) between each animation connected to the inputs.
var xfade_time: float;



func get_input_caption(input: int) -> String:
	pass;

func is_input_set_as_auto_advance(input: int) -> bool:
	pass;

func set_input_as_auto_advance(input: int, enable: bool) -> void:
	pass;

func set_input_caption(input: int, caption: String) -> void:
	pass;


