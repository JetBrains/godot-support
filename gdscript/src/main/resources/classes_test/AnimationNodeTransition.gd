extends AnimationNode
class_name AnimationNodeTransition

var input_count: int;
var xfade_time: float;

func get_input_caption(input: int) -> String:
    pass;
func is_input_set_as_auto_advance(input: int) -> bool:
    pass;
func set_input_as_auto_advance(input: int, enable: bool) -> void:
    pass;
func set_input_caption(input: int, caption: String) -> void:
    pass;
