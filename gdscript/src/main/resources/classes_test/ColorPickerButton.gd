extends Button
class_name ColorPickerButton

var color: Color;
var edit_alpha: bool;
var toggle_mode: bool;

func get_picker() -> ColorPicker:
    pass;
func get_popup() -> PopupPanel:
    pass;
