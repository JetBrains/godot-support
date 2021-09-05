extends Object
class_name TranslationServer



func add_translation(translation: Translation) -> void:
    pass;

func clear() -> void:
    pass;

func get_loaded_locales() -> Array:
    pass;

func get_locale() -> String:
    pass;

func get_locale_name(locale: String) -> String:
    pass;

func get_translation_object(locale: String) -> Translation:
    pass;

func remove_translation(translation: Translation) -> void:
    pass;

func set_locale(locale: String) -> void:
    pass;

func translate(message: StringName, context: StringName) -> StringName:
    pass;

func translate_plural(message: StringName, plural_message: StringName, n: int, context: StringName) -> StringName:
    pass;

