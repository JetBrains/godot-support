extends Resource
class_name Translation


var locale: String setget set_locale, get_locale;

func add_message(src_message: StringName, xlated_message: StringName, context: StringName) -> void:
    pass;

func add_plural_message(src_message: StringName, xlated_messages: PackedStringArray, context: StringName) -> void:
    pass;

func erase_message(src_message: StringName, context: StringName) -> void:
    pass;

func get_message(src_message: StringName, context: StringName) -> StringName:
    pass;

func get_message_count() -> int:
    pass;

func get_message_list() -> PackedStringArray:
    pass;

func get_plural_message(src_message: StringName, src_plural_message: StringName, n: int, context: StringName) -> StringName:
    pass;

