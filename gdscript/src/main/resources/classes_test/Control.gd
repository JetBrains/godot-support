extends CanvasItem
class_name Control
const FOCUS_NONE = 0;
const FOCUS_CLICK = 1;
const FOCUS_ALL = 2;
const NOTIFICATION_RESIZED = 40;
const NOTIFICATION_MOUSE_ENTER = 41;
const NOTIFICATION_MOUSE_EXIT = 42;
const NOTIFICATION_FOCUS_ENTER = 43;
const NOTIFICATION_FOCUS_EXIT = 44;
const NOTIFICATION_THEME_CHANGED = 45;
const NOTIFICATION_SCROLL_BEGIN = 47;
const NOTIFICATION_SCROLL_END = 48;
const NOTIFICATION_LAYOUT_DIRECTION_CHANGED = 49;
const CURSOR_ARROW = 0;
const CURSOR_IBEAM = 1;
const CURSOR_POINTING_HAND = 2;
const CURSOR_CROSS = 3;
const CURSOR_WAIT = 4;
const CURSOR_BUSY = 5;
const CURSOR_DRAG = 6;
const CURSOR_CAN_DROP = 7;
const CURSOR_FORBIDDEN = 8;
const CURSOR_VSIZE = 9;
const CURSOR_HSIZE = 10;
const CURSOR_BDIAGSIZE = 11;
const CURSOR_FDIAGSIZE = 12;
const CURSOR_MOVE = 13;
const CURSOR_VSPLIT = 14;
const CURSOR_HSPLIT = 15;
const CURSOR_HELP = 16;
const PRESET_TOP_LEFT = 0;
const PRESET_TOP_RIGHT = 1;
const PRESET_BOTTOM_LEFT = 2;
const PRESET_BOTTOM_RIGHT = 3;
const PRESET_CENTER_LEFT = 4;
const PRESET_CENTER_TOP = 5;
const PRESET_CENTER_RIGHT = 6;
const PRESET_CENTER_BOTTOM = 7;
const PRESET_CENTER = 8;
const PRESET_LEFT_WIDE = 9;
const PRESET_TOP_WIDE = 10;
const PRESET_RIGHT_WIDE = 11;
const PRESET_BOTTOM_WIDE = 12;
const PRESET_VCENTER_WIDE = 13;
const PRESET_HCENTER_WIDE = 14;
const PRESET_WIDE = 15;
const PRESET_MODE_MINSIZE = 0;
const PRESET_MODE_KEEP_WIDTH = 1;
const PRESET_MODE_KEEP_HEIGHT = 2;
const PRESET_MODE_KEEP_SIZE = 3;
const SIZE_FILL = 1;
const SIZE_EXPAND = 2;
const SIZE_EXPAND_FILL = 3;
const SIZE_SHRINK_CENTER = 4;
const SIZE_SHRINK_END = 8;
const MOUSE_FILTER_STOP = 0;
const MOUSE_FILTER_PASS = 1;
const MOUSE_FILTER_IGNORE = 2;
const GROW_DIRECTION_BEGIN = 0;
const GROW_DIRECTION_END = 1;
const GROW_DIRECTION_BOTH = 2;
const ANCHOR_BEGIN = 0;
const ANCHOR_END = 1;
const LAYOUT_DIRECTION_INHERITED = 0;
const LAYOUT_DIRECTION_LOCALE = 1;
const LAYOUT_DIRECTION_LTR = 2;
const LAYOUT_DIRECTION_RTL = 3;
const TEXT_DIRECTION_INHERITED = 3;
const TEXT_DIRECTION_AUTO = 0;
const TEXT_DIRECTION_LTR = 1;
const TEXT_DIRECTION_RTL = 2;
const STRUCTURED_TEXT_DEFAULT = 0;
const STRUCTURED_TEXT_URI = 1;
const STRUCTURED_TEXT_FILE = 2;
const STRUCTURED_TEXT_EMAIL = 3;
const STRUCTURED_TEXT_LIST = 4;
const STRUCTURED_TEXT_NONE = 5;
const STRUCTURED_TEXT_CUSTOM = 6;

var anchor_bottom: float;
var anchor_left: float;
var anchor_right: float;
var anchor_top: float;
var focus_mode: int;
var focus_neighbor_bottom: NodePath;
var focus_neighbor_left: NodePath;
var focus_neighbor_right: NodePath;
var focus_neighbor_top: NodePath;
var focus_next: NodePath;
var focus_previous: NodePath;
var grow_horizontal: int;
var grow_vertical: int;
var hint_tooltip: String;
var layout_direction: int;
var mouse_default_cursor_shape: int;
var mouse_filter: int;
var offset_bottom: float;
var offset_left: float;
var offset_right: float;
var offset_top: float;
var rect_clip_content: bool;
var rect_global_position: Vector2;
var rect_min_size: Vector2;
var rect_pivot_offset: Vector2;
var rect_position: Vector2;
var rect_rotation: float;
var rect_scale: Vector2;
var rect_size: Vector2;
var size_flags_horizontal: int;
var size_flags_stretch_ratio: float;
var size_flags_vertical: int;
var theme: Theme;
var theme_type_variation: StringName;

func _can_drop_data(position: Vector2, data: Variant) -> bool:
    pass;
func _drop_data(position: Vector2, data: Variant) -> void:
    pass;
func _get_drag_data(position: Vector2) -> Variant:
    pass;
func _get_minimum_size() -> Vector2:
    pass;
func _gui_input(event: InputEvent) -> void:
    pass;
func _has_point(: Vector2) -> bool:
    pass;
func _make_custom_tooltip(for_text: String) -> Control:
    pass;
func _structured_text_parser(args: Array, text: String) -> void:
    pass;
func accept_event() -> void:
    pass;
func add_theme_color_override(name: StringName, color: Color) -> void:
    pass;
func add_theme_constant_override(name: StringName, constant: int) -> void:
    pass;
func add_theme_font_override(name: StringName, font: Font) -> void:
    pass;
func add_theme_font_size_override(name: StringName, font_size: int) -> void:
    pass;
func add_theme_icon_override(name: StringName, texture: Texture2D) -> void:
    pass;
func add_theme_stylebox_override(name: StringName, stylebox: StyleBox) -> void:
    pass;
func find_next_valid_focus() -> Control:
    pass;
func find_prev_valid_focus() -> Control:
    pass;
func force_drag(data: Variant, preview: Control) -> void:
    pass;
func get_anchor(side: int) -> float:
    pass;
func get_begin() -> Vector2:
    pass;
func get_combined_minimum_size() -> Vector2:
    pass;
func get_cursor_shape(position: Vector2) -> int:
    pass;
func get_end() -> Vector2:
    pass;
func get_focus_neighbor(side: int) -> NodePath:
    pass;
func get_focus_owner() -> Control:
    pass;
func get_global_rect() -> Rect2:
    pass;
func get_minimum_size() -> Vector2:
    pass;
func get_offset(offset: int) -> float:
    pass;
func get_parent_area_size() -> Vector2:
    pass;
func get_parent_control() -> Control:
    pass;
func get_rect() -> Rect2:
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
func get_tooltip(at_position: Vector2) -> String:
    pass;
func grab_click_focus() -> void:
    pass;
func grab_focus() -> void:
    pass;
func has_focus() -> bool:
    pass;
func has_theme_color(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_color_override(name: StringName) -> bool:
    pass;
func has_theme_constant(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_constant_override(name: StringName) -> bool:
    pass;
func has_theme_font(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_font_override(name: StringName) -> bool:
    pass;
func has_theme_font_size(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_font_size_override(name: StringName) -> bool:
    pass;
func has_theme_icon(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_icon_override(name: StringName) -> bool:
    pass;
func has_theme_stylebox(name: StringName, theme_type: StringName) -> bool:
    pass;
func has_theme_stylebox_override(name: StringName) -> bool:
    pass;
func is_layout_rtl() -> bool:
    pass;
func minimum_size_changed() -> void:
    pass;
func release_focus() -> void:
    pass;
func remove_theme_color_override(name: StringName) -> void:
    pass;
func remove_theme_constant_override(name: StringName) -> void:
    pass;
func remove_theme_font_override(name: StringName) -> void:
    pass;
func remove_theme_font_size_override(name: StringName) -> void:
    pass;
func remove_theme_icon_override(name: StringName) -> void:
    pass;
func remove_theme_stylebox_override(name: StringName) -> void:
    pass;
func set_anchor(side: int, anchor: float, keep_offset: bool, push_opposite_anchor: bool) -> void:
    pass;
func set_anchor_and_offset(side: int, anchor: float, offset: float, push_opposite_anchor: bool) -> void:
    pass;
func set_anchors_and_offsets_preset(preset: int, resize_mode: int, margin: int) -> void:
    pass;
func set_anchors_preset(preset: int, keep_offsets: bool) -> void:
    pass;
func set_begin(position: Vector2) -> void:
    pass;
func set_drag_forwarding(target: Control) -> void:
    pass;
func set_drag_preview(control: Control) -> void:
    pass;
func set_end(position: Vector2) -> void:
    pass;
func set_focus_neighbor(side: int, neighbor: NodePath) -> void:
    pass;
func set_global_position(position: Vector2, keep_offsets: bool) -> void:
    pass;
func set_offset(side: int, offset: float) -> void:
    pass;
func set_offsets_preset(preset: int, resize_mode: int, margin: int) -> void:
    pass;
func set_position(position: Vector2, keep_offsets: bool) -> void:
    pass;
func set_size(size: Vector2, keep_offsets: bool) -> void:
    pass;
func warp_mouse(to_position: Vector2) -> void:
    pass;
