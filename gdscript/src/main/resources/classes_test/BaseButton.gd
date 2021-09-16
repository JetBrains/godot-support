extends Control
class_name BaseButton
const DRAW_NORMAL = 0;
const DRAW_PRESSED = 1;
const DRAW_HOVER = 2;
const DRAW_DISABLED = 3;
const DRAW_HOVER_PRESSED = 4;
const ACTION_MODE_BUTTON_PRESS = 0;
const ACTION_MODE_BUTTON_RELEASE = 1;

var action_mode: int;
var button_group: ButtonGroup;
var button_mask: int;
var disabled: bool;
var focus_mode: int;
var keep_pressed_outside: bool;
var pressed: bool;
var shortcut: Shortcut;
var shortcut_context: Node;
var shortcut_in_tooltip: bool;
var toggle_mode: bool;

func _pressed() -> void:
    pass;
func _toggled(button_pressed: bool) -> void:
    pass;
func get_draw_mode() -> int:
    pass;
func is_hovered() -> bool:
    pass;
func set_pressed_no_signal(pressed: bool) -> void:
    pass;
