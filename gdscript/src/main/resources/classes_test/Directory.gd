extends RefCounted
class_name Directory



func change_dir(todir: String) -> int:
    pass;

func copy(from: String, to: String) -> int:
    pass;

func current_is_dir() -> bool:
    pass;

func dir_exists(path: String) -> bool:
    pass;

func file_exists(path: String) -> bool:
    pass;

func get_current_dir() -> String:
    pass;

func get_current_drive() -> int:
    pass;

func get_drive(idx: int) -> String:
    pass;

func get_drive_count() -> int:
    pass;

func get_next() -> String:
    pass;

func get_space_left() -> int:
    pass;

func list_dir_begin(show_navigational: bool, show_hidden: bool) -> int:
    pass;

func list_dir_end() -> void:
    pass;

func make_dir(path: String) -> int:
    pass;

func make_dir_recursive(path: String) -> int:
    pass;

func open(path: String) -> int:
    pass;

func remove(path: String) -> int:
    pass;

func rename(from: String, to: String) -> int:
    pass;

