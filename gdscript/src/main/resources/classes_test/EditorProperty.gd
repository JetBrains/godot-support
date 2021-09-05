extends Container
class_name EditorProperty


var checkable: bool setget set_checkable, is_checkable;
var checked: bool setget set_checked, is_checked;
var deletable: bool setget set_deletable, is_deletable;
var draw_red: bool setget set_draw_red, is_draw_red;
var keying: bool setget set_keying, is_keying;
var label: String setget set_label, get_label;
var read_only: bool setget set_read_only, is_read_only;

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

