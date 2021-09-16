extends Object
class_name ResourceLoader
const THREAD_LOAD_INVALID_RESOURCE = 0;
const THREAD_LOAD_IN_PROGRESS = 1;
const THREAD_LOAD_FAILED = 2;
const THREAD_LOAD_LOADED = 3;
const CACHE_MODE_IGNORE = 0;
const CACHE_MODE_REUSE = 1;
const CACHE_MODE_REPLACE = 2;


func exists(path: String, type_hint: String) -> bool:
    pass;
func get_dependencies(path: String) -> PackedStringArray:
    pass;
func get_recognized_extensions_for_type(type: String) -> PackedStringArray:
    pass;
func has_cached(path: String) -> bool:
    pass;
func load(path: String, type_hint: String, cache_mode: int) -> Resource:
    pass;
func load_threaded_get(path: String) -> Resource:
    pass;
func load_threaded_get_status(path: String, progress: Array) -> int:
    pass;
func load_threaded_request(path: String, type_hint: String, use_sub_threads: bool) -> int:
    pass;
func set_abort_on_missing_resources(abort: bool) -> void:
    pass;
