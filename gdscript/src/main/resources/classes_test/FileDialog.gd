extends ConfirmationDialog
class_name FileDialog
const FILE_MODE_OPEN_FILE = 0;
const FILE_MODE_OPEN_FILES = 1;
const FILE_MODE_OPEN_DIR = 2;
const FILE_MODE_OPEN_ANY = 3;
const FILE_MODE_SAVE_FILE = 4;
const ACCESS_RESOURCES = 0;
const ACCESS_USERDATA = 1;
const ACCESS_FILESYSTEM = 2;

var access: int;
var current_dir: String;
var current_file: String;
var current_path: String;
var dialog_hide_on_ok: bool;
var file_mode: int;
var filters: PackedStringArray;
var mode_overrides_title: bool;
var show_hidden_files: bool;
var title: String;

func add_filter(filter: String) -> void:
    pass;
func clear_filters() -> void:
    pass;
func deselect_all() -> void:
    pass;
func get_line_edit() -> LineEdit:
    pass;
func get_vbox() -> VBoxContainer:
    pass;
func invalidate() -> void:
    pass;
