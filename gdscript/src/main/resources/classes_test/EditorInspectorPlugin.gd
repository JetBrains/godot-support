extends RefCounted
class_name EditorInspectorPlugin



func _can_handle(object: Object) -> bool:
    pass;

func _parse_begin() -> void:
    pass;

func _parse_category(category: String) -> void:
    pass;

func _parse_end() -> void:
    pass;

func _parse_property(type: int, path: String, hint: int, hint_text: String, usage: int) -> bool:
    pass;

func add_custom_control(control: Control) -> void:
    pass;

func add_property_editor(property: String, editor: Control) -> void:
    pass;

func add_property_editor_for_multiple_properties(label: String, properties: PackedStringArray, editor: Control) -> void:
    pass;

