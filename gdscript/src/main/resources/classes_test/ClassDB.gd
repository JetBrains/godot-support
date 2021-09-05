extends Object
class_name ClassDB



func can_instantiate(class: StringName) -> bool:
    pass;

func class_exists(class: StringName) -> bool:
    pass;

func class_get_category(class: StringName) -> StringName:
    pass;

func class_get_integer_constant(class: StringName, name: StringName) -> int:
    pass;

func class_get_integer_constant_list(class: StringName, no_inheritance: bool) -> PackedStringArray:
    pass;

func class_get_method_list(class: StringName, no_inheritance: bool) -> Array:
    pass;

func class_get_property(object: Object, property: StringName) -> Variant:
    pass;

func class_get_property_list(class: StringName, no_inheritance: bool) -> Array:
    pass;

func class_get_signal(class: StringName, signal: StringName) -> Dictionary:
    pass;

func class_get_signal_list(class: StringName, no_inheritance: bool) -> Array:
    pass;

func class_has_integer_constant(class: StringName, name: StringName) -> bool:
    pass;

func class_has_method(class: StringName, method: StringName, no_inheritance: bool) -> bool:
    pass;

func class_has_signal(class: StringName, signal: StringName) -> bool:
    pass;

func class_set_property(object: Object, property: StringName, value: Variant) -> int:
    pass;

func get_class_list() -> PackedStringArray:
    pass;

func get_inheriters_from_class(class: StringName) -> PackedStringArray:
    pass;

func get_parent_class(class: StringName) -> StringName:
    pass;

func instantiate(class: StringName) -> Variant:
    pass;

func is_class_enabled(class: StringName) -> bool:
    pass;

func is_parent_class(class: StringName, inherits: StringName) -> bool:
    pass;

