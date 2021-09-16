extends Container
class_name ScrollContainer

var follow_focus: bool;
var rect_clip_content: bool;
var scroll_deadzone: int;
var scroll_horizontal: int;
var scroll_horizontal_enabled: bool;
var scroll_horizontal_visible: bool;
var scroll_vertical: int;
var scroll_vertical_enabled: bool;
var scroll_vertical_visible: bool;

func ensure_control_visible(control: Control) -> void:
    pass;
func get_h_scrollbar() -> HScrollBar:
    pass;
func get_v_scrollbar() -> VScrollBar:
    pass;
