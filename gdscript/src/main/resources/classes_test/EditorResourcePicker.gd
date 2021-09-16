extends HBoxContainer
class_name EditorResourcePicker

var base_type: String;
var editable: bool;
var edited_resource: Resource;
var toggle_mode: bool;

func _handle_menu_selected(id: int) -> void:
    pass;
func _set_create_options(menu_node: Object) -> void:
    pass;
func get_allowed_types() -> PackedStringArray:
    pass;
func set_toggle_pressed(pressed: bool) -> void:
    pass;
