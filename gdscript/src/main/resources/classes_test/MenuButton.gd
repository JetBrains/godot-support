extends Button
class_name MenuButton


var action_mode: int setget set_action_mode, get_action_mode;
var flat: bool setget set_flat, is_flat;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var switch_on_hover: bool setget set_switch_on_hover, is_switch_on_hover;
var toggle_mode: bool setget set_toggle_mode, is_toggle_mode;

func get_popup() -> PopupMenu:
    pass;

func set_disable_shortcuts(disabled: bool) -> void:
    pass;

