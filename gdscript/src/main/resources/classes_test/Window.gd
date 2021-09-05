extends Viewport
class_name Window

const NOTIFICATION_VISIBILITY_CHANGED = 30;
const MODE_WINDOWED = 0;
const MODE_MINIMIZED = 1;
const MODE_MAXIMIZED = 2;
const MODE_FULLSCREEN = 3;
const FLAG_RESIZE_DISABLED = 0;
const FLAG_BORDERLESS = 1;
const FLAG_ALWAYS_ON_TOP = 2;
const FLAG_TRANSPARENT = 3;
const FLAG_NO_FOCUS = 4;
const FLAG_MAX = 5;
const CONTENT_SCALE_MODE_DISABLED = 0;
const CONTENT_SCALE_MODE_CANVAS_ITEMS = 1;
const CONTENT_SCALE_MODE_VIEWPORT = 2;
const CONTENT_SCALE_ASPECT_IGNORE = 0;
const CONTENT_SCALE_ASPECT_KEEP = 1;
const CONTENT_SCALE_ASPECT_KEEP_WIDTH = 2;
const CONTENT_SCALE_ASPECT_KEEP_HEIGHT = 3;
const CONTENT_SCALE_ASPECT_EXPAND = 4;
const LAYOUT_DIRECTION_INHERITED = 0;
const LAYOUT_DIRECTION_LOCALE = 1;
const LAYOUT_DIRECTION_LTR = 2;
const LAYOUT_DIRECTION_RTL = 3;

var always_on_top: bool setget set_flag, get_flag;
var borderless: bool setget set_flag, get_flag;
var content_scale_aspect: int setget set_content_scale_aspect, get_content_scale_aspect;
var content_scale_mode: int setget set_content_scale_mode, get_content_scale_mode;
var content_scale_size: Vector2i setget set_content_scale_size, get_content_scale_size;
var current_screen: int setget set_current_screen, get_current_screen;
var exclusive: bool setget set_exclusive, is_exclusive;
var max_size: Vector2i setget set_max_size, get_max_size;
var min_size: Vector2i setget set_min_size, get_min_size;
var mode: int setget set_mode, get_mode;
var position: Vector2i setget set_position, get_position;
var size: Vector2i setget set_size, get_size;
var theme: Theme setget set_theme, get_theme;
var theme_type_variation: String setget set_theme_type_variation, get_theme_type_variation;
var title: String setget set_title, get_title;
var transient: bool setget set_transient, is_transient;
var transparent: bool setget set_flag, get_flag;
var unfocusable: bool setget set_flag, get_flag;
var unresizable: bool setget set_flag, get_flag;
var visible: bool setget set_visible, is_visible;
var wrap_controls: bool setget set_wrap_controls, is_wrapping_controls;

func can_draw() -> bool:
    pass;

func child_controls_changed() -> void:
    pass;

func get_contents_minimum_size() -> Vector2:
    pass;

func get_flag(flag: int) -> bool:
    pass;

func get_layout_direction() -> int:
    pass;

func get_real_size() -> Vector2i:
    pass;

func get_theme_color(name: StringName, theme_type: StringName) -> Color:
    pass;

func get_theme_constant(name: StringName, theme_type: StringName) -> int:
    pass;

func get_theme_font(name: StringName, theme_type: StringName) -> Font:
    pass;

func get_theme_font_size(name: StringName, theme_type: StringName) -> int:
    pass;

func get_theme_icon(name: StringName, theme_type: StringName) -> Texture2D:
    pass;

func get_theme_stylebox(name: StringName, theme_type: StringName) -> StyleBox:
    pass;

func grab_focus() -> void:
    pass;

func has_focus() -> bool:
    pass;

func has_theme_color(name: StringName, theme_type: StringName) -> bool:
    pass;

func has_theme_constant(name: StringName, theme_type: StringName) -> bool:
    pass;

func has_theme_font(name: StringName, theme_type: StringName) -> bool:
    pass;

func has_theme_font_size(name: StringName, theme_type: StringName) -> bool:
    pass;

func has_theme_icon(name: StringName, theme_type: StringName) -> bool:
    pass;

func has_theme_stylebox(name: StringName, theme_type: StringName) -> bool:
    pass;

func hide() -> void:
    pass;

func is_embedded() -> bool:
    pass;

func is_layout_rtl() -> bool:
    pass;

func is_maximize_allowed() -> bool:
    pass;

func is_using_font_oversampling() -> bool:
    pass;

func move_to_foreground() -> void:
    pass;

func popup(rect: Rect2i) -> void:
    pass;

func popup_centered(minsize: Vector2i) -> void:
    pass;

func popup_centered_clamped(minsize: Vector2i, fallback_ratio: float) -> void:
    pass;

func popup_centered_ratio(ratio: float) -> void:
    pass;

func popup_on_parent(parent_rect: Rect2i) -> void:
    pass;

func request_attention() -> void:
    pass;

func set_flag(flag: int, enabled: bool) -> void:
    pass;

func set_ime_active(active: bool) -> void:
    pass;

func set_ime_position(position: Vector2i) -> void:
    pass;

func set_layout_direction(direction: int) -> void:
    pass;

func set_use_font_oversampling(enable: bool) -> void:
    pass;

func show() -> void:
    pass;

