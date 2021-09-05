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

var access: int setget set_access, get_access;
var current_dir: String setget set_current_dir, get_current_dir;
var current_file: String setget set_current_file, get_current_file;
var current_path: String setget set_current_path, get_current_path;
var dialog_hide_on_ok: bool setget set_hide_on_ok, get_hide_on_ok;
var disable_overwrite_warning: bool setget set_disable_overwrite_warning, is_overwrite_warning_disabled;
var display_mode: int setget set_display_mode, get_display_mode;
var file_mode: int setget set_file_mode, get_file_mode;
var show_hidden_files: bool setget set_show_hidden_files, is_showing_hidden_files;
var title: String setget set_title, get_title;

func add_filter(filter: String) -> void:
    pass;

func clear_filters() -> void:
    pass;

func get_vbox() -> VBoxContainer:
    pass;

func invalidate() -> void:
    pass;

