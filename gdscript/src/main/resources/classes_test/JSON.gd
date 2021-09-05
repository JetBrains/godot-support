extends RefCounted
class_name JSON



func get_data() -> Variant:
    pass;

func get_error_line() -> int:
    pass;

func get_error_message() -> String:
    pass;

func parse(json_string: String) -> int:
    pass;

func stringify(data: Variant, indent: String, sort_keys: bool, full_precision: bool) -> String:
    pass;

