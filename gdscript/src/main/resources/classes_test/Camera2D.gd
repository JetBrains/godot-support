extends Node2D
class_name Camera2D
const ANCHOR_MODE_FIXED_TOP_LEFT = 0;
const ANCHOR_MODE_DRAG_CENTER = 1;
const CAMERA2D_PROCESS_PHYSICS = 0;
const CAMERA2D_PROCESS_IDLE = 1;

var anchor_mode: int;
var current: bool;
var custom_viewport: Node;
var drag_bottom_margin: float;
var drag_horizontal_enabled: bool;
var drag_horizontal_offset: float;
var drag_left_margin: float;
var drag_right_margin: float;
var drag_top_margin: float;
var drag_vertical_enabled: bool;
var drag_vertical_offset: float;
var editor_draw_drag_margin: bool;
var editor_draw_limits: bool;
var editor_draw_screen: bool;
var limit_bottom: int;
var limit_left: int;
var limit_right: int;
var limit_smoothed: bool;
var limit_top: int;
var offset: Vector2;
var process_callback: int;
var rotating: bool;
var smoothing_enabled: bool;
var smoothing_speed: float;
var zoom: Vector2;

func align() -> void:
    pass;
func clear_current() -> void:
    pass;
func force_update_scroll() -> void:
    pass;
func get_camera_position() -> Vector2:
    pass;
func get_camera_screen_center() -> Vector2:
    pass;
func get_drag_margin(margin: int) -> float:
    pass;
func get_limit(margin: int) -> int:
    pass;
func make_current() -> void:
    pass;
func reset_smoothing() -> void:
    pass;
func set_drag_margin(margin: int, drag_margin: float) -> void:
    pass;
func set_limit(margin: int, limit: int) -> void:
    pass;
