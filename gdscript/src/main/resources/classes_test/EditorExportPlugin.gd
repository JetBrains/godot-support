extends RefCounted
class_name EditorExportPlugin



func _export_begin(features: PackedStringArray, is_debug: bool, path: String, flags: int) -> void:
    pass;

func _export_end() -> void:
    pass;

func _export_file(path: String, type: String, features: PackedStringArray) -> void:
    pass;

func add_file(path: String, file: PackedByteArray, remap: bool) -> void:
    pass;

func add_ios_bundle_file(path: String) -> void:
    pass;

func add_ios_cpp_code(code: String) -> void:
    pass;

func add_ios_embedded_framework(path: String) -> void:
    pass;

func add_ios_framework(path: String) -> void:
    pass;

func add_ios_linker_flags(flags: String) -> void:
    pass;

func add_ios_plist_content(plist_content: String) -> void:
    pass;

func add_ios_project_static_lib(path: String) -> void:
    pass;

func add_shared_object(path: String, tags: PackedStringArray) -> void:
    pass;

func skip() -> void:
    pass;

