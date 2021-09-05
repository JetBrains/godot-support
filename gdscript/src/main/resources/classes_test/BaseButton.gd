extends Control
class_name BaseButton

const DRAW_NORMAL = 0;
const DRAW_PRESSED = 1;
const DRAW_HOVER = 2;
const DRAW_DISABLED = 3;
const DRAW_HOVER_PRESSED = 4;
const ACTION_MODE_BUTTON_PRESS = 0;
const ACTION_MODE_BUTTON_RELEASE = 1;

var action_mode: int setget set_action_mode, get_action_mode;
var button_group: ButtonGroup setget set_button_group, get_button_group;
var button_mask: int setget set_button_mask, get_button_mask;
var disabled: bool setget set_disabled, is_disabled;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var keep_pressed_outside: bool setget set_keep_pressed_outside, is_keep_pressed_outside;
var pressed: bool setget set_pressed, is_pressed;
var shortcut: Shortcut setget set_shortcut, get_shortcut;
var shortcut_context: Node setget set_shortcut_context, get_shortcut_context;
var shortcut_in_tooltip: bool setget set_shortcut_in_tooltip, is_shortcut_in_tooltip_enabled;
var toggle_mode: bool setget set_toggle_mode, is_toggle_mode;

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

