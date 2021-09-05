extends Control
class_name GraphEdit


var connection_lines_antialiased: bool setget set_connection_lines_antialiased, is_connection_lines_antialiased;
var connection_lines_thickness: float setget set_connection_lines_thickness, get_connection_lines_thickness;
var focus_mode: int setget set_focus_mode, get_focus_mode;
var minimap_enabled: bool setget set_minimap_enabled, is_minimap_enabled;
var minimap_opacity: float setget set_minimap_opacity, get_minimap_opacity;
var minimap_size: Vector2 setget set_minimap_size, get_minimap_size;
var rect_clip_content: bool setget set_clip_contents, is_clipping_contents;
var right_disconnects: bool setget set_right_disconnects, is_right_disconnects_enabled;
var scroll_offset: Vector2 setget set_scroll_ofs, get_scroll_ofs;
var show_zoom_label: bool setget set_show_zoom_label, is_showing_zoom_label;
var snap_distance: int setget set_snap, get_snap;
var use_snap: bool setget set_use_snap, is_using_snap;
var zoom: float setget set_zoom, get_zoom;
var zoom_max: float setget set_zoom_max, get_zoom_max;
var zoom_min: float setget set_zoom_min, get_zoom_min;
var zoom_step: float setget set_zoom_step, get_zoom_step;

func add_valid_connection_type(from_type: int, to_type: int) -> void:
    pass;

func add_valid_left_disconnect_type(type: int) -> void:
    pass;

func add_valid_right_disconnect_type(type: int) -> void:
    pass;

func clear_connections() -> void:
    pass;

func connect_node(from: StringName, from_port: int, to: StringName, to_port: int) -> int:
    pass;

func disconnect_node(from: StringName, from_port: int, to: StringName, to_port: int) -> void:
    pass;

func get_connection_list() -> Array:
    pass;

func get_zoom_hbox() -> HBoxContainer:
    pass;

func is_node_connected(from: StringName, from_port: int, to: StringName, to_port: int) -> bool:
    pass;

func is_valid_connection_type(from_type: int, to_type: int) -> bool:
    pass;

func remove_valid_connection_type(from_type: int, to_type: int) -> void:
    pass;

func remove_valid_left_disconnect_type(type: int) -> void:
    pass;

func remove_valid_right_disconnect_type(type: int) -> void:
    pass;

func set_connection_activity(from: StringName, from_port: int, to: StringName, to_port: int, amount: float) -> void:
    pass;

func set_selected(node: Node) -> void:
    pass;

