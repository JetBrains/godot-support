extends RefCounted
class_name ConfigFile



func clear() -> void:
    pass;

func erase_section(section: String) -> void:
    pass;

func erase_section_key(section: String, key: String) -> void:
    pass;

func get_section_keys(section: String) -> PackedStringArray:
    pass;

func get_sections() -> PackedStringArray:
    pass;

func get_value(section: String, key: String, default: Variant) -> Variant:
    pass;

func has_section(section: String) -> bool:
    pass;

func has_section_key(section: String, key: String) -> bool:
    pass;

func load(path: String) -> int:
    pass;

func load_encrypted(path: String, key: PackedByteArray) -> int:
    pass;

func load_encrypted_pass(path: String, password: String) -> int:
    pass;

func parse(data: String) -> int:
    pass;

func save(path: String) -> int:
    pass;

func save_encrypted(path: String, key: PackedByteArray) -> int:
    pass;

func save_encrypted_pass(path: String, password: String) -> int:
    pass;

func set_value(section: String, key: String, value: Variant) -> void:
    pass;

