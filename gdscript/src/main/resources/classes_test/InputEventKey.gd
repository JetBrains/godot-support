extends InputEventWithModifiers
class_name InputEventKey


var echo: bool setget set_echo, is_echo;
var keycode: int setget set_keycode, get_keycode;
var physical_keycode: int setget set_physical_keycode, get_physical_keycode;
var pressed: bool setget set_pressed, is_pressed;
var unicode: int setget set_unicode, get_unicode;

func get_keycode_with_modifiers() -> int:
    pass;

func get_physical_keycode_with_modifiers() -> int:
    pass;

