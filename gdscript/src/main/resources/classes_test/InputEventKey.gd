extends InputEventWithModifiers
class_name InputEventKey

var echo: bool;
var keycode: int;
var physical_keycode: int;
var pressed: bool;
var unicode: int;

func get_keycode_with_modifiers() -> int:
    pass;
func get_physical_keycode_with_modifiers() -> int:
    pass;
