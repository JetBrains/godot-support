extends HBoxContainer
class_name EditorResourcePicker


var base_type: String setget set_base_type, get_base_type;
var editable: bool setget set_editable, is_editable;
var edited_resource: Resource setget set_edited_resource, get_edited_resource;
var toggle_mode: bool setget set_toggle_mode, is_toggle_mode;

func _handle_menu_selected(id: int) -> void:
    pass;

func _set_create_options(menu_node: Object) -> void:
    pass;

func get_allowed_types() -> PackedStringArray:
    pass;

func set_toggle_pressed(pressed: bool) -> void:
    pass;

