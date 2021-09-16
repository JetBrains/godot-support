extends ConfirmationDialog
class_name EditorFileDialog
const FILE_MODE_OPEN_FILE = 0;
const FILE_MODE_OPEN_FILES = 1;
const FILE_MODE_OPEN_DIR = 2;
const FILE_MODE_OPEN_ANY = 3;
const FILE_MODE_SAVE_FILE = 4;
const ACCESS_RESOURCES = 0;
const ACCESS_USERDATA = 1;
const ACCESS_FILESYSTEM = 2;
const DISPLAY_THUMBNAILS = 0;
const DISPLAY_LIST = 1;

var access: int;
var current_dir: String;
var current_file: String;
var current_path: String;
var dialog_hide_on_ok: bool;
var disable_overwrite_warning: bool;
var display_mode: int;
var file_mode: int;
var show_hidden_files: bool;
var title: String;

func add_filter(filter: String) -> void:
    pass;
func clear_filters() -> void:
    pass;
func get_vbox() -> VBoxContainer:
    pass;
func invalidate() -> void:
    pass;
