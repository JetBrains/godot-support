extends Container
class_name ScrollContainer


var follow_focus: bool setget set_follow_focus, is_following_focus;
var rect_clip_content: bool setget set_clip_contents, is_clipping_contents;
var scroll_deadzone: int setget set_deadzone, get_deadzone;
var scroll_horizontal: int setget set_h_scroll, get_h_scroll;
var scroll_horizontal_enabled: bool setget set_enable_h_scroll, is_h_scroll_enabled;
var scroll_horizontal_visible: bool setget set_h_scroll_visible, is_h_scroll_visible;
var scroll_vertical: int setget set_v_scroll, get_v_scroll;
var scroll_vertical_enabled: bool setget set_enable_v_scroll, is_v_scroll_enabled;
var scroll_vertical_visible: bool setget set_v_scroll_visible, is_v_scroll_visible;

func ensure_control_visible(control: Control) -> void:
    pass;

func get_h_scrollbar() -> HScrollBar:
    pass;

func get_v_scrollbar() -> VScrollBar:
    pass;

