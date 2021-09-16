extends RefCounted
class_name ResourceFormatLoader
const CACHE_MODE_IGNORE = 0;
const CACHE_MODE_REUSE = 1;
const CACHE_MODE_REPLACE = 2;


func _get_dependencies(path: String, add_types: String) -> void:
    pass;
func _get_recognized_extensions() -> PackedStringArray:
    pass;
func _get_resource_type(path: String) -> String:
    pass;
func _handles_type(typename: StringName) -> bool:
    pass;
func _load(path: String, original_path: String, use_sub_threads: bool, cache_mode: int) -> Variant:
    pass;
func _rename_dependencies(path: String, renames: String) -> int:
    pass;
