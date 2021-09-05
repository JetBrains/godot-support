extends Node2D
class_name Camera2D

const ANCHOR_MODE_FIXED_TOP_LEFT = 0;
const ANCHOR_MODE_DRAG_CENTER = 1;
const CAMERA2D_PROCESS_PHYSICS = 0;
const CAMERA2D_PROCESS_IDLE = 1;

var anchor_mode: int setget set_anchor_mode, get_anchor_mode;
var current: bool setget _set_current, is_current;
var custom_viewport: Node setget set_custom_viewport, get_custom_viewport;
var drag_bottom_margin: float setget set_drag_margin, get_drag_margin;
var drag_horizontal_enabled: bool setget set_drag_horizontal_enabled, is_drag_horizontal_enabled;
var drag_horizontal_offset: float setget set_drag_horizontal_offset, get_drag_horizontal_offset;
var drag_left_margin: float setget set_drag_margin, get_drag_margin;
var drag_right_margin: float setget set_drag_margin, get_drag_margin;
var drag_top_margin: float setget set_drag_margin, get_drag_margin;
var drag_vertical_enabled: bool setget set_drag_vertical_enabled, is_drag_vertical_enabled;
var drag_vertical_offset: float setget set_drag_vertical_offset, get_drag_vertical_offset;
var editor_draw_drag_margin: bool setget set_margin_drawing_enabled, is_margin_drawing_enabled;
var editor_draw_limits: bool setget set_limit_drawing_enabled, is_limit_drawing_enabled;
var editor_draw_screen: bool setget set_screen_drawing_enabled, is_screen_drawing_enabled;
var limit_bottom: int setget set_limit, get_limit;
var limit_left: int setget set_limit, get_limit;
var limit_right: int setget set_limit, get_limit;
var limit_smoothed: bool setget set_limit_smoothing_enabled, is_limit_smoothing_enabled;
var limit_top: int setget set_limit, get_limit;
var offset: Vector2 setget set_offset, get_offset;
var process_callback: int setget set_process_callback, get_process_callback;
var rotating: bool setget set_rotating, is_rotating;
var smoothing_enabled: bool setget set_enable_follow_smoothing, is_follow_smoothing_enabled;
var smoothing_speed: float setget set_follow_smoothing, get_follow_smoothing;
var zoom: Vector2 setget set_zoom, get_zoom;

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

