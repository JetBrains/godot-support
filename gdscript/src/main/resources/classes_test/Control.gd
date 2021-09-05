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

var anchor_bottom: float setget _set_anchor, get_anchor;
var anchor_left: float setget _set_anchor, get_anchor;
var anchor_right: float setget _set_anchor, get_anchor;
var anchor_top: float setget _set_anchor, get_anchor;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var focus_neighbor_bottom: NodePath setget set_focus_neighbor, get_focus_neighbor;
var focus_neighbor_left: NodePath setget set_focus_neighbor, get_focus_neighbor;
var focus_neighbor_right: NodePath setget set_focus_neighbor, get_focus_neighbor;
var focus_neighbor_top: NodePath setget set_focus_neighbor, get_focus_neighbor;
var focus_next: NodePath setget set_focus_next, get_focus_next;
var focus_previous: NodePath setget set_focus_previous, get_focus_previous;
var grow_horizontal: int setget set_h_grow_direction, get_h_grow_direction;
var grow_vertical: int setget set_v_grow_direction, get_v_grow_direction;
var hint_tooltip: String setget set_tooltip, _get_tooltip;
var layout_direction: int setget set_layout_direction, get_layout_direction;
var mouse_default_cursor_shape: int setget set_default_cursor_shape, get_default_cursor_shape;
var mouse_filter: int setget set_mouse_filter, get_mouse_filter;
var offset_bottom: float setget set_offset, get_offset;
var offset_left: float setget set_offset, get_offset;
var offset_right: float setget set_offset, get_offset;
var offset_top: float setget set_offset, get_offset;
var rect_clip_content: bool setget set_clip_contents, is_clipping_contents;
var rect_global_position: Vector2 setget _set_global_position, get_global_position;
var rect_min_size: Vector2 setget set_custom_minimum_size, get_custom_minimum_size;
var rect_pivot_offset: Vector2 setget set_pivot_offset, get_pivot_offset;
var rect_position: Vector2 setget _set_position, get_position;
var rect_rotation: float setget set_rotation, get_rotation;
var rect_scale: Vector2 setget set_scale, get_scale;
var rect_size: Vector2 setget _set_size, get_size;
var size_flags_horizontal: int setget set_h_size_flags, get_h_size_flags;
var size_flags_stretch_ratio: float setget set_stretch_ratio, get_stretch_ratio;
var size_flags_vertical: int setget set_v_size_flags, get_v_size_flags;
var theme: Theme setget set_theme, get_theme;
var theme_type_variation: String setget set_theme_type_variation, get_theme_type_variation;

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

