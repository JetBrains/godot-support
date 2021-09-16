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

var always_on_top: bool;
var borderless: bool;
var content_scale_aspect: int;
var content_scale_mode: int;
var content_scale_size: Vector2i;
var current_screen: int;
var exclusive: bool;
var max_size: Vector2i;
var min_size: Vector2i;
var mode: int;
var position: Vector2i;
var size: Vector2i;
var theme: Theme;
var theme_type_variation: StringName;
var title: String;
var transient: bool;
var transparent: bool;
var unfocusable: bool;
var unresizable: bool;
var visible: bool;
var wrap_controls: bool;

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
