extends Resource
class_name Script


var source_code: String setget set_source_code, get_source_code;

func can_instantiate() -> bool:
    pass;

func get_base_script() -> Script:
    pass;

func get_instance_base_type() -> StringName:
    pass;

func get_property_default_value(property: StringName) -> Variant:
    pass;

func get_script_constant_map() -> Dictionary:
    pass;

func get_script_method_list() -> Array:
    pass;

func get_script_property_list() -> Array:
    pass;

func get_script_signal_list() -> Array:
    pass;

func has_script_signal(signal_name: StringName) -> bool:
    pass;

func has_source_code() -> bool:
    pass;

func instance_has(base_object: Object) -> bool:
    pass;

func is_tool() -> bool:
    pass;

func reload(keep_state: bool) -> int:
    pass;

