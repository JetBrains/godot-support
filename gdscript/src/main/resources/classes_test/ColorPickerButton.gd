extends Button
class_name ColorPickerButton


var color: Color setget set_pick_color, get_pick_color;
var edit_alpha: bool setget set_edit_alpha, is_editing_alpha;
var toggle_mode: bool setget set_toggle_mode, is_toggle_mode;

func get_picker() -> ColorPicker:
    pass;

func get_popup() -> PopupPanel:
    pass;

