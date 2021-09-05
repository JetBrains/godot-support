extends Object
class_name EditorFileSystemDirectory



func find_dir_index(name: String) -> int:
    pass;

func find_file_index(name: String) -> int:
    pass;

func get_file(idx: int) -> String:
    pass;

func get_file_count() -> int:
    pass;

func get_file_import_is_valid(idx: int) -> bool:
    pass;

func get_file_path(idx: int) -> String:
    pass;

func get_file_script_class_extends(idx: int) -> String:
    pass;

func get_file_script_class_name(idx: int) -> String:
    pass;

func get_file_type(idx: int) -> StringName:
    pass;

func get_name() -> String:
    pass;

func get_parent() -> EditorFileSystemDirectory:
    pass;

func get_path() -> String:
    pass;

func get_subdir(idx: int) -> EditorFileSystemDirectory:
    pass;

func get_subdir_count() -> int:
    pass;

