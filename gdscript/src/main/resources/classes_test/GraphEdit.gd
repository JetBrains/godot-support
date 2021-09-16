extends Control
class_name GraphEdit

var connection_lines_antialiased: bool;
var connection_lines_thickness: float;
var focus_mode: int;
var minimap_enabled: bool;
var minimap_opacity: float;
var minimap_size: Vector2;
var rect_clip_content: bool;
var right_disconnects: bool;
var scroll_offset: Vector2;
var show_zoom_label: bool;
var snap_distance: int;
var use_snap: bool;
var zoom: float;
var zoom_max: float;
var zoom_min: float;
var zoom_step: float;

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
