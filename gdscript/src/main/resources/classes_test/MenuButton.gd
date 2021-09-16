extends Button
class_name MenuButton

var action_mode: int;
var flat: bool;
var focus_mode: int;
var switch_on_hover: bool;
var toggle_mode: bool;

func get_popup() -> PopupMenu:
    pass;
func set_disable_shortcuts(disabled: bool) -> void:
    pass;
