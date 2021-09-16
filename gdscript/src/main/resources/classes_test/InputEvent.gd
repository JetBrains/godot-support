extends Resource
class_name InputEvent

var device: int;

func accumulate(with_event: InputEvent) -> bool:
    pass;
func as_text() -> String:
    pass;
func get_action_strength(action: StringName, exact_match: bool) -> float:
    pass;
func is_action(action: StringName, exact_match: bool) -> bool:
    pass;
func is_action_pressed(action: StringName, allow_echo: bool, exact_match: bool) -> bool:
    pass;
func is_action_released(action: StringName, exact_match: bool) -> bool:
    pass;
func is_action_type() -> bool:
    pass;
func is_echo() -> bool:
    pass;
func is_match(event: InputEvent, exact_match: bool) -> bool:
    pass;
func is_pressed() -> bool:
    pass;
func xformed_by(xform: Transform2D, local_ofs: Vector2) -> InputEvent:
    pass;
