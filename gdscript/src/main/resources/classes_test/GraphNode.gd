extends Container
class_name GraphNode
const OVERLAY_DISABLED = 0;
const OVERLAY_BREAKPOINT = 1;
const OVERLAY_POSITION = 2;

var comment: bool;
var language: String;
var mouse_filter: int;
var overlay: int;
var position_offset: Vector2;
var resizable: bool;
var selected: bool;
var show_close: bool;
var text_direction: int;
var title: String;

func clear_all_slots() -> void:
    pass;
func clear_opentype_features() -> void:
    pass;
func clear_slot(idx: int) -> void:
    pass;
func get_connection_input_color(idx: int) -> Color:
    pass;
func get_connection_input_count() -> int:
    pass;
func get_connection_input_position(idx: int) -> Vector2:
    pass;
func get_connection_input_type(idx: int) -> int:
    pass;
func get_connection_output_color(idx: int) -> Color:
    pass;
func get_connection_output_count() -> int:
    pass;
func get_connection_output_position(idx: int) -> Vector2:
    pass;
func get_connection_output_type(idx: int) -> int:
    pass;
func get_opentype_feature(tag: String) -> int:
    pass;
func get_slot_color_left(idx: int) -> Color:
    pass;
func get_slot_color_right(idx: int) -> Color:
    pass;
func get_slot_type_left(idx: int) -> int:
    pass;
func get_slot_type_right(idx: int) -> int:
    pass;
func is_slot_enabled_left(idx: int) -> bool:
    pass;
func is_slot_enabled_right(idx: int) -> bool:
    pass;
func set_opentype_feature(tag: String, value: int) -> void:
    pass;
func set_slot(idx: int, enable_left: bool, type_left: int, color_left: Color, enable_right: bool, type_right: int, color_right: Color, custom_left: Texture2D, custom_right: Texture2D) -> void:
    pass;
func set_slot_color_left(idx: int, color_left: Color) -> void:
    pass;
func set_slot_color_right(idx: int, color_right: Color) -> void:
    pass;
func set_slot_enabled_left(idx: int, enable_left: bool) -> void:
    pass;
func set_slot_enabled_right(idx: int, enable_right: bool) -> void:
    pass;
func set_slot_type_left(idx: int, type_left: int) -> void:
    pass;
func set_slot_type_right(idx: int, type_right: int) -> void:
    pass;
