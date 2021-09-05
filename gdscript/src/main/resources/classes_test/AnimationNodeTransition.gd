extends AnimationNode
class_name AnimationNodeTransition


var input_count: int setget set_enabled_inputs, get_enabled_inputs;
var xfade_time: float setget set_cross_fade_time, get_cross_fade_time;

func get_input_caption(input: int) -> String:
    pass;

func is_input_set_as_auto_advance(input: int) -> bool:
    pass;

func set_input_as_auto_advance(input: int, enable: bool) -> void:
    pass;

func set_input_caption(input: int, caption: String) -> void:
    pass;

