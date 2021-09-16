extends RefCounted
class_name Expression


func execute(inputs: Array, base_instance: Object, show_error: bool) -> Variant:
    pass;
func get_error_text() -> String:
    pass;
func has_execute_failed() -> bool:
    pass;
func parse(expression: String, input_names: PackedStringArray) -> int:
    pass;
