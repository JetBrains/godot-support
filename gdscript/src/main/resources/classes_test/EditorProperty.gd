extends Container
class_name EditorProperty

var checkable: bool;
var checked: bool;
var deletable: bool;
var draw_red: bool;
var keying: bool;
var label: String;
var read_only: bool;

func _update_property() -> void:
    pass;
func add_focusable(control: Control) -> void:
    pass;
func emit_changed(property: StringName, value: Variant, field: StringName, changing: bool) -> void:
    pass;
func get_edited_object() -> Object:
    pass;
func get_edited_property() -> StringName:
    pass;
func get_tooltip_text() -> String:
    pass;
func set_bottom_editor(editor: Control) -> void:
    pass;
