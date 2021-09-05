extends Node
class_name EditorFileSystem



func get_file_type(path: String) -> String:
    pass;

func get_filesystem() -> EditorFileSystemDirectory:
    pass;

func get_filesystem_path(path: String) -> EditorFileSystemDirectory:
    pass;

func get_scanning_progress() -> float:
    pass;

func is_scanning() -> bool:
    pass;

func scan() -> void:
    pass;

func scan_sources() -> void:
    pass;

func update_file(path: String) -> void:
    pass;

func update_script_classes() -> void:
    pass;

