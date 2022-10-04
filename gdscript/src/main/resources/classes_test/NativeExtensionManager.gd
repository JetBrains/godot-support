extends Object
class_name NativeExtensionManager

const LOAD_STATUS_OK = 0;

const LOAD_STATUS_FAILED = 1;

const LOAD_STATUS_ALREADY_LOADED = 2;

const LOAD_STATUS_NOT_LOADED = 3;

const LOAD_STATUS_NEEDS_RESTART = 4;




func get_extension(path: String) -> NativeExtension:
	pass;

func get_loaded_extensions() -> PackedStringArray:
	pass;

func is_extension_loaded(path: String) -> bool:
	pass;

func load_extension(path: String) -> int:
	pass;

func reload_extension(path: String) -> int:
	pass;

func unload_extension(path: String) -> int:
	pass;


