class_name NativeExtensionManager

const LOAD_STATUS_OK = 0;

const LOAD_STATUS_FAILED = 1;

const LOAD_STATUS_ALREADY_LOADED = 2;

const LOAD_STATUS_NOT_LOADED = 3;

const LOAD_STATUS_NEEDS_RESTART = 4;




func get_extension() -> NativeExtension:
	pass;

func get_loaded_extensions() -> PackedStringArray:
	pass;

func is_extension_loaded() -> bool:
	pass;

func load_extension() -> int:
	pass;

func reload_extension() -> int:
	pass;

func unload_extension() -> int:
	pass;


